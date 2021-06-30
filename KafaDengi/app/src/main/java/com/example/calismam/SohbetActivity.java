
//  ANA
package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.calismam.Adapter.SohbetAdapter;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.Model.Sohbet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SohbetActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView ad, durum;
    ImageView img_profil;
    ImageButton gonder;
    EditText edt_mesaj;

    FirebaseAuth firebaseAuth,getFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser fUser;

    //mesajın görülüp görülmediğini kontrol ediyoruz
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<Sohbet> sorbetList;
    SohbetAdapter sohbetAdapter;

    Intent intent;


    String hisUid;
    String id;
    String hisImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sohbet);

        toolbar = findViewById(R.id.toolbar_sohbetActivity);
        toolbar.setTitle("");

        recyclerView = findViewById(R.id.recyler_view_sohbetActivity);
        ad = findViewById(R.id.txt_ad_sohbetActivity);
        durum = findViewById(R.id.txt_durum_sohbetActivity);
        gonder = findViewById(R.id.img_btn_gonder_sohbetActivity);
        edt_mesaj = findViewById(R.id.edt_mesaj_sohbetActivity);
        img_profil = findViewById(R.id.img_profil_sohbetActivity);

        //recylerview için layoutmanager


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);


        //recylerview özellikleri
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        setTitle("Sohbet");





        intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        getFirebaseAuth = FirebaseAuth.getInstance();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Kullanıcılar");




        //kullanıcı bilgilerini aldık
        Query userQuery = databaseReference.orderByChild("id").equalTo(hisUid);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

                    String AdSoy = ""+ds.child("AdSoy").getValue();
                    hisImage = ""+ds.child("resimurl").getValue();

                    ad.setText(AdSoy);
                    try{
                        //toolbarın içine image eklendi
                        Picasso.get().load(hisImage).placeholder(R.drawable.placeperson).into(img_profil);

                    }
                    catch (Exception e) {
                        Picasso.get().load(R.drawable.placeperson).into(img_profil);

                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edittext deki mesajımızı aldık
                String message = edt_mesaj.getText().toString().trim();
                //mesaj boş mu değil mi kontrol edelim
                if(TextUtils.isEmpty(message)){
                    //boş ise
                    Toast.makeText(SohbetActivity.this, "Boş mesaj gönderilemez...", Toast.LENGTH_SHORT).show();


                }
                else {
                    //boş değil ise

                    mesajGonder(message);

                }
            }
        });




        Mesajgoruldu();

        MesajOku();






    }

    private void Mesajgoruldu() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Sohbetler");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Sohbet sohbet = ds.getValue(Sohbet.class);
                    if(sohbet.getAlan() != null && sohbet.getAlan().equals(id) && sohbet.getGonderen().equals(hisUid)){
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("goruldu",true);
                        ds.getRef().updateChildren(hasSeenHashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//private void MesajOku(String id, String hisUid,String resimurl) {
    private void MesajOku() {

        sorbetList = new ArrayList<>();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Sohbetler");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sorbetList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Sohbet sohbet = ds.getValue(Sohbet.class);


                    if(sohbet.getAlan().equals(id) && sohbet.getGonderen().equals(hisUid)
                    || sohbet.getAlan().equals(hisUid) && sohbet.getGonderen().equals(id)){
                        sorbetList.add(sohbet);

                    }
                    //Adapter





                    sohbetAdapter = new SohbetAdapter(SohbetActivity.this, sorbetList,hisImage);
                    sohbetAdapter.notifyDataSetChanged();

                    //recylerview için adapter ayar
                    recyclerView.setAdapter(sohbetAdapter);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void mesajGonder(String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //id = firebaseAuth.getUid();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gonderen", id);
        hashMap.put("alan",hisUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("goruldu",false);


        databaseReference.child("Sohbetler").push().setValue(hashMap);

        //Mesaj gönderildikten sonra edittextde ki mesajı sıfırlayalım
        edt_mesaj.setText("");

        //Sohbetlistefragmenta sohbetlerimizi ekliyoruz
        DatabaseReference sohbetRef = FirebaseDatabase.getInstance().getReference("Sohbetlistesi")
                .child(fUser.getUid())
                .child(hisUid);

        sohbetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    sohbetRef.child("id").setValue(hisUid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    private void kullanıcıDurumKontrol(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            id = firebaseUser.getUid(); //Giris yapmıs olan kullanıcının IDsini aldık

        }
        else {
            startActivity(new Intent(this,GirisActivity.class));
            finish();
        }


    }

    @Override
    protected void onStart() {
        kullanıcıDurumKontrol();

        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.signout){
            firebaseAuth.signOut();
            kullanıcıDurumKontrol();
        }




        return super.onOptionsItemSelected(item);


    }
}