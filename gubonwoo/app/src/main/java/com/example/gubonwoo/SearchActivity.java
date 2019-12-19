package com.example.gubonwoo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SearchActivity extends AppCompatActivity {

    ListView listview = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);




        // 코드 계속 ...
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_1),
                "전면 안개등", "I'm not the only one.\r\nStay with me.\r\n") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_2),
                "파워 스티어링 경고등", "heaven.\r\nI do it for you.") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_3),
                "후면 안개등", "Tears in heaven.\r\nChange the world.") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_4),
                "워셔액 부족", "Still got the blues.\r\nOne day.") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_5),
                "브레이크 패드 경고등", "A tale that wasn't right.\r\nI want out.") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_6),
                "정속주행 켜짐", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_7),
                "방향 지시등", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_8),
                "빗방울과 빛 감지기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_9),
                "동절기 모드", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_10),
                "정보 표시장치", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_11),
                "예열 플러그", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_12),
                "서리주의", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_13),
                "점화 스위치 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_14),
                "차내 열쇠 없음", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_15),
                "전자 열쇠 배터리 부족", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_16),
                "차간 거리 경보장치", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_17),
                "클러치 페달 밟기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_18),
                "브레이크 페달 밟기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_19),
                "스티어링 잠금장치", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_20),
                "전조등", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_21),
                "타이어 압력 부족", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_22),
                "차폭등 정보", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_23),
                "외부 경관 등 결함", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_24),
                "브레이크 등 경보", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_25),
                "디젤 배기가스 후처리 장치 경보", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_26),
                "트레일러 견인 장치 경보", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_27),
                "공기현가장치 경보", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_28),
                "차선이탈 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_29),
                "촉매변환 장치 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_30),
                "안전벨트 미착용", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_31),
                "주차브레이크 등", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_32),
                "배터리 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_33),
                "주차 보조 장치", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_34),
                "오일 교환 등 서비스 요망", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_35),
                "자동 전조등", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_36),
                "전조등 번위 조정기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_37),
                "후방 스포일러 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_38),
                "컨버터블 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_39),
                "에어백 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_40),
                "수동식 브레이크 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_41),
                "연료여과기 물 있음", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_42),
                "에어백 작동 정지", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_43),
                "결함 문제", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_44),
                "전조등 하향", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_45),
                "공기여과기 더러움", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_46),
                "경제운전 지시기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_47),
                "내리막길 주행 제어장치", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_48),
                "온도 주의", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_49),
                "쏠림 방지 브레이크 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_50),
                "연료필터 주의", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_51),
                "문 열림", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_52),
                "보닛 열림", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_53),
                "연료 부족", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_54),
                "자동 기어 박스 경고", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_55),
                "속도제한기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_56),
                "현가장치 조절기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_57),
                "유압 부족", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_58),
                "앞 유리 성에 제거", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_59),
                "트렁크 열림", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_60),
                "안전 제어장치 꺼짐", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_61),
                "빛방울 감지기", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_62),
                "엔진/배출 경고등", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_63),
                "뒷 유리창 성에 제거", "Hello.\r\nSomeone like you.") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.i_64),
                "와이퍼 작동", "Hello.\r\nSomeone like you.") ;


        EditText editTextFilter = (EditText)findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText) ;
                } else {
                    listview.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;
    }

}