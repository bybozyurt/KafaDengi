package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.example.calismam.Adapter.YorumAdapter;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.Model.Yorum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YorumlarActivity extends AppCompatActivity {


    Toolbar toolbar;
    private RecyclerView recyclerView;
    private YorumAdapter yorumAdapter;
    private List<Yorum> yorumListesi;

    EditText edt_yorum_ekle;
    ImageView profil_resmi;
    TextView txt_gonder;

    String etkinlikId;
    String gonderenId;

    FirebaseUser mevcutKullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar);


        Toolbar toolbar = findViewById(R.id.toolbar_yorumlarActivity);
        toolbar.setTitle("Yorumlar");
        setTitle("KAFADENGİ");

        /*
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_yorumlarActivity);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Yorumlar");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });*/

        Intent intent = getIntent();

        etkinlikId = intent.getStringExtra("etkinlikId");
        gonderenId = intent.getStringExtra("gonderenId");


        recyclerView = findViewById(R.id.recyler_view_yorumlarActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        yorumListesi = new ArrayList<>();
        yorumAdapter = new YorumAdapter(this,yorumListesi, etkinlikId);
        recyclerView.setAdapter(yorumAdapter);


        edt_yorum_ekle = findViewById(R.id.edt_yorum_yorumlarActivity);
        profil_resmi = findViewById(R.id.profil_resmi_yorumlarActivity);
        txt_gonder = findViewById(R.id.txt_gonder_yorumlarActivity);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();



        txt_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_yorum_ekle.getText().toString().equals("")){
                    Toast.makeText(YorumlarActivity.this, "Boş yorum gönderemezsiniz.", Toast.LENGTH_SHORT).show();
                }
                else {
                    yorumEkle();
                }
            }
        });


        resimAl();
        yorumlariOku();



    }

    private void yorumEkle() {

        DatabaseReference yorumlarYolu = FirebaseDatabase.getInstance().getReference("Yorumlar")
                .child(etkinlikId);

        String yorumid = yorumlarYolu.push().getKey();


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("yorum",edt_yorum_ekle.getText().toString());
        hashMap.put("gonderen",mevcutKullanici.getUid());
        hashMap.put("yorumid",yorumid);


        yorumlarYolu.child(yorumid).setValue(hashMap);
        eklenenBildirimler();
        edt_yorum_ekle.setText("");
    }
//şüpheli edt*
    private void eklenenBildirimler(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(gonderenId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciId",mevcutKullanici.getUid());
        hashMap.put("text","yorum: "+edt_yorum_ekle.getText().toString());
        hashMap.put("etkinlikId", etkinlikId);
        hashMap.put("isGonderi",true);

        databaseReference.push().setValue(hashMap);



    }

    private void resimAl(){

        DatabaseReference resimAlmaYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                .child(mevcutKullanici.getUid());

        resimAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(profil_resmi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void yorumlariOku(){

        DatabaseReference yorumlariOkumaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar")
                .child(etkinlikId);

        yorumlariOkumaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yorumListesi.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Yorum yorum = dataSnapshot.getValue(Yorum.class);
                    yorumListesi.add(yorum);
                }

                yorumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}