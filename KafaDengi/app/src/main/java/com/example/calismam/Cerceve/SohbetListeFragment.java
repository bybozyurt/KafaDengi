package com.example.calismam.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calismam.Adapter.KullaniciAdapter;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.Model.Sohbet;
import com.example.calismam.Model.SohbetList;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SohbetListeFragment extends Fragment{

    private RecyclerView recyclerView;

    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullanici;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<SohbetList> kullaniciList;








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_sohbet_liste, container, false);

        recyclerView = view.findViewById(R.id.recyler_view_sohbetListeFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getActivity().setTitle("Sohbetler");





        fuser = FirebaseAuth.getInstance().getCurrentUser();

        kullaniciList = new ArrayList<>();



        reference = FirebaseDatabase.getInstance().getReference("Sohbetlistesi")
                .child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kullaniciList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SohbetList sohbetList = dataSnapshot.getValue(SohbetList.class);
                    kullaniciList.add(sohbetList);

                }

                sohbetListesi();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return view;
    }

    private void sohbetListesi() {

        mKullanici = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mKullanici.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                    for (SohbetList sohbetList : kullaniciList){
                        if(kullanici.getId().equals(sohbetList.getId())){
                            mKullanici.add(kullanici);
                        }
                    }
                }

                kullaniciAdapter = new KullaniciAdapter(getContext(),mKullanici,true);
                recyclerView.setAdapter(kullaniciAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}