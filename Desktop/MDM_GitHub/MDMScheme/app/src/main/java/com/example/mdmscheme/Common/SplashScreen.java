package com.example.mdmscheme.Common;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.mdmscheme.R;

public class SplashScreen extends Activity {
  private static int SPLASH_TIMER = 5000;
    private TextView tv;
    private ImageView Img;
    private static int  SplashTimeOut=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        tv = (TextView) findViewById(R.id.tv1);
        Img = (ImageView) findViewById(R.id.logoimg);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(anim);
        Img.startAnimation(anim);

        boolean b = new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Login.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(Img,"logo_image");
                pairs[1] = new Pair<View,String>(tv,"logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                startActivity(i,options.toBundle());
            }
        },SplashTimeOut);

    }
}
