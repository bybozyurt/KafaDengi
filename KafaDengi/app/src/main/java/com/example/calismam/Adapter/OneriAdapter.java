package com.example.calismam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calismam.AnaSayfaActivity;
//import com.example.calismam.Cerceve.EtkinlikDetayFragment;
import com.example.calismam.Cerceve.PersonFragment;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class OneriAdapter extends RecyclerView.Adapter<OneriAdapter.ViewHolder>{

    private Context mContext;
    private List<Kullanici> kullaniciList;
    private FirebaseUser mevcutKullanici;


    public OneriAdapter(Context mContext, List<Kullanici> kullaniciList) {
        this.mContext = mContext;
        this.kullaniciList = kullaniciList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.oneri_item,parent,false);

        return new OneriAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        final  Kullanici kullanici = kullaniciList.get(position);

        holder.btn_takip.setVisibility(View.VISIBLE); //gone idi visibile yaptık
        holder.txt_kullanici_adi.setText(kullanici.getKullaniciAdi()); // modelde ki getKullaniciAdi
        holder.txt_ad.setText(kullanici.getAdSoy());
        Glide.with(mContext).load(kullanici.getResimurl()).into(holder.profil_resmi);

        takipEdiliyor(kullanici.getId(),holder.btn_takip,holder.txt_kullanici_adi,holder.txt_ad,holder.profil_resmi);

        if(kullanici.getId().equals(mevcutKullanici.getUid())){ //Kullanıcı kendisinin yanında takip et butonu görmesin
            holder.profil_resmi.setVisibility(View.GONE);//butonu görünmez yaptık
            holder.txt_kullanici_adi.setVisibility(View.GONE);
            holder.txt_ad.setVisibility(View.GONE);
            holder.btn_takip.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", kullanici.getId());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new PersonFragment()).commit();
            }
        });


        holder.btn_takip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_takip.getText().toString().equals("Takip Et")){

                    //Ben takip ediyorsam karşı tarafı benim takip ettiklerime ekliyor
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("TakipEdilenler").child(kullanici.getId()).setValue(true);

                    //Karşı taraf beni takip ediyorsa takip ettiklerine beni ekleyecek
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("Takipciler").child(mevcutKullanici.getUid()).setValue(true);

                    eklenenBildirimler(kullanici.getId());

                }
                //Eğer zaten takip ediliyor takip ediyora tıklayınca takipten çıkacak ve buton takip ol a dönüşecek
                else {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(mevcutKullanici.getUid())
                            .child("TakipEdilenler").child(kullanici.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("Takipciler").child(mevcutKullanici.getUid()).removeValue();

                }
            }
        });



    }

    private void eklenenBildirimler(String kullaniciId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(kullaniciId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciId",mevcutKullanici.getUid());
        hashMap.put("text","seni takip etmeye başladı");
        hashMap.put("etkinlikId","");
        hashMap.put("isGonderi",false);

        databaseReference.push().setValue(hashMap);



    }

    private void takipEdiliyor(String kullaniciId,Button button,TextView kullaniciAdi, TextView ad, ImageView pp){
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(mevcutKullanici.getUid()).child("TakipEdilenler");
        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(kullaniciId).exists()){ //verilerin içinde takip edilen kullanıcı var ise

                    button.setVisibility(View.GONE);
                    pp.setVisibility(View.GONE);
                    kullaniciAdi.setVisibility(View.GONE);
                    ad.setVisibility(View.GONE);








                }
                else {
                    button.setText("Takip Et");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return kullaniciList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil_resmi;

        public TextView txt_kullanici_adi, txt_ad;

        public Button btn_takip;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_img_oneriItem);
            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_oneriItem);
            txt_ad = itemView.findViewById(R.id.txt_ad_oneriItem);
            btn_takip = itemView.findViewById(R.id.btn_takipEt_oneriItem);



        }
    }




    }

