package com.example.gubonwoo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends Activity {
  /*  ListView listView;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView)this.findViewById(R.id.listView);

        String [] values = { "전면 안개등", "파워 스티어링 경고등", "후면 안개등", "워셔액 부족", "브레이크 패드 경고등",
                "정속주행 켜짐", "방향 지시등", " 빗방울과 빛 감지기", "동절기 모드", "정보 표시장치",
                "예열 플러그", "서리주의", "점화 스위치 경고", "차내 열쇠 없음", "전자 열쇠 배터리 부족",
                "차간 거리 경보장치","클러치 페달 밟기","브레이크 페달 밟기","스티어링 잠금장치","전조등",
                "타이어 압력 부족","차폭등 정보","외부 경관 등 결함","브레이크 등 경보","디젤 배기가스 후처리 장치 경보",
                "트레일러 견인 장치 경보","공기현가장치 경보","차선이탈 경고","촉매변환 장치 경고","안전벨트 미착용",
                "주차브레이크 등","배터리 경고","주차 보조 장치","오일 교환 등 서비스 요망","자동 전조등","전조등 번위 조정기",
                "후방 스포일러 경고","컨버터블 경고","에어백 경고","수동식 브레이크 경고","연료여과기 물 있음","에어백 작동 정지",
                "결함 문제","전조등 하향","공기여과기 더러움","경제운전 지시기","내리막길 주행 제어장치","온도 주의","쏠림 방지 브레이크 경고",
                "연료필터 주의","문 열림","보닛 열림","연료 부족","자동 기어 박스 경고","속도제한기","현가장치 조절기","유압 부족",
                "앞 유리 성에 제거","트렁크 열림","안전 제어장치 꺼짐","빛방울 감지기","엔진/배출 경고등","뒷 유리창 성에 제거","와이퍼 작동"};
        ArrayList<String> items = new ArrayList<>();

        for (int i=0;i<64;i++)
            items.add(values[i]);

        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.activity_item, null);
            }

            // ImageView 인스턴스
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

            String [] values = { "전면 안개등", "파워 스티어링 경고등", "후면 안개등", "워셔액 부족", "브레이크 패드 경고등",
                    "정속주행 켜짐", "방향 지시등", " 빗방울과 빛 감지기", "동절기 모드", "정보 표시장치",
                    "예열 플러그", "서리주의", "점화 스위치 경고", "차내 열쇠 없음", "전자 열쇠 배터리 부족",
                    "차간 거리 경보장치","클러치 페달 밟기","브레이크 페달 밟기","스티어링 잠금장치","전조등",
                    "타이어 압력 부족","차폭등 정보","외부 경관 등 결함","브레이크 등 경보","디젤 배기가스 후처리 장치 경보",
                    "트레일러 견인 장치 경보","공기현가장치 경보","차선이탈 경고","촉매변환 장치 경고","안전벨트 미착용",
                    "주차브레이크 등","배터리 경고","주차 보조 장치","오일 교환 등 서비스 요망","자동 전조등","전조등 번위 조정기",
                    "후방 스포일러 경고","컨버터블 경고","에어백 경고","수동식 브레이크 경고","연료여과기 물 있음","에어백 작동 정지",
                    "결함 문제","전조등 하향","공기여과기 더러움","경제운전 지시기","내리막길 주행 제어장치","온도 주의","쏠림 방지 브레이크 경고",
                    "연료필터 주의","문 열림","보닛 열림","연료 부족","자동 기어 박스 경고","속도제한기","현가장치 조절기","유압 부족",
                    "앞 유리 성에 제거","트렁크 열림","안전 제어장치 꺼짐","빛방울 감지기","엔진/배출 경고등","뒷 유리창 성에 제거","와이퍼 작동"};
            // 리스트뷰의 아이템에 이미지를 변경한다.
            if(values[0].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[1].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[2].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[3].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[4].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[5].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[6].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[7].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[8].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[9].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[10].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[11].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[12].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[13].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[14].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[15].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[16].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[17].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[18].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[19].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[20].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[21].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[22].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[23].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[24].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[25].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[26].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[27].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[28].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[29].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[30].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[31].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[32].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[33].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[34].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[35].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[36].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[37].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[38].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[39].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[40].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[41].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[42].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[43].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[44].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[45].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[46].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[47].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[48].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[49].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[50].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[51].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[52].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[53].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[54].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[56].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[57].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[58].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[59].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[60].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[61].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[62].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);
            else if(values[63].equals(items.get(position)))
                imageView.setImageResource(R.drawable.i_1);






            TextView textView = (TextView)v.findViewById(R.id.textView);
            textView.setText(items.get(position));

            final String text = items.get(position);
            Button button = (Button)v.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });

            return v;
        }
    }
*/

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        String [] values = { "전면 안개등", "파워 스티어링 경고등", "후면 안개등", "워셔액 부족", "브레이크 패드 경고등",
//                "정속주행 켜짐", "방향 지시등", " 빗방울과 빛 감지기", "동절기 모드", "정보 표시장치",
//                "예열 플러그", "서리주의", "점화 스위치 경고", "차내 열쇠 없음", "전자 열쇠 배터리 부족",
//                "차간 거리 경보장치","클러치 페달 밟기","브레이크 페달 밟기","스티어링 잠금장치","전조등",
//                "타이어 압력 부족","차폭등 정보","외부 경관 등 결함","브레이크 등 경보","디젤 배기가스 후처리 장치 경보",
//                "트레일러 견인 장치 경보","공기현가장치 경보","차선이탈 경고","촉매변환 장치 경고","안전벨트 미착용",
//                "주차브레이크 등","배터리 경고","주차 보조 장치","오일 교환 등 서비스 요망","자동 전조등","전조등 번위 조정기",
//                "후방 스포일러 경고","컨버터블 경고","에어백 경고","수동식 브레이크 경고","연료여과기 물 있음","에어백 작동 정지",
//                "결함 문제","전조등 하향","공기여과기 더러움","경제운전 지시기","내리막길 주행 제어장치","온도 주의","쏠림 방지 브레이크 경고",
//                "연료필터 주의","문 열림","보닛 열림","연료 부족","자동 기어 박스 경고","속도제한기","현가장치 조절기","유압 부족",
//                "앞 유리 성에 제거","트렁크 열림","안전 제어장치 꺼짐","빛방울 감지기","엔진/배출 경고등","뒷 유리창 성에 제거","와이퍼 작동"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, values);
//
//        setListAdapter(adapter);
//    }
//
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        String item = (String) getListAdapter().getItem(position);
//        Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
//    }



}
