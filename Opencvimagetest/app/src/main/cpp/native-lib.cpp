
#include <jni.h>
#include "opencv2/core.hpp"
#include "opencv2/core/utility.hpp"
#include "opencv2/core/ocl.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/features2d.hpp"
#include "opencv2/calib3d.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/xfeatures2d.hpp"
#include <iostream>
#include <android/log.h>

using namespace cv;
using namespace cv::xfeatures2d;
using namespace std;

const int LOOP_NUM = 10;
const int GOOD_PTS_MAX = 50;
const float GOOD_PORTION = 0.15f;

int64 work_begin = 0;
int64 work_end = 0;

static void workBegin()
{
    work_begin = getTickCount();
}

static void workEnd()
{
    work_end = getTickCount() - work_begin;
}

static double getTime()
{
    return work_end / ((double)getTickFrequency()) * 1000.;
}

struct SURFDetector
{
    Ptr<Feature2D> surf;
    SURFDetector(double hessian = 800.0)
    {
        surf = SURF::create(hessian);
    }
    template<class T>
    void operator()(const T& in, const T& mask, std::vector<cv::KeyPoint>& pts, T& descriptors, bool useProvided = false)
    {
        surf->detectAndCompute(in, mask, pts, descriptors, useProvided);
    }
};

template<class KPMatcher>
struct SURFMatcher
{
    KPMatcher matcher;
    template<class T>
    void match(const T& in1, const T& in2, std::vector<cv::DMatch>& matches)
    {
        matcher.match(in1, in2, matches);
    }
};

static Mat drawGoodMatches(
        const Mat& img1,
        const Mat& img2,
        const std::vector<KeyPoint>& keypoints1,
        const std::vector<KeyPoint>& keypoints2,
        std::vector<DMatch>& matches,
        std::vector<Point2f>& scene_corners_
)
{
    //-- Sort matches and preserve top 10% matches
    std::sort(matches.begin(), matches.end());
    std::vector< DMatch > good_matches;
    double minDist = matches.front().distance;
    double maxDist = matches.back().distance;

    const int ptsPairs = std::min(GOOD_PTS_MAX, (int)(matches.size() * GOOD_PORTION));
    for (int i = 0; i < ptsPairs; i++)
    {
        good_matches.push_back(matches[i]);
    }
    std::cout << "\nMax distance: " << maxDist << std::endl;
    std::cout << "Min distance: " << minDist << std::endl;

    std::cout << "Calculating homography using " << ptsPairs << " point pairs." << std::endl;

    // drawing the results
    Mat img_matches;


    drawMatches(img1, keypoints1, img2, keypoints2,
                good_matches, img_matches, Scalar::all(-1), Scalar::all(-1),
                std::vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);


    //-- Localize the object
    std::vector<Point2f> obj;
    std::vector<Point2f> scene;

    for (size_t i = 0; i < good_matches.size(); i++)
    {
        //-- Get the keypoints from the good matches
        obj.push_back(keypoints1[good_matches[i].queryIdx].pt);
        scene.push_back(keypoints2[good_matches[i].trainIdx].pt);
    }
    //-- Get the corners from the image_1 ( the object to be "detected" )
    std::vector<Point2f> obj_corners(4);
    obj_corners[0] = Point(0, 0);
    obj_corners[1] = Point(img1.cols, 0);
    obj_corners[2] = Point(img1.cols, img1.rows);
    obj_corners[3] = Point(0, img1.rows);
    std::vector<Point2f> scene_corners(4);

    Mat H = findHomography(obj, scene, RANSAC);
    perspectiveTransform(obj_corners, scene_corners, H);

    scene_corners_ = scene_corners;

    //-- Draw lines between the corners (the mapped object in the scene - image_2 )
    line(img_matches,
         scene_corners[0] + Point2f((float)img1.cols, 0), scene_corners[1] + Point2f((float)img1.cols, 0),
         Scalar(0, 255, 0), 2, LINE_AA);
    line(img_matches,
         scene_corners[1] + Point2f((float)img1.cols, 0), scene_corners[2] + Point2f((float)img1.cols, 0),
         Scalar(0, 255, 0), 2, LINE_AA);
    line(img_matches,
         scene_corners[2] + Point2f((float)img1.cols, 0), scene_corners[3] + Point2f((float)img1.cols, 0),
         Scalar(0, 255, 0), 2, LINE_AA);
    line(img_matches,
         scene_corners[3] + Point2f((float)img1.cols, 0), scene_corners[0] + Point2f((float)img1.cols, 0),
         Scalar(0, 255, 0), 2, LINE_AA);

    return img_matches;
}


float resize(UMat img_src, UMat &img_resize, int resize_width){

    float scale = resize_width / (float)img_src.cols ;

    if (img_src.cols > resize_width) {
        int new_height = cvRound(img_src.rows * scale);
        resize(img_src, img_resize, Size(resize_width, new_height));
    }
    else {
        img_resize = img_src;
    }
    return scale;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_opencvimagetest_MainActivity_imageprocessing(JNIEnv *env, jobject thiz,
                                                              jlong object_image,
                                                              jlong scene_image) {


    ocl::setUseOpenCL(true);

    UMat img1, img2;

    Mat &img_object = *(Mat *) object_image
    Mat &img_scene = *(Mat *) scene_image;



    img_object.copyTo(img1);
    img_scene.copyTo(img2);

    float resizeRatio = resize(img2, img2, 800);
    resize(img1, img1, 800);


    cvtColor( img1, img1, COLOR_RGBA2GRAY);
    cvtColor( img2, img2, COLOR_RGBA2GRAY);


    double surf_time = 0.;

    //declare input/output
    std::vector<KeyPoint> keypoints1, keypoints2;
    std::vector<DMatch> matches;

    UMat _descriptors1, _descriptors2;
    Mat descriptors1 = _descriptors1.getMat(ACCESS_RW),
            descriptors2 = _descriptors2.getMat(ACCESS_RW);

    //instantiate detectors/matchers
    SURFDetector surf;

    SURFMatcher<BFMatcher> matcher;

    //-- start of timing section

    for (int i = 0; i <= LOOP_NUM; i++)
    {
        if (i == 1) workBegin();
        surf(img1.getMat(ACCESS_READ), Mat(), keypoints1, descriptors1);
        surf(img2.getMat(ACCESS_READ), Mat(), keypoints2, descriptors2);
        matcher.match(descriptors1, descriptors2, matches);
    }
    workEnd();

    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ",
                        "%d keypoints on object image", keypoints1.size());
    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ",
                        "%d keypoints on scene image", keypoints2.size());

    surf_time = getTime();
    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ",
                        "SURF run time:  %f ms", surf_time / LOOP_NUM);


    std::vector<Point2f> corner;
    Mat img_matches = drawGoodMatches(img1.getMat(ACCESS_READ), img2.getMat(ACCESS_READ), keypoints1, keypoints2, matches, corner);


    line(img_scene, Point2f(corner[0].x/resizeRatio, corner[0].y/resizeRatio), Point2f(corner[1].x/resizeRatio, corner[1].y/resizeRatio), Scalar(0, 255, 0, 255), 10);
    line(img_scene, Point2f(corner[1].x/resizeRatio, corner[1].y/resizeRatio), Point2f(corner[2].x/resizeRatio, corner[2].y/resizeRatio), Scalar(0, 255, 0, 255), 10);
    line(img_scene, Point2f(corner[2].x/resizeRatio, corner[2].y/resizeRatio), Point2f(corner[3].x/resizeRatio, corner[3].y/resizeRatio), Scalar(0, 255, 0, 255), 10);
    line(img_scene, Point2f(corner[3].x/resizeRatio, corner[3].y/resizeRatio), Point2f(corner[0].x/resizeRatio, corner[0].y/resizeRatio), Scalar(0, 255, 0, 255), 10);

    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "draw box %f %f", corner[0].x/resizeRatio, corner[0].y/resizeRatio );
    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "draw box %f %f", corner[1].x/resizeRatio, corner[1].y/resizeRatio );
    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "draw box %f %f", corner[2].x/resizeRatio, corner[2].y/resizeRatio );
    __android_log_print(ANDROID_LOG_DEBUG, "native-lib :: ", "draw box %f %f", corner[3].x/resizeRatio, corner[3].y/resizeRatio );

    // TODO: implement imageprocessing()
}