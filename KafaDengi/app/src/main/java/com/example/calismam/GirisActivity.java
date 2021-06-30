package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GirisActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText emailText, passwordText;
    TextView textSifre, textKayit;
    DatabaseReference databaseReference;

    CheckBox checkBox;

    SharedPreferences GET;
    SharedPreferences.Editor SET;
    boolean durum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        setTitle("KAFADENGİ");

        firebaseAuth = FirebaseAuth.getInstance(); //initialize ettik.
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        textSifre = findViewById(R.id.textSifre);
        textKayit = findViewById(R.id.textKayit);
        checkBox = findViewById(R.id.chb_hatirla);


        GET = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SET = GET.edit();

        // Beni hatırla işlemi
        boolean bilgihatirla = GET.getBoolean("boolean_key", false);
        if (bilgihatirla == true) {
            checkBox.setChecked(true);

            emailText.setText(GET.getString("keyPosta", ""));
            passwordText.setText(GET.getString("keyParola", ""));
        } else {
            checkBox.setChecked(false);

            emailText.setText("");
            passwordText.setText("");
        }




        textSifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this, sifreUnutActivity.class);
                startActivity(intent);
            }
        });

        textKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this, KayitActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // İçeri giriş yapmış kullanıcıları veriyor


        if (firebaseUser != null) {
            Intent intent = new Intent(GirisActivity.this, AnaSayfaActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                durum = checkBox.isChecked();

                if (durum == true) {
                    SET.putBoolean("boolean_key", true);
                    SET.putString("keyPosta", emailText.getText().toString());
                    SET.putString("keyParola", passwordText.getText().toString());
                    SET.commit();

                } else {
                    SET.putBoolean("boolean_key", false);
                    SET.putString("keyPosta", "");
                    SET.putString("keyParola", "");
                    SET.commit();

                }

            }
        });
    }







    public void signInClicked(View view) {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        if (email.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Lütfen zorunlu yerleri doldurunuz", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GirisActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("Kullanıcılar").child(firebaseAuth.getCurrentUser().getUid());

                        
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(GirisActivity.this, AnaSayfaActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(GirisActivity.this, "Lütfen e-mail adresinizi doğrulayınız", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }



}
