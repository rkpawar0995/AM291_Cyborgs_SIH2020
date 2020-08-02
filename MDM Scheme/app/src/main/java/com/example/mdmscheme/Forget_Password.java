package com.example.mdmscheme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;

public class Forget_Password extends AppCompatActivity {

    private FirebaseApp mAuth;
    private Button reset;
    private TextInputEditText EmailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);
       mAuth = FirebaseApp.getInstance();

       EmailTv = findViewById(R.id.emailText);
       reset = findViewById(R.id.forget_btn);

//       reset.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               ResetUserPass();
//           }
//       });
    }

//    private void ResetUserPass() {
//
//        String email;
//        email  = EmailTv.getText().toString();
//
//        if (TextUtils.isEmpty(email)) {
//            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(Forget_Password.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Forget_Password.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });



}