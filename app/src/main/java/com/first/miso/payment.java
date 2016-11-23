package com.first.miso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Application.Domain.PaymentInf;
import Application.Domain.Reservation;


public class payment extends AppCompatActivity {

    final public static int DEFAULT_PORT = 8888;
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
    String[] banktype2 = {"신한 은행", "국민 은행", "우리 은행"};
    String selectedbanktype = null;

    TextView textpaycost;
    Spinner spinnerbanktype;
    TextView account1, account2, account3, account4;
    Button btnpayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        textpaycost = (TextView)findViewById(R.id.paycost);
        spinnerbanktype = (Spinner)findViewById(R.id.banktype);
        account1 = (TextView)findViewById(R.id.ll2);
        account2 = (TextView)findViewById(R.id.ll4);
        account3 = (TextView)findViewById(R.id.ll6);
        account4 = (TextView)findViewById(R.id.ll8);
        btnpayment = (Button)findViewById(R.id.btnpay);

        Intent intent = getIntent();
        if(intent != null) {
            id = intent.getStringExtra("id");
            cost = Integer.parseInt(intent.getStringExtra("cost2"));
            date_in = intent.getStringExtra("date_in");
            date_out = intent.getStringExtra("date_out");
            covers2 = intent.getIntExtra("covers2", 0);
            location = intent.getStringExtra("location");
        }

        textpaycost.setText("결제 금액 : "+ cost +"원");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, banktype2);

        spinnerbanktype.setAdapter(adapter);

        btnpayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Object> paymentlist = new ArrayList<Object>();

                selectedbanktype = spinnerbanktype.getTransitionName();
                if((account1.getText()!=null)&&(account2.getText()!=null)&&(account2.getText()!=null)&&(account4.getText()!=null)){
                    accountall = account1.getText()+"-"+account2.getText()+"-"+account3.getText()+"-"+account4.getText();
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
                pay.setType("payment");

                paymentlist.add(0, reserve);
                paymentlist.add(1, pay);

                try {
                    o.writeObject((Object)paymentlist);//무조건 Object형으로 형변환을 해줘야 하는지??
                    o.flush();
                    o.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(payment.this, MainActivity.class);
                intent.putExtra(id,id);
                finish();
                startActivity(intent);
            }
        });

        Thread worker = new Thread() {
            public void run() {
                try {
                    // 소켓 생성 및 입출력 스트림을 소켓에 연결
                    //socket = new Socket("10.0.2.2", DEFAULT_PORT);                 // local ip
                    socket = new Socket("192.168.1.102", DEFAULT_PORT);     // remote ip
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