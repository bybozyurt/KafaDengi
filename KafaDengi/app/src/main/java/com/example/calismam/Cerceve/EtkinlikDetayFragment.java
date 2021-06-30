package com.example.calismam.Cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calismam.Adapter.EtkinlikAdapter;
import com.example.calismam.Model.Etkinlik;
import com.example.calismam.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EtkinlikDetayFragment extends Fragment {


    String etkinlikId;
    private RecyclerView recyclerView;
    private EtkinlikAdapter etkinlikAdapter;
    private List<Etkinlik> etkinlikList;

    public TextView min, max, adres, detay, ucret;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etkinlik_detay, container, false);

        min =view.findViewById(R.id.txt_min);
        max =view.findViewById(R.id.txt_max);
        adres =view.findViewById(R.id.txt_adres);
        detay =view.findViewById(R.id.txt_detay);
        ucret =view.findViewById(R.id.txt_ucret);



        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        etkinlikId = preferences.getString("etkinlikId","bos");

        recyclerView = view.findViewById(R.id.EtdetayRecyler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        etkinlikList = new ArrayList<>();
        etkinlikAdapter = new EtkinlikAdapter(getContext(), etkinlikList,true);
        recyclerView.setAdapter(etkinlikAdapter);

        etkinlikOku();

        etkinlikAyrıntı();




        return view;
    }

    private void etkinlikAyrıntı() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Etkinlikler")
                .child(etkinlikId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Etkinlik etkinlik = snapshot.getValue(Etkinlik.class);


                min.setText("Minimum katılımcı: "+ etkinlik.getEtkinlik_min());
                max.setText("Maximum katılımcı: "+ etkinlik.getEtkinlik_max());
                adres.setText("Açık Adres: "+ etkinlik.getEtkinlik_adres());
                ucret.setText("Ücret: "+ etkinlik.getEtkinlik_ucret());
                detay.setText("Detaylar: "+ etkinlik.getEtkinlik_detay());



                /*String adres = etkinlik.getEtkinlik_adres();
                String min = hashMap.get("etkinlik_min").toString();
                String max = hashMap.get("etkinlik_max").toString();
                String ilce= hashMap.get("etkinlik_ilce").toString();
                String ucret = hashMap.get("etkinlik_ucret").toString();
                String detay = hashMap.get("etkinlik_detay").toString();
                etkinlikList.add(min,max,ilce,adres,detay,ucret);*/













            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void etkinlikOku() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Etkinlikler")
                .child(etkinlikId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etkinlikList.clear();
                Etkinlik etkinlik = snapshot.getValue(Etkinlik.class);
                etkinlikList.add(etkinlik);

                etkinlikAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}