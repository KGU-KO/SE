package com.first.miso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class withdraw_yes_click extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_yes_click);

        // login버튼 누르면 login 화면으로 돌아가게 하는 이벤트!
        Button withdraw_button = (Button) findViewById(R.id.withdraw_button);
        withdraw_button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(withdraw_yes_click.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
