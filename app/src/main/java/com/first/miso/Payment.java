package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Application.Domain.PaymentInf;
import Application.Domain.Reservation;


public class Payment extends AppCompatActivity{

    final static public int DEFAULT_PORT = LoginActivity.getDefaultPort();
    final public static String DEFAULT_IP = LoginActivity.getDefaultIP();

    private ObjectOutputStream o;
    private ObjectInputStream i;
    private Socket socket;

    int cost;
    String date_in;
    String date_out;
    int covers2;
    String id = null;
    String location;
    String accountall;
    String selectedbanktype = null;

    TextView textpaycost;
    Spinner spinnerbanktype;
    TextView account1, account2, account3;
    Button btnpayment;
    RadioGroup bankgroup;

    Handler handler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        handler1 = new Handler();

        textpaycost = (TextView)findViewById(R.id.paycost);
        bankgroup = (RadioGroup)findViewById(R.id.bankgroup);
        account1 = (TextView)findViewById(R.id.ll2);
        account2 = (TextView)findViewById(R.id.ll4);
        account3 = (TextView)findViewById(R.id.ll6);
        btnpayment = (Button)findViewById(R.id.btnpay);

        Intent intent = getIntent();
        if(intent != null) {
            id = intent.getStringExtra("id");
            cost = intent.getIntExtra("cost2", 0);
            date_in = intent.getStringExtra("date_in");
            date_out = intent.getStringExtra("date_out");
            covers2 = intent.getIntExtra("covers2", 0);
            location = intent.getStringExtra("location");
        }

        textpaycost.setText("결제 금액 : "+ cost +"원");

        RadioGroup.OnCheckedChangeListener bankcheck=
                new RadioGroup.OnCheckedChangeListener(){
                    public void onCheckedChanged(RadioGroup group, int checkedid){
                        if(group.getId() == R.id.bankgroup) {
                            switch (checkedid) {
                                case R.id.sinhan:
                                    selectedbanktype = "신한은행";
                                    break;
                                case R.id.kookmin:
                                    selectedbanktype = "국민은행";
                                    break;
                                case R.id.woori:
                                    selectedbanktype = "우리은행";
                                    break;
                            }
                        }
                    }
                };

        bankgroup.setOnCheckedChangeListener(bankcheck);

        btnpayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Object> paymentlist = new ArrayList<Object>();

                if((account1.getText()!=null)&&(account2.getText()!=null)&&(account2.getText()!=null)){
                    accountall = account1.getText()+"-"+account2.getText()+"-"+account3.getText();
                }else{
                    int nevertouch3 = 0;
                }

                Reservation reserve = new Reservation();
                reserve.setId(id);
                reserve.setCovers(covers2);
                reserve.setCost(cost);
                reserve.setDate_in(date_in);
                reserve.setDate_out(date_out);
                reserve.setLocation(location);
                reserve.setType("reservation");

                PaymentInf pay = new PaymentInf();
                pay.setId(id);
                pay.setBanktype(selectedbanktype);
                pay.setAccount(accountall);
                pay.setCharge(cost);
                pay.setType("payment");

                paymentlist.add(reserve);
                paymentlist.add(pay);

                try {
                    o.writeObject(paymentlist);
                    o.flush();
                    o.close();
                    i.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Payment.this, "결제 성공!!!", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(Payment.this, MainActivity.class);
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
}