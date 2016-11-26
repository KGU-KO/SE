package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class Choose_Date extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ArrayList<String> mItems;
    ArrayAdapter<String> adapter;
    TextView textYear;
    TextView textMon;
    TextView textDayin;
    TextView textDayout;
    String dayin2;
    String dayout2;

    TextView textCost;
    String textCost2 = "0";
    String date_in = "0";
    String date_out = "0";
    TextView textCovers;

    Button btninc;
    Button btndec;
    Button btnres;

    int covers2 = 0;
    int choose = 0;
    String id = null;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_date);

        Intent i = getIntent();
        if(i != null) {
            id = i.getStringExtra("id");
            location = i.getStringExtra("location");
        }

        textYear = (TextView) this.findViewById(R.id.edit1);
        textMon = (TextView) this.findViewById(R.id.edit2);

        textDayin = (TextView) this.findViewById(R.id.inRoom2);
        textDayout = (TextView) this.findViewById(R.id.outRoom2);

        textCost = (TextView) this.findViewById(R.id.cost2);
        textCovers = (TextView) this.findViewById(R.id.covers2);

        btninc = (Button)this.findViewById(R.id.inc);
        btninc.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                covers2++;
                textCovers.setText(covers2+"");
                //textCost2 = Integer.toString(covers2*15000*(Integer.parseInt(dayout2) - Integer.parseInt(dayin2)));//하루 요금 15000
                //textCost.setText(textCost2+"원");
            }
        });

        btndec = (Button)this.findViewById(R.id.dec);
        btndec.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                covers2--;
                if(covers2 < 0) covers2 = 0;
                textCovers.setText(covers2+"");
            }
        });

        btnres = (Button)this.findViewById(R.id.res);
        btnres.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if((covers2 > 0) && (Integer.parseInt(dayin2) > 0) && (Integer.parseInt(dayout2) > 0)){
                    textCost2 = Integer.toString(covers2*10000*(Integer.parseInt(dayout2) - Integer.parseInt(dayin2)));//하루 요금 15000
                    textCost.setText(textCost2+"원");
                }
                else{
                    int nevertouch2 = 0;
                }
            }
        });

        mItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);


        GridView gird = (GridView) this.findViewById(R.id.grid1);
        gird.setAdapter(adapter);
        gird.setOnItemClickListener(this);

        Date date = new Date(); // 오늘에 날짜를 세팅해 준다.
        int year = date.getYear() + 1900;
        int mon = date.getMonth() + 1;
        textYear.setText(year + "");
        textMon.setText(mon + "");

        fillDate(year, mon);

        Button btnmove = (Button)this.findViewById(R.id.bt1);
        btnmove.setOnClickListener(this);

        // 예약 버튼
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Choose_Date.this, Check_Info.class);
                intent.putExtra("id", id);
                intent.putExtra("covers2", covers2);
                intent.putExtra("date_in", date_in);
                intent.putExtra("date_out", date_out);
                intent.putExtra("cost2", textCost2);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });
        //*****************************
        // previous 버튼 누를때
        Button date_previouse_button = (Button) findViewById(R.id.date_previouse_button);
        date_previouse_button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(Choose_Date.this, Choose_Skiplace.class); //이 빨간 부분에 결제화면 액티비티 이름 넣어주기!

                startActivity(intent);
            }
        });

        //*****************************
        // Main 버튼 누를때

        Button date_main_button = (Button) findViewById(R.id.date_main_button);
        date_main_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Choose_Date.this, MainActivity.class); //이 빨간 부분에 결제화면 액티비티 이름 넣어주기!

                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId() == R.id.bt1){
            int year = Integer.parseInt(textYear.getText().toString());
            int mon = Integer.parseInt(textMon.getText().toString());
            fillDate(year,mon);
        }
    }

    private void fillDate(int year, int mon) {
        mItems.clear();

        mItems.add("일");
        mItems.add("월");
        mItems.add("화");
        mItems.add("수");
        mItems.add("목");
        mItems.add("금");
        mItems.add("토");

        Date current = new Date(year - 1900, mon - 1, 1);
        int day = current.getDay(); //요일도 int로 저장

        for (int i = 0; i < day; i++) {
            mItems.add("");
        }

        current.setDate(32);    // 32일까지 입력하면 1일로 바꿔준다.
        int last = 32 - current.getDate();

        for (int i = 1; i <= last; i++) {
            mItems.add(i + "");
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if(mItems.get(arg2).equals("")) {
            int nevertouch = 0;
        }
        else {
            if(choose == 1){
                dayout2 = mItems.get(arg2);
                if(Integer.parseInt(dayin2) > Integer.parseInt(dayout2)){
                    textDayin.setText(dayout2);
                    textDayout.setText(dayin2);
                }
                else{
                    textDayin.setText(dayin2);
                    textDayout.setText(dayout2);
                }
            }else {
                dayin2 = mItems.get(arg2);
                textDayin.setText(dayin2);
                choose++;
            }
        }
        date_in = textYear.getText().toString()+"년 " + textMon.getText().toString()+"월 " + textDayin.getText().toString()+"일";
        date_out = textYear.getText().toString()+"년 " + textMon.getText().toString()+"월 " + textDayout.getText().toString()+"일";
    }
}