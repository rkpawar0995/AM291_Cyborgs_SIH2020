package com.example.mdmscheme.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mdmscheme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class Register1 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText emailTv,passwordTv,nameEditText,schoolNameTv,schoolId;
    private Button regi;
    private static final String USERS = "users";
    private String TAG = "Register1";
    private String name, schoolname, schoolid,email,password;
    private FirebaseDatabase database;
    public User user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        mAuth = FirebaseAuth.getInstance();

        emailTv = findViewById(R.id.emailText);
        passwordTv = findViewById(R.id.password);
        regi = findViewById(R.id.register_btn);
        nameEditText = findViewById(R.id.name_profile);
        schoolNameTv = findViewById(R.id.school_name);
        schoolId = findViewById(R.id.school_id_profile);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USERS);

        View reg = findViewById(R.id.login_here);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Register1.this,Login.class);
                startActivity(i);
                finish();
            }
        });

        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailTv.getText().toString() != null && passwordTv.getText().toString() != null) {
                    name = nameEditText.getText().toString();
                    email = emailTv.getText().toString();
                    password = passwordTv.getText().toString();
                    schoolid = schoolId.getText().toString();
                    schoolname = schoolNameTv.getText().toString();
                    user = new User(name, email, schoolid, schoolname);
                    RegisterUserAccount();
                }
            }
        });

    }
       public void RegisterUserAccount() {

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please enter email!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please enter password!",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                    // if the user created intent to login activity
                    Intent intent = new Intent(Register1.this, Login.class);
                    startActivity(intent);
                }
                else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();
                }
            }


        });

    }
    public void updateUI(FirebaseUser currentUser) {
        String keyid = mDatabase.push().getKey();
        mDatabase.child(keyid).setValue(user); //adding user info to database
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
    }
}
