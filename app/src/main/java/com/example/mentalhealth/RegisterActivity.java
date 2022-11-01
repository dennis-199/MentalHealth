package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton backBtn, gpsBtn;
    private ImageView profileIv;
    private EditText nameEt, phoneEt, countryEt, stateEt, cityEt, addressEt, emailEt, passwordEt, cPasswordEt;
    private Button registerBtn;
    private TextView registerSellerTv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init UI views
        backBtn = findViewById(R.id.backBtn);

        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        cPasswordEt = findViewById(R.id.cPasswordEt);
        registerBtn = findViewById(R.id.registerBtn);
        registerSellerTv = findViewById(R.id.registerSellerTv);

        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog( this );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register user
                inputData ();
            }
        });
        registerSellerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open register seller activity
                startActivity(new Intent(RegisterActivity.this, RegisterDoctorActivity.class ));

            }
        });
    }

    private String fullName,phoneNumber, country, state, city, address, email, password, confirmPassword;
    private void inputData() {

        //input data
        fullName = nameEt.getText ( ).toString ( ).trim ( );
        phoneNumber = phoneEt.getText ( ).toString ( ).trim ( );

        email = emailEt.getText ( ).toString ( ).trim ( );
        password = passwordEt.getText ( ).toString ( ).trim ( );
        confirmPassword = cPasswordEt.getText ( ).toString ( ).trim ( );

        //validate data

        if (TextUtils.isEmpty ( fullName )) {
            Toast.makeText ( this , "Enter name..." , Toast.LENGTH_SHORT ).show ( );
        }
        if (TextUtils.isEmpty ( phoneNumber )) {
            Toast.makeText ( this , "Enter phone number..." , Toast.LENGTH_SHORT ).show ( );
        }


        if (!Patterns.EMAIL_ADDRESS.matcher ( email ).matches ( )) {
            Toast.makeText ( this , "Invalid email pattern..." , Toast.LENGTH_SHORT ).show ( );
        }
        if (password.length ( ) < 6) {
            Toast.makeText ( this , "Password should be at least 6 characters long..." , Toast.LENGTH_SHORT ).show ( );
        }
        if (!password.equals ( confirmPassword )) {
            Toast.makeText ( this , "Password doesn't match..." , Toast.LENGTH_SHORT ).show ( );
        }

        createAccount ( );
    }

    private void createAccount() {
        progressDialog.setMessage ( "Creating account..." );
        progressDialog.show ( );

        //create account
        firebaseAuth.createUserWithEmailAndPassword ( email , password ).addOnSuccessListener ( new OnSuccessListener<AuthResult>( ) {
            @Override
            public void onSuccess(AuthResult authResult) {
                //account created
                saveFirebaseData ( );
            }
        } ).addOnFailureListener ( new OnFailureListener( ) {
            @Override
            public void onFailure(@NonNull Exception e) {
                // failed creating account
                progressDialog.dismiss ( );
                Toast.makeText ( RegisterActivity.this , "" + e.getMessage ( ) , Toast.LENGTH_SHORT ).show ( );
            }
        } );
    }

    private void saveFirebaseData() {
        progressDialog.setMessage ( "Saving Account info..." );

        final String timestamp = "" + System.currentTimeMillis ( );

        //setup data to save
        HashMap< String, Object > hashMap = new HashMap <> ( );
        hashMap.put ( "uid" , "" + firebaseAuth.getUid ( ) );
        hashMap.put ( "email" , "" + email );
        hashMap.put ( "name" , "" + fullName );
        hashMap.put ( "phone" , "" + phoneNumber );

        hashMap.put ( "timestamp" , "" + timestamp );
        hashMap.put ( "accountType" , "User" );
        hashMap.put ( "online" , "true" );

        // save to db

        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child( firebaseAuth.getUid ( ) ).setValue ( hashMap ).addOnSuccessListener ( new OnSuccessListener < Void > ( ) {
                    @Override
                    public void onSuccess(Void unused) {
                        // db updated
                        progressDialog.dismiss ();
                        startActivity ( new Intent ( RegisterActivity.this, MainActivity.class ) );
                        finish();
                    }
                } )
                .addOnFailureListener ( new OnFailureListener ( ) {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // failed updating db
                        progressDialog.dismiss ();
                        startActivity ( new Intent ( RegisterActivity.this, MainActivity.class ) );
                        finish();
                    }
                } );
    }
}