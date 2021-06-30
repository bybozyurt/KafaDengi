package com.example.calismam.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.calismam.Adapter.EtkinlikAdapter;
import com.example.calismam.Model.Etkinlik;
import com.example.calismam.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EtkinlikAraFragment extends Fragment {

    private RecyclerView recyclerView;


    private EtkinlikAdapter etkinlikAdapter;
    private List<Etkinlik> etkinlikList;
    private EditText edt_ara;
    private Button button;




    private final String[] secenekler = {"Sehir","ilce","Tür","Tümü"};
    private Spinner sp_secenekler;

    private ArrayAdapter<String> dataAdapterSecenekler;

    String anahtar_kelimemiz = "yerIsmi";






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etkinlik_ara, container, false);

        edt_ara = view.findViewById(R.id.edt_ara_EtkinlikAraFragment);

        recyclerView = view.findViewById(R.id.recycler_view_ara_EtkinlikAraFragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        etkinlikList = new ArrayList<>();
        etkinlikAdapter = new EtkinlikAdapter(getContext(), etkinlikList,true);

        recyclerView.setAdapter(etkinlikAdapter);

        button = view.findViewById(R.id.btn_ara_etkinlik);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anahtar_kelimemiz == "Sehir") {
                    String s = edt_ara.getText().toString();
                    etkinlikleriAra(s);
                }
                else if(anahtar_kelimemiz == "ilce"){
                    String s = edt_ara.getText().toString();
                    ilceAl(s);
                }
                else if(anahtar_kelimemiz == "tur"){
                    String s = edt_ara.getText().toString();
                    etkinlikTur(s);
                }
                else if(anahtar_kelimemiz == "tümü"){
                    String s = edt_ara.getText().toString();
                    etkinlikleriOku();
                }
            }
        });




        sp_secenekler = (Spinner) view.findViewById(R.id.sp_ara_neye_gore_EtkinlikAraFragment);
        dataAdapterSecenekler = new ArrayAdapter<String >(getActivity(), android.R.layout.simple_spinner_item,secenekler);

        dataAdapterSecenekler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_secenekler.setAdapter(dataAdapterSecenekler);


        etkinlikleriOku();



        sp_secenekler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString().equals(secenekler[0])){
                    etkinlikList.clear();
                    anahtar_kelimemiz = "Sehir";
                }
                else if(parent.getSelectedItem().toString().equals(secenekler[1])){
                    etkinlikList.clear();
                    anahtar_kelimemiz = "ilce";
                }
                else if(parent.getSelectedItem().toString().equals(secenekler[2])){
                    etkinlikList.clear();
                    anahtar_kelimemiz = "tur";
                }
                else if(parent.getSelectedItem().toString().equals(secenekler[3])){
                    etkinlikList.clear();
                    anahtar_kelimemiz = "tümü";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });











        return view;
    }


    private void etkinlikleriOku(){

        DatabaseReference etkinlikYolu = FirebaseDatabase.getInstance().getReference("Etkinlikler");
        etkinlikYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(edt_ara.getText().toString().equals("")){
                    etkinlikList.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Etkinlik etkinlik = dataSnapshot.getValue(Etkinlik.class);
                        etkinlikList.add(etkinlik);

                    }
                    etkinlikAdapter.notifyDataSetChanged();

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //sehir
    private void etkinlikleriAra(String s){


        Query query = FirebaseDatabase.getInstance().getReference("Etkinlikler/")
                .orderByChild("sehir")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                etkinlikList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Etkinlik etkinlik = dataSnapshot.getValue(Etkinlik.class);
                    etkinlikList.add(etkinlik);

                }

                etkinlikAdapter.notifyDataSetChanged();
                //etkinlikAdapter = new EtkinlikAdapter(getContext(),etkinlikList);
                //recyclerView.setAdapter(etkinlikAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "hata", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void ilceAl(String s){


        Query query = FirebaseDatabase.getInstance().getReference("Etkinlikler")
                .orderByChild("etkinlik_ilce")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                etkinlikList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Etkinlik etkinlik = dataSnapshot.getValue(Etkinlik.class);
                    etkinlikList.add(etkinlik);



                }
                //etkinlikAdapter.notifyDataSetChanged();
                etkinlikAdapter = new EtkinlikAdapter(getContext(), etkinlikList,true);
                recyclerView.setAdapter(etkinlikAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void etkinlikTur(String s){

        Query query = FirebaseDatabase.getInstance().getReference("Etkinlikler")
                .orderByChild("etkinlik_baslik")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                etkinlikList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Etkinlik etkinlik = dataSnapshot.getValue(Etkinlik.class);
                    etkinlikList.add(etkinlik);



                }
                //etkinlikAdapter.notifyDataSetChanged();
                etkinlikAdapter = new EtkinlikAdapter(getContext(), etkinlikList,true);
                recyclerView.setAdapter(etkinlikAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}