package com.example.calismam.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.calismam.Adapter.EtkinlikAdapter;
import com.example.calismam.Model.Etkinlik;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EtkinlikAdapter etkinlikAdapter;
    private List<Etkinlik> etkinlikListeleri;

    private List<String> takipListesi;

    private ImageView img_sohbet,img_oneri,img_ara;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        img_sohbet = view.findViewById(R.id.img_sohbet_homeFragment);
        img_oneri = view.findViewById(R.id.img_arkadasOneri_homeFragment);
        img_ara = view.findViewById(R.id.etkinlikAra_homeFragment);

        recyclerView= view.findViewById(R.id.recycler_view_HomeFragment);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        etkinlikListeleri = new ArrayList<>();

        etkinlikAdapter = new EtkinlikAdapter(getContext(), etkinlikListeleri,true);

        recyclerView.setAdapter(etkinlikAdapter);

        takipKontrolu();


        img_sohbet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new SohbetListeFragment()).commit();

            }
        });

        img_oneri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new ArkOneriFragment()).commit();

            }
        });

        img_ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new EtkinlikAraFragment()).commit();

            }
        });









        return view;
    }


    // Sadece Takip ettiklerimizin gönderilerini göreceğiz
    private void takipKontrolu(){
        takipListesi = new ArrayList<>();

        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference("Takip")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("TakipEdilenler");
        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                takipListesi.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    takipListesi.add(dataSnapshot.getKey());

                }
                etkinlikleriOku();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void etkinlikleriOku(){

        DatabaseReference etkinlikYolu = FirebaseDatabase.getInstance().getReference("Etkinlikler");

        etkinlikYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                etkinlikListeleri.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){ //veritabanındaki verilerin cocuklarını al

                    Etkinlik etkinlik = dataSnapshot.getValue(Etkinlik.class); // verileri etkinlik sınıfına aktardık
                    for(String id : takipListesi){

                        if(etkinlik.getGonderen().equals(id)){  //Gonderenin id si takiplistesindekilerin id sine eşitse*
                            etkinlikListeleri.add(etkinlik);

                        }
                    }
                }

                etkinlikAdapter.notifyDataSetChanged(); // değişiklikleri anında güncelleyecek


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }








}