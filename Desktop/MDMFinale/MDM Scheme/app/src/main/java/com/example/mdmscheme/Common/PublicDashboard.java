package com.example.mdmscheme.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.mdmscheme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class PublicDashboard extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    private TextView std;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_dashboard);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Alert Notification")
                .setContentText("Something is wrong! please check Notice")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        std = findViewById(R.id.student_count);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("live-feed");
        ValueEventListener eventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            String Studcount;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  PeopleCount people = snapshot.getValue(PeopleCount.class);
                  people.setKey(snapshot.getKey());
                 // Studcount = keyid.child("")
            }
           // std.setText()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button button = (Button) findViewById(R.id.button_p);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();

                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(currentDateString);
    }
}