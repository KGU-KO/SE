package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Application.Domain.Reservation;

public class MainActivity extends AppCompatActivity {

    String id = null;

    final public static int DEFAULT_PORT = 8888;
    //final public static String DEFAULT_IP = "192.168.0.74";
    final public static String DEFAULT_IP = "10.0.2.2";

    private ObjectOutputStream o;
    private ObjectInputStream i;
    private Socket socket;
    Object read = null;
    String reserve_check = "(";
    String success = "$";
    String miss = "#";
    String close = "`";
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null) {
            id = intent.getStringExtra("id");
        }

        //**********************************************************
        //스키장 예약하기 누를때 액션
        Button button = (Button) findViewById(R.id.button);
        //h1 = (Handler)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                //Log.i("USER ID", id);
                Intent intent = new Intent(MainActivity.this, choose_skiplace.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //**********************************************************
        //스키장 예약 확인/취소 누를때 액션
        //**********************************************************
        //**********************************************************
        //예약을 안한경우 이버튼을 누르면 어떡하지??
        //**********************************************************
        //**********************************************************

        Button button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                try {
                    o.writeObject(reserve_check+id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //**********************************************************
        //스키장 이용요금확인 누를때 액션
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, check_use_money.class);

                startActivity(intent);
            }
        });
        //**********************************************************
        //스키장 스키장사이트이동 누를때 액션
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, move_ski_site.class);

                startActivity(intent);
            }
        });

        //탈퇴하기 버튼 누를떄 액션
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, withdraw.class);

                startActivity(intent);
            }
        });

        final Thread worker = new Thread() {
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

                //Reservation reserve = new Reservation();
                ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

                try {
                    while(true) {
                        if(!socket.isClosed()) {
                            reservationList = (ArrayList<Reservation>)i.readObject();
                        }

                        if(reservationList.get(0).getType().equals("success")) {

                            Intent intent = new Intent(MainActivity.this, reserve_info.class);
                            intent.putExtra("listCount", reservationList.size());
                            intent.putExtra("id", id);

                            for(int i=0 ; i<reservationList.size() ; i++) {
                                intent.putExtra("covers"+i, reservationList.get(i).getCovers());
                                intent.putExtra("cost"+i, reservationList.get(i).getCost());
                                intent.putExtra("date_in"+i, reservationList.get(i).getDate_in());
                                intent.putExtra("date_out"+i, reservationList.get(i).getDate_out());
                                intent.putExtra("location"+i, reservationList.get(i).getLocation());
                            }

                            o.flush();
                            o.close();
                            i.close();
                            socket.close();
                            startActivity(intent);
                            finish();
                            break;
                        }

                        else if (reservationList.get(0).getType().equals("fail")) {
                            Looper.prepare();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(MainActivity.this, "예약정보가 없습니다!!!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            Looper.loop();
                            //Toast toast = Toast.makeText(MainActivity.this, "예약정보가 없습니다!!!", Toast.LENGTH_SHORT);
                            //toast.show();
                            Intent intent = new Intent(MainActivity.this, choose_date.class);
                            intent.putExtra("id", id);
                            o.flush();
                            o.close();
                            i.close();
                            socket.close();
                            startActivity(intent);
                            finish();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        worker.start();
    }
}
