package com.example.calismam.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.calismam.Adapter.GonderiAdapter;
import com.example.calismam.Model.Gonderi;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GonderiAdapter gonderiAdapter;
    private List<Gonderi> gonderiListeleri;

    private List<String> takipListesi;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView= view.findViewById(R.id.recycler_view_HomeFragment);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        gonderiListeleri = new ArrayList<>();

        gonderiAdapter = new GonderiAdapter(getContext(), gonderiListeleri);

        recyclerView.setAdapter(gonderiAdapter);

        takipKontrolu();





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
                gonderileriOku();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gonderileriOku(){

        DatabaseReference gonderiYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

        gonderiYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                gonderiListeleri.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){ //veritabanındaki verilerin cocuklarını al

                    Gonderi gonderi = dataSnapshot.getValue(Gonderi.class); // verileri gonderi sınıfına aktardık
                    for(String id : takipListesi){

                        if(gonderi.getGonderen().equals(id)){  //Gonderenin id si takiplistesindekilerin id sine eşitse*
                            gonderiListeleri.add(gonderi);

                        }
                    }
                }

                gonderiAdapter.notifyDataSetChanged(); // değişiklikleri anında güncelleyecek


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}