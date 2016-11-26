package com.first.miso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Application.Domain.User;

public class New_Register extends AppCompatActivity {

    final static public int DEFAULT_PORT = LoginActivity.getDefaultPort();
    final public static String DEFAULT_IP = LoginActivity.getDefaultIP();

    private Socket socket;
    private ObjectOutputStream o;
    private ObjectInputStream i;
    EditText name, phone, id, pwd;
    Object read;
    String newRegisterFail = "%", newRegisterSuccess = "^";
    Handler handler1, handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_register);

        name = (EditText) findViewById(R.id.name_input);
        phone = (EditText) findViewById(R.id.phone_input);
        id = (EditText) findViewById(R.id.id_input);
        pwd = (EditText) findViewById(R.id.pwd_input);
        handler1 = new Handler();
        handler2 = new Handler();

        Button register_make_button= (Button) findViewById(R.id. register_make_button);
        register_make_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User newUser = new User();

                newUser.setName(name.getText().toString());
                newUser.setPhone(phone.getText().toString());
                newUser.setId(id.getText().toString());
                newUser.setPwd(pwd.getText().toString());
                newUser.setRegister(true);
                newUser.setType("signUp");

                try {
                    o.writeObject((Object)newUser);
                    o.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button register_cancel_button = (Button) findViewById(R.id. register_cancel_button);
        register_cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User newUser = new User();
                newUser.setRegister(false);
                newUser.setType("signUp");

                try {
                    o.writeObject(newUser);
                    o.flush();
                    o.close();
                    i.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(New_Register.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        Thread worker = new Thread() {

            public void run() {

                try {
                    // 소켓 생성 및 입출력 스트림을 소켓에 연결
                    socket = new Socket(DEFAULT_IP, DEFAULT_PORT);
                    o = new ObjectOutputStream(socket.getOutputStream());
                    i = new ObjectInputStream(socket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while (true) {
                        if(socket != null && !socket.isClosed()) {
                            read = i.readObject();
                        }
                        else {
                            break;
                        }

                        if(read.toString().equals(newRegisterFail)) {
                            handler1.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(New_Register.this);
                                    alert.setTitle("회원가입 에러");
                                    alert.setMessage("아이디가 중복됩니다!!");
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

                        else if(read.toString().equals(newRegisterSuccess)) {
                            handler2.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(New_Register.this, "회원가입 성공!!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            o.close();
                            i.close();
                            socket.close();

                            Intent intent = new Intent(New_Register.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
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
