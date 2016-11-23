package com.first.miso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class withdraw extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw);


        //탈퇴하기 yes버튼 눌렀을때->withdrwa_yes_click

        Button withdraw_yes_button = (Button) findViewById(R.id.withdraw_yes_button);
        withdraw_yes_button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(withdraw.this, withdraw_yes_click.class);

                startActivity(intent);
            }
        });

        //탈퇴하기 no 버튼 눌렀을때 ->메인으로돌아가게 설정!
        Button withdraw_no_button = (Button) findViewById(R.id.withdraw_no_button);
        withdraw_no_button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(withdraw.this, MainActivity.class);

                startActivity(intent);
            }
        });

    }
}
