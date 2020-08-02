package com.example.mdmscheme.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdmscheme.Forget_Password;
import com.example.mdmscheme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button reg;
    private  Button log, forget;
    private Button skiplogin;
    private TextInputEditText emailTextView;
    private TextInputEditText passwordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        log = findViewById(R.id.lgbtn);
        reg = findViewById(R.id.regbtn);
        forget = findViewById(R.id.forgetlgn);
        skiplogin = findViewById(R.id.skiplogin_btn);

        emailTextView = findViewById(R.id.userText);
        passwordTextView = findViewById(R.id.password);



        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent re = new Intent(Login.this, Register1.class);
                startActivity(re);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fr = new Intent(Login.this, Forget_Password.class);
                startActivity(fr);
            }
        });
        skiplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skip = new Intent(Login.this, DashBoard.class);
                startActivity(skip);
            }
        });

    }

    private void loginUserAccount() {

        String email, password;
        email =  emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Login.this, DashBoard.class);
                    startActivity(intent); }
                else { Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}