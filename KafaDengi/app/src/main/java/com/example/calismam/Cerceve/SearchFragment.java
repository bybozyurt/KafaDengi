package com.example.calismam.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.calismam.Adapter.KullaniciAdapter;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;

    EditText search_bar;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);



        recyclerView = view.findViewById(R.id.recycler_view_Search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        search_bar = view.findViewById(R.id.edt_search_bar);

        mKullaniciler = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(getContext(),mKullaniciler,true);

        recyclerView.setAdapter(kullaniciAdapter);

        kullanicileriOku();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            //girilen metin de??i??tikce ona g??re arama yapacak
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                kullaniciAra(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void kullaniciAra(String s){
        //Sorguya g??re ??a????r??yor verileri
        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullan??c??lar").orderByChild("kullaniciAdi")
                .startAt(s)
                .endAt(s+"\uf8ff"); //buna g??re arama yapacak

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mKullaniciler.clear();
                //veritaban??ndan verileri al??p datasnapshota ekledik kullan??c??ya aktard??k datasnapshot ?? kullan??c??y??da listeye aktard??k
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                }
                //veriler her g??ncellendi??inde liste g??ncellencek
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void kullanicileriOku(){
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullan??c??lar");

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Arama bar?? bo?? ise b??t??n hepsini g??sterecek
                if(search_bar.getText().toString().equals("")){
                    mKullaniciler.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                        mKullaniciler.add(kullanici);
                    }
                    kullaniciAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}