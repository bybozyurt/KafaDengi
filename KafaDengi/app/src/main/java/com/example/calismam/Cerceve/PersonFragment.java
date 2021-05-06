package com.example.calismam.Cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calismam.GirisActivity;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PersonFragment extends Fragment {

    ImageView resimSecenekler, profil_resmi;

    TextView txt_gonderiler, txt_takipciler, txt_takipEdilenler, txt_Ad, txt_bio, txt_kullaniciAdi;
    Button btn_profili_Duzenle;
    

    FirebaseUser firebaseUser ;
    String profilId;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public PersonFragment() {
        // Required empty public constructor
    }



    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);


        profilId = prefs.getString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());


        resimSecenekler = view.findViewById(R.id.resimSecenekler_ProfilCercevesi);
        profil_resmi = view.findViewById(R.id.profil_resmi_profilCercevesi);

        txt_gonderiler = view.findViewById(R.id.txt_gonderiler_profilCercevesi);
        txt_takipciler = view.findViewById(R.id.txt_takipciler_profilCercevesi);
        txt_takipEdilenler = view.findViewById(R.id.txt_takipEdilenler_profilCercevesi);
        txt_bio = view.findViewById(R.id.txt_bio_profilCercevesi);
        txt_Ad = view.findViewById(R.id.txt_ad_profilCercevesi);
        txt_kullaniciAdi = view.findViewById(R.id.txt_kullaniciadi_profilCerceve);

        btn_profili_Duzenle = view.findViewById(R.id.btn_profiliDuzenle_profilCercevesi);


        //metotları cagıracagız

        kullaniciBilgisi();
        takipcileriAl();

        //gonderi sayısınıAl metodu yapacagız

        //Kendi profil sayfamı görüyorsam eger
        if(profilId.equals(firebaseUser.getUid())){
            btn_profili_Duzenle.setText("Profili Düzenle");
        }
        else {

            takipKontrolu();
        }


        btn_profili_Duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = btn_profili_Duzenle.getText().toString();

                if(btn.equals("Profili Düzenle")){
                    //Profili düzenlemeye gitsin
                }
                else if (btn.equals("Takip Et")){ //?
                    //eğer takip et butonuysa firebase de takp in altında kullanıcının ıd sini bulsun onun içinde takip edinlerin değerini true yapsın yani takip edilenlere eklesin
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(profilId).setValue(true);


                    //Takip icinde profilId yi onun icinde Takipcileri bul takipedeni takipcilerine ekle
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("Takipciler").child(firebaseUser.getUid()).setValue(true);

                }

                else if(btn.equals("Takip Ediliyor")){
                    //Hem kendi takipedilenimizden hemde karşı tarafın takipçilerinden çıkıyoruz

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(profilId).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("Takipciler").child(firebaseUser.getUid()).removeValue();


                }


            }
        });






        return view;
    }
    private void kullaniciBilgisi(){
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(profilId);

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext() == null){
                    return;
                }

                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                Glide.with(getContext()).load(kullanici.getResimurl()).into(profil_resmi);
                txt_kullaniciAdi.setText(kullanici.getKullaniciAdi());
                txt_Ad.setText(kullanici.getAdSoy());
                txt_bio.setText(kullanici.getBio());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void takipKontrolu(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                .child("TakipEdilenler");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(profilId).exists()){
                    btn_profili_Duzenle.setText("Takip Ediliyor");
                }

                else {
                    btn_profili_Duzenle.setText("Takip Et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void takipcileriAl(){
        //takipci sayısını alacagız

        DatabaseReference takipciYolu = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                .child("Takipciler");

        takipciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //veritabanında takipci sayısını aldık
                txt_takipciler.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //takip edilen sayısını alacagız
        DatabaseReference takipEdilenYol = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                .child("TakipEdilenler");

        takipEdilenYol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //veritabanında takip edilen sayısını aldık
                txt_takipEdilenler.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}