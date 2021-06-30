package com.example.calismam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.calismam.Cerceve.EtkinlikDetayFragment;
import com.example.calismam.Cerceve.PersonFragment;
import com.example.calismam.Model.Etkinlik;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.example.calismam.TakipcilerActivity;
import com.example.calismam.YorumlarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;



public class EtkinlikAdapter extends  RecyclerView.Adapter<EtkinlikAdapter.ViewHolder>{



    public Context mContext;
    public List<Etkinlik> mEtkinlik;

    private FirebaseUser mevcutFirebaseUser;// o an işlem yapan kullanıcı
    private boolean isfragment;
    





    public EtkinlikAdapter(Context mContext, List<Etkinlik> mEtkinlik, boolean isfragment) {
        this.mContext = mContext;
        this.mEtkinlik = mEtkinlik;
        this.isfragment = isfragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.etkinlik_ogesi,parent,false); //etkinlik_ogesi layoutu bağladık




        return new EtkinlikAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //mevcut kullanıcıyı aldık

        Etkinlik etkinlik = mEtkinlik.get(position);

        Glide.with(mContext)
                .load(etkinlik.getEtkinlikResmi())
                .into(holder.etkinlik_resmi);





        holder.txt_etkinlik_sehir.setText(etkinlik.getSehir());
        holder.txt_etkinlik_ilce.setText(etkinlik.getEtkinlik_ilce());
        holder.txt_etkinlik_baslik.setText(etkinlik.getEtkinlik_baslik());
        holder.txt_etkinlik_tarih_baslangic.setText(etkinlik.getEtkinlik_baslangic());
        holder.txt_etkinlik_tarih_bitis.setText(etkinlik.getEtkinlik_bitis());

        gonderenBilgileri(holder.profil_resmi,holder.txt_kullanici_adi,holder.txt_gonderen, etkinlik.getGonderen());
        katilBasan(etkinlik.getEtkinlikId(),holder.katil_resmi);
        katilSayisi(holder.txt_katil_sayisi, etkinlik.getEtkinlikId());
        yorumlarıAl(etkinlik.getEtkinlikId(),holder.txt_yorumlari_gor);







        holder.yorum_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("etkinlikId", etkinlik.getEtkinlikId());
                intent.putExtra("gonderenId", etkinlik.getGonderen());
                mContext.startActivity(intent);

            }
        });


        holder.txt_yorumlari_gor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YorumlarActivity.class);
                intent.putExtra("etkinlikId", etkinlik.getEtkinlikId());
                intent.putExtra("gonderenId", etkinlik.getGonderen());
                mContext.startActivity(intent);

            }
        });




        holder.katil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.katil_resmi.getTag().equals("katil")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("KatilmakIsteyenler")
                            .child(etkinlik.getEtkinlikId())
                            .child(mevcutFirebaseUser.getUid()).setValue(true); //katil ikonuna tıklandığında katilmakIsteyen veritabanına gidilecek
                    eklenenBildirimler(etkinlik.getGonderen(), etkinlik.getEtkinlikId());
                }

                else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("KatilmakIsteyenler")
                            .child(etkinlik.getEtkinlikId())
                            .child(mevcutFirebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid", etkinlik.getGonderen());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new PersonFragment()).commit();




                /*String min = "Minumum Katılım :"+etkinlik.getEtkinlik_min();
                String max = "Maximum Katılım :"+etkinlik.getEtkinlik_max();
                String adres = "Etkinlik Adresi :"+etkinlik.getEtkinlik_adres();
                String detaylar = "Etkinlik Detayları :"+etkinlik.getEtkinlik_detay();
                String ucret = "Katılım Ücreti :"+etkinlik.getEtkinlik_ucret();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("ETKİNLİK AYRINTILARI");

                builder.setMessage(min+"\n"+max+"\n"+adres+"\n"+detaylar+"\n"+ucret);


                builder.show();*/



            }
        });



        holder.txt_kullanici_adi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid", etkinlik.getGonderen());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new PersonFragment()).commit();



            }
        });

        holder.txt_gonderen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid", etkinlik.getGonderen());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new PersonFragment()).commit();



            }
        });


        holder.etkinlik_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("etkinlikId", etkinlik.getEtkinlikId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new EtkinlikDetayFragment()).commit();



            }
        });

        holder.txt_katil_sayisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TakipcilerActivity.class);
                intent.putExtra("id", etkinlik.getEtkinlikId());
                intent.putExtra("baslik","Katilmak İsteyenler");
                mContext.startActivity(intent);

            }
        });

        holder.secenekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.delete:

                                FirebaseDatabase.getInstance().getReference("Etkinlikler")
                                        .child(etkinlik.getEtkinlikId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(mContext, "Başarıyla Silindi", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                return true;

                            default:
                                return false;


                        }
                    }
                });
                popupMenu.inflate(R.menu.ayar_etkinlik);

                //etkinligi atan baskasiysa silme seçeneğigini goremeyecek
                if(!etkinlik.getGonderen().equals(mevcutFirebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);

                }
                popupMenu.show();

            }
        });








    }

    @Override
    public int getItemCount() {
        return mEtkinlik.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView profil_resmi, yorum_resmi, katil_resmi, etkinlik_resmi, secenekler;
        public TextView txt_kullanici_adi,txt_gonderen, txt_katil_sayisi,txt_yorumlari_gor;
        public TextView txt_etkinlik_tarih_baslangic, txt_etkinlik_tarih_bitis, txt_etkinlik_baslik, txt_etkinlik_sehir, txt_etkinlik_ilce;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_etkinlik_ogesi);
            yorum_resmi = itemView.findViewById(R.id.yorum_resmi_etkinlik_ogesi);
            katil_resmi = itemView.findViewById(R.id.katil_resmi_etkinlik_ogesi);
            etkinlik_resmi = itemView.findViewById(R.id.etkinlik_resmi_etkinlik_ogesi);
            secenekler = itemView.findViewById(R.id.more_etkinlik_ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_etkinlik_ogesi);
            txt_gonderen = itemView.findViewById(R.id.txt_gonderen_etkinlik_ogesi);
            txt_etkinlik_tarih_baslangic = itemView.findViewById(R.id.txt_tarih_baslangic_etkinlik_ogesi);
            txt_etkinlik_tarih_bitis = itemView.findViewById(R.id.txt_tarih_bitis_etkinlik_ogesi);
            txt_etkinlik_baslik = itemView.findViewById(R.id.txt_etkinlik_baslik_etkinlik_ogesi);
            txt_etkinlik_sehir = itemView.findViewById(R.id.txt_etkinlik_sehir_etkinlik_ogesi);
            txt_etkinlik_ilce = itemView.findViewById(R.id.txt_etkinlik_ilce_etkinlik_ogesi);
            txt_katil_sayisi = itemView.findViewById(R.id.txt_katil_sayi_etkinlik_ogesi);
            txt_yorumlari_gor = itemView.findViewById(R.id.txt_yorumlari_gor_etkinlik_ogesi);




        }

    }

    private void yorumlarıAl(String etkinlikId, final TextView yorumlar){

        DatabaseReference yorumlariAlmaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar")
                .child(etkinlikId);

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

    private void eklenenBildirimler(String kullaniciId, String etkinlikId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(kullaniciId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciId",mevcutFirebaseUser.getUid());
        hashMap.put("text","etkinliğine katılmak istiyor");
        hashMap.put("etkinlikId",etkinlikId);
        hashMap.put("isGonderi",true);

        databaseReference.push().setValue(hashMap);



    }







    private void katilBasan(String etkinlikId, ImageView imageView){
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference katilSayisiVeritabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("KatilmakIsteyenler")
                .child(etkinlikId);

        katilSayisiVeritabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(mevcutKullanici.getUid()).exists()){

                    imageView.setImageResource(R.drawable.ic_katiliyor);
                    imageView.setTag("katilmakIstiyor");


                }
                else {
                    imageView.setImageResource(R.drawable.ic_katil);
                    imageView.setTag("katil");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void katilSayisi(TextView katilanlar, String etkinlikId){

        DatabaseReference katilSayisiVeritabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("KatilmakIsteyenler")
                .child(etkinlikId);

        katilSayisiVeritabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                katilanlar.setText(snapshot.getChildrenCount()+" kişi katılmak istiyor"); // beğenilerin sayılarını gösterdik
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
