package com.example.mdmscheme.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mdmscheme.R;

public class MerakiCamera extends AppCompatActivity {
    private Button realtime;
    private  Button ss;
    private Button histor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meraki_camera);

        realtime = findViewById(R.id.real_time_btn);
        ss = findViewById(R.id.ss_btn);
        histor = findViewById(R.id.historical_btn);

        realtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent re = new Intent(MerakiCamera.this, RealTime_Data.class);
                startActivity(re);
            }
        });
        ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ss = new Intent(MerakiCamera.this, SnapShort_Data.class);
                startActivity(ss);
            }
        });
        histor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hd = new Intent(MerakiCamera.this, HisrtoricalData.class);
                startActivity(hd);
            }
        });

    }
}