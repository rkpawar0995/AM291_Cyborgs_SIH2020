package com.example.mdmscheme.Common;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mdmscheme.R;

public class Info extends AppCompatActivity {

    ImageView photo;
    TextView dec,title;

    String aname,adec,aphoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info
        );

        aname = getIntent().getExtras().getString("name");
        adec = getIntent().getExtras().getString("dec");
        aphoto = getIntent().getExtras().getString("photo");


        photo=findViewById(R.id.photo);
        title=findViewById(R.id.title);
        dec=findViewById(R.id.dec);


        Glide.with(this)
                .load(aphoto)
                .into(photo);


        title.setText(aname);
        dec.setText(adec);



    }
}
