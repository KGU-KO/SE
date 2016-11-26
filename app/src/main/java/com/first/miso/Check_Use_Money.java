package com.first.miso;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Check_Use_Money extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_use_money);

        // 곤지암 스키장 요금표
        Button usemoney_button1 = (Button) findViewById(R.id.usemoney_button1);
        usemoney_button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.konjiamresort.co.kr/ski/useInfo.dev")));
            }
        });

        // 알펜시아 스키장 요금표
        Button usemoney_button2 = (Button) findViewById(R.id.usemoney_button2);
        usemoney_button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.alpensiaresort.co.kr/AlSkiSkiUseInfo.gdc")));
            }
        });

        // 덕유산 스키장 요금표
        Button usemoney_button3 = (Button) findViewById(R.id.usemoney_button3);
        usemoney_button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mdysresort.com/ski/price_lift_131104.asp")));
            }
        });

        // 비발디 파크 요금표
        Button usemoney_button4 = (Button) findViewById(R.id.usemoney_button4);
        usemoney_button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.daemyungresort.com/daemyung.vp.skiworld.04_04_01_01.ds/dmparse.dm")));
            }
        });

        // Main 버튼 클릭시
        Button usemoney_main = (Button) findViewById(R.id.usemoney_main);
        usemoney_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Check_Use_Money.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
