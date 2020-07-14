package com.example.mdmscheme.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.mdmscheme.R;
import com.google.android.material.navigation.NavigationView;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CardView attend;
    private CardView pub;
    private CardView his;
    private CardView notice;
    private CardView meraki;
    private CardView stock;

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        attend = (CardView) findViewById(R.id.attend);
        pub = (CardView) findViewById(R.id.pub);
        his = (CardView) findViewById(R.id.his);
        notice = (CardView) findViewById(R.id.notice);
        meraki = (CardView) findViewById(R.id.meraki);
        stock = (CardView) findViewById(R.id.stock);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent att = new Intent(DashBoard.this,Attendannce.class);
                startActivity(att);
            }
        });
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pub = new Intent(DashBoard.this,PublicDashboard.class);
                startActivity(pub);
            }
        });
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent his = new Intent(DashBoard.this,History.class);
                startActivity(his);
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noti = new Intent(DashBoard.this,Notice.class);
                startActivity(noti);
            }
        });
        meraki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent me = new Intent(DashBoard.this,MerakiCamera.class);
                startActivity(me);
            }
        });
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stock = new Intent(DashBoard.this,FoodStock.class);
                startActivity(stock);
            }
        });






        //--------HOOKS----------
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //--------TOOL BAR----------
        setSupportActionBar(toolbar);

        //--------Navigation Menu----------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(DashBoard.this);
        navigationView.setCheckedItem(R.id.nav_home);

    }

    public void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_school:
               startActivity( new Intent(DashBoard.this,School.class));
               break;
            case R.id.nav_setting:
                Intent intent1 = new Intent(DashBoard.this, Setting.class);
                startActivity(intent1);
                break;
            case R.id.nav_help:
                Intent intent2 = new Intent(DashBoard.this, Help.class);
                startActivity(intent2);
                break;
            case R.id.nav_about:
                Intent intent3 = new Intent(DashBoard.this, About.class);
                startActivity(intent3);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(DashBoard.this, Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                Intent intent5 = new Intent(DashBoard.this, Login.class);
                startActivity(intent5);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
