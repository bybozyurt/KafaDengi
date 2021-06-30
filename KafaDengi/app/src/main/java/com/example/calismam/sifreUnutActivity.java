package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class sifreUnutActivity extends AppCompatActivity {
    EditText unutEmail;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_unut);

        setTitle("KAFADENGİ");

        firebaseAuth = FirebaseAuth.getInstance();
        unutEmail = findViewById(R.id.unutEmail);

    }

    public void sifremiUnuttum(View view){
        String email = unutEmail.getText().toString();

        if (email.equals("")){
            Toast.makeText(this, "E-mail adresinizi giriniz", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(sifreUnutActivity.this, "E-mailini kontrol et", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(sifreUnutActivity.this, GirisActivity.class);
                        startActivity(intent);
                        
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(sifreUnutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}