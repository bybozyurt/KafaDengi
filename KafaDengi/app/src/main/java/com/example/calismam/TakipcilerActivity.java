package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.calismam.Adapter.KullaniciAdapter;
import com.example.calismam.Model.Kullanici;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TakipcilerActivity extends AppCompatActivity {

    String id;
    String baslik;

    List<String> idList;

    RecyclerView recyclerView;
    KullaniciAdapter kullaniciAdapter;
    List<Kullanici> kullaniciList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takipciler);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        baslik = intent.getStringExtra("baslik");



        Toolbar toolbar = findViewById(R.id.toolbar_TakipcilerActivity);
        toolbar.setTitle(baslik);
        setTitle("KAFADENGİ");


        recyclerView = findViewById(R.id.recyler_view_takipcilerActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kullaniciList = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(this,kullaniciList,false);
        recyclerView.setAdapter(kullaniciAdapter);


        idList = new ArrayList<>();

        switch (baslik){
            case "Katilmak İsteyenler":
                getKatilanlar();
                break;
            case "TakipEdilenler":
                getTakipEdilenler();
                break;
            case "Takipciler":
                getTakipciler();
                break;
        }


    }

    private void getKatilanlar() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("KatilmakIsteyenler")
                .child(id);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    idList.add(dataSnapshot.getKey());
                }
                KullanicilariGoster();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTakipEdilenler() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Takip")
                .child(id)
                .child("TakipEdilenler");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    idList.add(dataSnapshot.getKey());
                }
                KullanicilariGoster();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void getTakipciler() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Takip")
                .child(id)
                .child("Takipciler");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    idList.add(dataSnapshot.getKey());
                }
                KullanicilariGoster();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void KullanicilariGoster(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kullaniciList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                    for (String id : idList){
                        if (kullanici.getId().equals(id)) {
                            kullaniciList.add(kullanici);
                        }
                    }
                }
                kullaniciAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



}