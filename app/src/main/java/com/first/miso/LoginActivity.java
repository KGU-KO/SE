package com.first.miso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Application.Domain.User;

public class LoginActivity extends Activity {
    final public static int DEFAULT_PORT = 8888;
    final public static String DEFAULT_IP = "192.168.0.74";
    //final public static String DEFAULT_IP = "10.0.2.2";

    private Socket socket;
    EditText id, pwd;
    Button loginButton, signUpButton, exitButton;
    private ObjectOutputStream o;
    private ObjectInputStream i;
    User user = null;

    int exit = 0;
    String miss = "#";
    String success = "$";
    String newRegister = "@";
    Object read = null;

    Handler d = null;

    //어플 시작시  초기화설정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.id_input);                   // 아이디 입력칸을 찾는다.
        pwd = (EditText) findViewById(R.id.pw_input);                  // 패스워드 입력칸을 찾는다.
        loginButton = (Button) findViewById(R.id.loginButton);          // 로그인 버튼을 찾는다.
        signUpButton = (Button) findViewById(R.id.signUpButton);    // 회원가입 버튼을 찾는다.
        exitButton = (Button) findViewById(R.id.exitButton);        // 종료 버튼을 찾는다.
        d = new Handler();


        exitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    o.writeObject(exit);
                    o.close();
                    i.close();
                    socket.close();
                } catch (IOException e) {
                    Log.e("writeObject error: ", e.toString());
                }
                finish();
            }
        });

        // 버튼을 누르는 이벤트 발생, 이벤트 제어문이기 때문에 이벤트 발생 때마다 발동된다. 시스템이 처리하는 부분이 무한루프문에
        //있더라도 이벤트가 발생하면 자동으로 실행된다.
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                user = new User();

                String data = id.getText().toString();
                user.setId(data);
                String data2 = pwd.getText().toString();
                user.setPwd(data2);
                user.setType("login");

                if (user != null) {
                    try {
                        o.writeObject(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    o.writeObject(newRegister);
                    i.close();
                    o.close();
                    socket.close();
                    finish();
                } catch (IOException e) {
                    Log.e("writeObject error: ", e.toString());
                }

                Intent intent = new Intent(LoginActivity.this, new_register.class);
                startActivity(intent);
            }
        });

        Thread worker = new Thread() {

            public void run() {
                //Looper.prepare();

                try {
                    // 소켓 생성 및 입출력 스트림을 소켓에 연결
                    //socket = new Socket("10.0.2.2", DEFAULT_PORT);                 // local ip
                    socket = new Socket(DEFAULT_IP, DEFAULT_PORT);     // remote ip
                    o = new ObjectOutputStream(socket.getOutputStream());
                    i = new ObjectInputStream(socket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while (true) {
                        //result = (int)i.readObject();
                        if(socket != null && !socket.isClosed()) {
                            read = i.readObject();
                        }
                        else {
                            break;
                        }

                        if(read.toString().equals(success)) {
                            i.close();
                            o.close();
                            socket.close();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("id", user.getId());
                            finish();
                            startActivity(intent);
                        }

                        else if(read.toString().equals(miss)) {
                            d.post(new Runnable() {
                                @Override
                                public void run() {
                                    //Log.w("MISS TYPE", " " + result);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                                    alert.setTitle("ID/PASSWORD 에러");
                                    alert.setMessage("다시 입력하세요!!");
                                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alert.show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Looper.loop();
            }

        };
        worker.start();
    }
}