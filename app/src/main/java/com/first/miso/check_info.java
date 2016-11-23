package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Application.Domain.Reservation;

public class check_info extends AppCompatActivity {
    final public static int DEFAULT_PORT = 8888;
    final public static String DEFAULT_IP = "192.168.0.74";
    //final public static String DEFAULT_IP = "10.0.2.2";

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
                Reservation reserve = new Reservation();
                reserve.setId(id);
                reserve.setCovers(covers2);
                reserve.setCost(cost);
                reserve.setDate_in(date_in);
                reserve.setDate_out(date_out);
                reserve.setLocation(location);
                reserve.setType("reservation");
                try {
                    o.writeObject(reserve);
                    o.flush();
                    o.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(check_info.this, MainActivity.class);
                intent.putExtra("id", id);
                finish();
                startActivity(intent);
            }
        });
        //취소하기 버튼 누를때
        Button cancelbutton = (Button) findViewById(R.id.cancelbutton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(check_info.this, MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        Thread worker = new Thread() {
            public void run() {
                try {
                    // 소켓 생성 및 입출력 스트림을 소켓에 연결
                    //socket = new Socket("10.0.2.2", DEFAULT_PORT);                 // local ip
                    socket = new Socket(DEFAULT_IP, DEFAULT_PORT);     // remote ip
                    o = new ObjectOutputStream(socket.getOutputStream());
                    i = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        worker.start();
    }
}
