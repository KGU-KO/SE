package com.first.miso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Choose_Skiplace extends AppCompatActivity {
    String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_skiplace);

        Intent i = getIntent();
        if(i != null) {
            id = i.getStringExtra("id");
        }

        //********************************************
        //A 스키장 버튼눌렀을때
        Button skibutton1 = (Button) findViewById(R.id.skibutton1);
        skibutton1.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(Choose_Skiplace.this, Choose_Date.class);
                intent.putExtra("id", id);
                intent.putExtra("location", "곤지암");
                startActivity(intent);
            }
        });
        //********************************************
        //B 스키장 버튼눌렀을때
        Button skibutton2 = (Button) findViewById(R.id.skibutton2);
        skibutton2.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(Choose_Skiplace.this, Choose_Date.class);
                intent.putExtra("id", id);
                intent.putExtra("location", "알펜시아");
                startActivity(intent);
            }
        });
        //********************************************
        //C 스키장 버튼눌렀을때
        Button skibutton3 = (Button) findViewById(R.id.skibutton3);
        skibutton3.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(Choose_Skiplace.this, Choose_Date.class);
                intent.putExtra("id", id);
                intent.putExtra("location", "덕유산");
                startActivity(intent);
            }
        });

        // ********************************************
        // D 스키장 버튼눌렀을때
        Button skibutton4 = (Button) findViewById(R.id.skibutton4);
        skibutton4.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent intent = new Intent(Choose_Skiplace.this,Choose_Date.class);
                intent.putExtra("id", id);
                intent.putExtra("location", "비발디");
                startActivity(intent);
            }
        });

        // ********************************************
        // Undo  버튼눌렀을때

        Button skiplace_button = (Button) findViewById(R.id.skiplace_undo);
        skiplace_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Choose_Skiplace.this, MainActivity.class); //이 빨간 부분에 결제화면 액티비티 이름 넣어주기!
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });



    }
}
