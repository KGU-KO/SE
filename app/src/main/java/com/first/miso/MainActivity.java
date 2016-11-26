package com.first.miso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    final static public int DEFAULT_PORT = LoginActivity.getDefaultPort();
    final public static String DEFAULT_IP = LoginActivity.getDefaultIP();

    private ObjectOutputStream o;
    private ObjectInputStream i;
    private Socket socket;
    Object read = null;
    String reserve_check = "(";
    String withdraw = ")";
    Handler handler1 = null;
    Handler handler2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler1 = new Handler();
        handler2 = new Handler();

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
                Intent intent = new Intent(MainActivity.this, Choose_Skiplace.class);
                intent.putExtra("id", id);
                finish();
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
                Intent intent = new Intent(MainActivity.this, Check_Use_Money.class);
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
                Intent intent = new Intent(MainActivity.this, Move_Ski_Site.class);
                startActivity(intent);
            }
        });

        //탈퇴하기 버튼 누를떄 액션
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("회원 탈퇴");
                        alert.setMessage("탈퇴하시겠습니까?");

                        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    o.writeObject(withdraw + id);
                                    o.flush();
                                    i.close();
                                    o.close();
                                    socket.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        });
                        alert.show();
                    }
                });
            }
        });

        final Thread worker = new Thread() {
            public void run() {
                try {
                    // 소켓 생성 및 입출력 스트림을 소켓에 연결
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

                            Intent intent = new Intent(MainActivity.this, Reserve_Info.class);
                            intent.putExtra("listCount", reservationList.size());
                            intent.putExtra("id", id);

                            for(int i=0 ; i<reservationList.size() ; i++) {
                                intent.putExtra("number"+i, reservationList.get(i).getNumber());
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

                        // 토스트 수정!!!
                        else if (reservationList.get(0).getType().equals("fail")) {
                            handler1.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(MainActivity.this, "예약정보가 없습니다!!!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
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
