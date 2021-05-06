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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;

    EditText search_bar;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

        View view = inflater.inflate(R.layout.fragment_search, container, false);



        recyclerView = view.findViewById(R.id.recycler_view_Search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        search_bar = view.findViewById(R.id.edt_search_bar);

        mKullaniciler = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(getContext(),mKullaniciler);

        recyclerView.setAdapter(kullaniciAdapter);

        kullanicileriOku();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            //girilen metin değiştikce ona göre arama yapacak
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
        //Sorguya göre çağırıyor verileri
        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").orderByChild("kullaniciAdi")
                .startAt(s)
                .endAt(s+"\uf8ff"); //buna göre arama yapacak

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mKullaniciler.clear();
                //veritabanından verileri alıp datasnapshota ekledik kullanıcıya aktardık datasnapshot ı kullanıcıyıda listeye aktardık
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                }
                //veriler her güncellendiğinde liste güncellencek
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void kullanicileriOku(){
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar");

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Arama barı boş ise bütün hepsini gösterecek
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