package com.example.calismam.Cerceve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.calismam.Adapter.KullaniciAdapter;
import com.example.calismam.Adapter.OneriAdapter;
import com.example.calismam.Model.Kullanici;
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


public class ArkOneriFragment extends Fragment {



    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private OneriAdapter oneriAdapter;

    private FirebaseUser firebaseUser;


    private List<Kullanici> mKullanici;


    String profilId;






    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_ark_oneri, container, false);





        recyclerView = view.findViewById(R.id.recyler_view_ArkOneriFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        toolbar = view.findViewById(R.id.toolbar_ArkOneriFragment);
        toolbar.setTitle("Arkadaş Öneri");
        getActivity().setTitle("KAFA DENGİ");
        mKullanici = new ArrayList<>();

        oneriAdapter = new OneriAdapter(getContext(),mKullanici);

        recyclerView.setAdapter(oneriAdapter);


        kullanicilariOku();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profilId = prefs.getString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());





        return view;


    }

    private void kullanicilariOku() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mKullanici.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);

                    mKullanici.add(kullanici);


                }
                oneriAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}