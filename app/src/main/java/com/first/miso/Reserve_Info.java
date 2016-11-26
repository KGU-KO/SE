package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import Application.Domain.Reservation;

public class Reserve_Info extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final static public int DEFAULT_PORT = LoginActivity.getDefaultPort();
    final public static String DEFAULT_IP = LoginActivity.getDefaultIP();

    private ObjectOutputStream o;
    private ObjectInputStream i;
    private Socket socket;

    int selectedIndex = 0;
    String id = null;
    int listCount = 0;
    ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

    ArrayList<String> mItems;
    ArrayAdapter<String> adapter;

    ListView listView;
    Button btnDeleteReserve;

    Handler handler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_info);
        handler1 = new Handler();

        Intent intent = getIntent();
        if(intent != null) {
            id = intent.getStringExtra("id");
            listCount = intent.getIntExtra("listCount", 0);

            for(int index=0 ; index<listCount ; index++) {
                Reservation reserve = new Reservation();
                reserve.setNumber(intent.getIntExtra("number"+index, 0));
                reserve.setCovers(intent.getIntExtra("covers"+index, 0));
                reserve.setCost(intent.getIntExtra("cost"+index, 0));
                reserve.setDate_in(intent.getStringExtra("date_in"+index));
                reserve.setDate_out(intent.getStringExtra("date_out"+index));
                reserve.setLocation(intent.getStringExtra("location"+index));
                reservationList.add(reserve);
            }
        }

        listView = (ListView)findViewById(R.id.listReserve);
        btnDeleteReserve = (Button)findViewById(R.id.btnDelete);

        mItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fillList(listCount, reservationList);

        btnDeleteReserve.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Reserve_Info.this, "예약 취소 성공!!!", Toast.LENGTH_SHORT).show();
                    }
                });

                try {
                    reservationList.get(selectedIndex).setId(id);
                    reservationList.get(selectedIndex).setType("reservation_delete");
                    o.writeObject(reservationList.get(selectedIndex));
                    o.flush();
                    o.close();
                    i.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Reserve_Info.this, MainActivity.class);
                intent.putExtra("id", id);
                finish();
                startActivity(intent);
            }
        });

        Thread worker = new Thread() {
            public void run() {
                try {
                    // 소켓 생성 및 입출력 스트림을 소켓에 연결
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

    protected void fillList(int listcount, ArrayList<Reservation> reserve){

        for(int i=0; i<listcount; i++){
            mItems.add("입실 : "+reserve.get(i).getDate_in()+"  ~  퇴실 : "+reserve.get(i).getDate_out()+
                    "\n장소 : "+reserve.get(i).getLocation()+" 인원수 : "+reserve.get(i).getCovers()+" 요금 : "+reserve.get(i).getCost());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mItems.get(i).equals("")) {
        }
        else {
            selectedIndex = i;
        }
    }
}