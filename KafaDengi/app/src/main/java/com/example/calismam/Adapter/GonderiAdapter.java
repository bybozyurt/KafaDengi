package com.example.calismam.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calismam.GirisActivity;
import com.example.calismam.Model.Gonderi;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.example.calismam.YorumlarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;



public class GonderiAdapter extends  RecyclerView.Adapter<GonderiAdapter.ViewHolder>{


    public Context mContext;
    public List<Gonderi> mGonderi;

    private FirebaseUser mevcutFirebaseUser;// o an işlem yapan kullanıcı
    





    public GonderiAdapter(Context mContext, List<Gonderi> mGonderi) {
        this.mContext = mContext;
        this.mGonderi = mGonderi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gonderi_ogesi,parent,false); //gonderi_ogesi layoutu bağladık




        return new GonderiAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //mevcut kullanıcıyı aldık

        Gonderi gonderi = mGonderi.get(position);

        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);








        holder.txt_gonderi_sehir.setText(gonderi.getEtkinlik_sehir());
        holder.txt_gonderi_ilce.setText(gonderi.getEtkinlik_ilce());
        holder.txt_gonderi_baslik.setText(gonderi.getEtkinlik_baslik());
        holder.txt_gonderi_tarih_baslangic.setText(gonderi.getEtkinlik_baslangic());
        holder.txt_gonderi_tarih_bitis.setText(gonderi.getEtkinlik_bitis());

        gonderenBilgileri(holder.profil_resmi,holder.txt_kullanici_adi,holder.txt_gonderen,gonderi.getGonderen());
        begenildi(gonderi.getGonderiId(),holder.begeni_resmi);
        begeniSayisi(holder.txt_begeni_sayisi,gonderi.getGonderiId());
        yorumlarıAl(gonderi.getGonderiId(),holder.txt_yorumlari_gor);




        holder.yorum_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);

            }
        });


        holder.txt_yorumlari_gor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);

            }
        });




        holder.begeni_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.begeni_resmi.getTag().equals("begen")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Begeniler")
                            .child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).setValue(true); //beğeniye tıklandığında beğeni veritabanına gidilecek
                }

                else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Begeniler")
                            .child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String min = "Minumum Katılım :"+gonderi.getEtkinlik_min();
                String max = "Maximum Katılım :"+gonderi.getEtkinlik_max();
                String adres = "Etkinlik Adresi :"+gonderi.getEtkinlik_adres();
                String detaylar = "Etkinlik Detayları :"+gonderi.getEtkinlik_detay();
                String ucret = "Katılım Ücreti :"+gonderi.getEtkinlik_ucret();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("ETKİNLİK AYRINTILARI");

                builder.setMessage(min+"\n"+max+"\n"+adres+"\n"+detaylar+"\n"+ucret);


                builder.show();

            }
        });





    }

    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView profil_resmi, yorum_resmi, begeni_resmi, gonderi_resmi;
        public TextView txt_kullanici_adi,txt_gonderen,txt_begeni_sayisi,txt_yorumlari_gor;
        public TextView txt_gonderi_tarih_baslangic, txt_gonderi_tarih_bitis, txt_gonderi_baslik, txt_gonderi_sehir,txt_gonderi_ilce;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_gonderi_ogesi);
            yorum_resmi = itemView.findViewById(R.id.yorum_resmi_gonderi_ogesi);
            begeni_resmi = itemView.findViewById(R.id.begeni_resmi_gonderi_ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_gonderi_ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_gonderi_ogesi);
            txt_gonderen = itemView.findViewById(R.id.txt_gonderen_gonderi_ogesi);
            txt_gonderi_tarih_baslangic = itemView.findViewById(R.id.txt_tarih_baslangic_gonderi_ogesi);
            txt_gonderi_tarih_bitis = itemView.findViewById(R.id.txt_tarih_bitis_gonderi_ogesi);
            txt_gonderi_baslik = itemView.findViewById(R.id.txt_gonderi_baslik_gonderi_ogesi);
            txt_gonderi_sehir = itemView.findViewById(R.id.txt_gonderi_sehir_gonderi_ogesi);
            txt_gonderi_ilce = itemView.findViewById(R.id.txt_gonderi_ilce_gonderi_ogesi);
            txt_begeni_sayisi = itemView.findViewById(R.id.txt_begeni_sayi_gonderi_ogesi);
            txt_yorumlari_gor = itemView.findViewById(R.id.txt_yorumlari_gor_gonderi_ogesi);




        }

    }

    private void yorumlarıAl(String gonderiId, final TextView yorumlar){

        DatabaseReference yorumlariAlmaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar")
                .child(gonderiId);

        yorumlariAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                yorumlar.setText(snapshot.getChildrenCount()+" yorumun hepsini gör...");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }







    private void begenildi(String gonderiId, ImageView imageView){
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);

        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(mevcutKullanici.getUid()).exists()){

                    imageView.setImageResource(R.drawable.ic_begenildi);
                    imageView.setTag("begenildi");


                }
                else {
                    imageView.setImageResource(R.drawable.ic_begeni);
                    imageView.setTag("begen");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void begeniSayisi(TextView begeniler, String gonderiId){

        DatabaseReference begeniSayisiVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);

        begeniSayisiVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                begeniler.setText(snapshot.getChildrenCount()+" beğeni"); // beğenilerin sayılarını gösterdik
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void gonderenBilgileri(ImageView profil_resmi , TextView kullaniciadi, TextView gonderen, String kullaniciId){



        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(kullaniciId);

        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                Glide.with(mContext).load(kullanici.getResimurl()).into(profil_resmi);
                kullaniciadi.setText(kullanici.getKullaniciAdi());// kullanıcı adı kısmına veritabanından kullanıcı adimizi verdik
                gonderen.setText(kullanici.getKullaniciAdi());// gonderen kısmına kullanıcı adımızı verdik




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }








}
