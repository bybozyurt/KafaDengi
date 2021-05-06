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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {

    EditText kEmail, kSifre, kSifre2, AdSoy, kAdi;
    TextView textKayitliyim;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        setTitle("KAFA DENGİ");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();



        AdSoy = findViewById(R.id.AdSoy);
        kAdi = findViewById(R.id.kAdi);
        kEmail = findViewById(R.id.kEmail);
        kSifre = findViewById(R.id.kSifre);
        kSifre2 = findViewById(R.id.kSifre2);
        textKayitliyim = findViewById(R.id.textKayitliyim);

        textKayitliyim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitActivity.this, GirisActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    public void kayit(View view){

        String ad_soyad = AdSoy.getText().toString();
        String kullanici_adi = kAdi.getText().toString();
        String email = kEmail.getText().toString();
        String password = kSifre.getText().toString();
        String password2 = kSifre2.getText().toString();

        if(email.equals("") || password.equals("") || password2.equals("") || kullanici_adi.equals("") || ad_soyad.equals("")){
            Toast.makeText(this, "Lütfen gerekli yerleri doldurunuz", Toast.LENGTH_SHORT).show();
        }
        else {
            if(password.equals(password2)){
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    String KullaniciID = firebaseUser.getUid();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(KullaniciID);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("id",KullaniciID);
                                    hashMap.put("kullaniciAdi",kullanici_adi.toLowerCase());
                                    hashMap.put("email",email);
                                    hashMap.put("AdSoy",ad_soyad);
                                    hashMap.put("bio","");
                                    hashMap.put("password",password);
                                    hashMap.put("resimurl","https://firebasestorage.googleapis.com/v0/b/kafadengi-e1d4f.appspot.com/o/placeperson.png?alt=media&token=ed756080-5f6b-4d52-a49b-b8f9578b95bc");

                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(KayitActivity.this, "Hesabınız oluşturuldu. E-mailinize doğrulama kodu gönderildi.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(KayitActivity.this, GirisActivity.class);
                                                startActivity(intent);
                                                finish();
                                                firebaseAuth.signOut();

                                            }
                                        }
                                    });





                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(KayitActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });


            }
            else {
                Toast.makeText(this, "Şifreleriniz uyuşmuyor.", Toast.LENGTH_SHORT).show();
            }
        }

    }


}