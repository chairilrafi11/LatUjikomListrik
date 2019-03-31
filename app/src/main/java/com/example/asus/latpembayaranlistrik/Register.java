package com.example.asus.latpembayaranlistrik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //Deklarasi Widget
    TextView TextToLogin;
    EditText EditTextEmail, EditTextPassword1, EditTextPassword2;
    private Button BtnRegister;
    private ProgressDialog progressDialog;
    private Spinner spPilih;
    private long backPressedTime;

    //deklarasi FIrebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private FirebaseAuth firebaseAuth;

    //defining firebaseauth object
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //intializing Widget
        TextToLogin = (TextView) findViewById(R.id.TextToLogin);
        EditTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        EditTextPassword1 = (EditText) findViewById(R.id.EditTextPassword1);
        EditTextPassword2 = (EditText) findViewById(R.id.EditTextPassword2);
        BtnRegister = (Button) findViewById(R.id.BtnRegister);

        progressDialog = new ProgressDialog(this);

        TextToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Register.this, Login.class);
                startActivity(mIntent);
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting email and password from edit texts
                String email = EditTextEmail.getText().toString().trim();
                String password = EditTextPassword1.getText().toString().trim();
                String repassword = EditTextPassword2.getText().toString().trim();

                //checking if email and passwords are empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password) && TextUtils.isEmpty(repassword)) {
                    Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.equals(repassword)) {
                } else {
                    Toast.makeText(Register.this, "Password Tidak Sesuai", Toast.LENGTH_LONG).show();
                    return;
                }

                //if the email and password are not empty
                //displaying a progress dialog

                progressDialog.setMessage("Tunggu Sebentar...");
                progressDialog.show();

                //creating a new user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    userID = user.getUid();
                                    //display some message here
                                    Intent mIntent = new Intent(Register.this, Lengkapidata_user.class);
                                    startActivity(mIntent);
                                    Toast.makeText(Register.this, "Successfully registered ", Toast.LENGTH_LONG).show();
                                } else {
                                    //display some message here
                                    Toast.makeText(Register.this, "Registration Error", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Registration Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Register.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
