package com.example.mdmscheme.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mdmscheme.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Profile extends AppCompatActivity {
    private TextView nameTxt , schoolTxt, schoolIdTxt;
    private TextView emailTxt;
    private final String TAG = this.getClass().getName().toUpperCase();
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private Map<String, String> userMap;
    private String email;
    private String USERID;
    private static final String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = mDatabase.child(USERS);
        Log.v("USERID", userRef.getKey());

        nameTxt = (TextView)findViewById(R.id.name_profile);
        schoolTxt = (TextView) findViewById(R.id.school_name_profile);
        schoolIdTxt = (TextView)findViewById(R.id.school_id_profile);
        emailTxt = (TextView)findViewById(R.id.emailPro);

        ValueEventListener eventListener = userRef.addValueEventListener(new ValueEventListener() {
            String fname,mail,schlid,schName;
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for (DataSnapshot keyid : dataSnapshot.getChildren()) {
                    if (keyid.child("email").getValue().equals(email)) {
                        fname = keyid.child("name").getValue().toString();
                        mail = keyid.child("email").getValue().toString();
                        schlid = keyid.child("schoolid").getValue().toString();
                        schName = keyid.child("schoolname").getValue().toString();
                    }
                }

                nameTxt.setText(fname);
                emailTxt.setText(mail);
                schoolIdTxt.setText(schlid);
                schoolTxt.setText(schName);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

    }
}