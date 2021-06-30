package com.example.calismam.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calismam.Adapter.BildirimAdapter;
import com.example.calismam.Model.Bildirim;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BildirimAdapter bildirimAdapter;
    private List<Bildirim> bildirimList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.not_recylerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        bildirimList = new ArrayList<>();
        bildirimAdapter = new BildirimAdapter(getContext(),bildirimList);
        recyclerView.setAdapter(bildirimAdapter);

        BildirimleriOku();
        getActivity().setTitle("KAFA DENGİ");



        return view;


    }

    private void BildirimleriOku() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bildirimList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Bildirim bildirim = dataSnapshot.getValue(Bildirim.class);
                    bildirimList.add(bildirim);
                }

                Collections.reverse(bildirimList);
                bildirimAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}