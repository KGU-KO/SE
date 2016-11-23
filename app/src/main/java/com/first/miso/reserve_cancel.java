package com.first.miso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class reserve_cancel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_cancel);


        // '예약이 취소되었습니다' 문장 출력후 메인버튼누르면 메인으로 돌아가는 이벤트
        Button  go_main_button = (Button) findViewById(R.id. go_main_button);
        go_main_button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(reserve_cancel.this, MainActivity.class);

                startActivity(intent);
            }
        });


    }
}
