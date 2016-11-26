package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Check_Info extends AppCompatActivity {

    int cost;
    String date_in;
    String date_out;
    int covers2;
    String id = null;
    String location;

    TextView textdayin, textdayout;
    TextView textcovers, textcost;

    private ObjectOutputStream o;
    private ObjectInputStream i;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        Intent intent = getIntent();
        if(intent != null) {
            id = intent.getStringExtra("id");
            cost = Integer.parseInt(intent.getStringExtra("cost2"));
            date_in = intent.getStringExtra("date_in");
            date_out = intent.getStringExtra("date_out");
            covers2 = intent.getIntExtra("covers2", 0);
            location = intent.getStringExtra("location");
        }

        textdayin = (TextView)findViewById(R.id.dayin3);
        textdayin.setText("입실 : "+date_in);

        textdayout = (TextView)findViewById(R.id.dayout3);
        textdayout.setText("퇴실 : "+date_out);

        textcovers = (TextView)findViewById(R.id.covers3);
        textcovers.setText("인원수 : "+covers2+"명");

        textcost = (TextView)findViewById(R.id.cost3);
        textcost.setText("요금 : "+cost+"원");

        //결제하기 버튼 누를때
        Button paybutton = (Button) findViewById(R.id.paybutton);
        paybutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Check_Info.this, Payment.class);
                intent.putExtra("id", id);
                intent.putExtra("covers2", covers2);
                intent.putExtra("date_in", date_in);
                intent.putExtra("date_out", date_out);
                intent.putExtra("cost2", cost);
                intent.putExtra("location", location);
                finish();
                startActivity(intent);
            }
        });

        //취소하기 버튼 누를때
        Button cancelbutton = (Button) findViewById(R.id.cancelbutton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Check_Info.this, MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}