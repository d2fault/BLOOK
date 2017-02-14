package com.example.sunbr.blookfront;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton findFromG = (ImageButton) findViewById(R.id.findFromG);
        findFromG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        ImageButton takeP = (ImageButton) findViewById(R.id.takeP);
        takeP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });



    }
}
