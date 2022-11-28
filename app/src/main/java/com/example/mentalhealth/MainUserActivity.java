package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainUserActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference, reference2;
    private String userID;
    private ImageButton moreBtn;
    private TextView textView, fullname;
    private ProgressDialog progressDialog;
    private CardView doctors, journal, myAppointments,chatSpace, bookAppointments, myDiagnosis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        doctors = findViewById(R.id.Doctors);
        doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, DoctorsActivity.class);
                startActivity(intent);
            }
        });
        journal = findViewById(R.id.Journal);
        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, JournalActivity.class);
                startActivity(intent);
            }
        });

        myAppointments = findViewById(R.id.MyAppointments);
        myAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, MyAppointmentsActivity.class);
                startActivity(intent);
            }
        });
        chatSpace = findViewById(R.id.ChatSpace);
        chatSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, ChatSpaceActivity.class);
                startActivity(intent);

            }
        });
        bookAppointments = findViewById(R.id.BookAppointments);
        bookAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, BookAppointmentsActivity.class);
                startActivity(intent);
            }
        });
        myDiagnosis = findViewById(R.id.MyDiagnosis);
        myDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUserActivity.this, MyDiagnosisActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog( this );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );

        final TextView greetingTextView = (TextView) findViewById(R.id.greetings);
        final TextView full_nameTextview = (TextView) findViewById(R.id.names);

        reference.orderByChild("uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren ()) {
                    String name = "" + ds.child("name").getValue();

                    Calendar c = Calendar.getInstance();
                    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                    String fullname = name;

                    if(timeOfDay >= 0 && timeOfDay < 12){
                        greetingTextView.setText("Good Morning ");
                        full_nameTextview.setText(name+"!");
                    }else if(timeOfDay >= 12 && timeOfDay < 16){
                        greetingTextView.setText("Good Afternoon ");
                        full_nameTextview.setText(name+"!");
                    }else if(timeOfDay >= 16 && timeOfDay < 21){
                        greetingTextView.setText("Good Evening " );
                        full_nameTextview.setText(name+"!");
                    }else if(timeOfDay >= 21 && timeOfDay < 24){
                        greetingTextView.setText("Good Night ");
                        full_nameTextview.setText(name+"!");
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}