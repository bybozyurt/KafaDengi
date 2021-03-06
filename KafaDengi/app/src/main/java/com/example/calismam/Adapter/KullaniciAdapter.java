package com.example.calismam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calismam.AnaSayfaActivity;
import com.example.calismam.Cerceve.PersonFragment;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.example.calismam.SohbetActivity;
import com.example.calismam.TakipcilerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder>{

    private Context mContext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser firebaseUser;
    private boolean isfragment;


    public KullaniciAdapter(Context mContext, List<Kullanici> mKullanicilar,boolean isfragment) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;
        this.isfragment = isfragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi,parent, false);



        return new KullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Kullanici kullanici = mKullanicilar.get(position);

        //String hisUID = mKullanicilar.get(position).getId();
        holder.btn_takipEt.setVisibility(View.VISIBLE); //gone idi visibile yapt??k
        holder.kullaniciadi.setText(kullanici.getKullaniciAdi()); // modelde ki getKullaniciAdi
        holder.ad.setText(kullanici.getAdSoy());
        Glide.with(mContext).load(kullanici.getResimurl()).into(holder.profil_image); //Glide kulland??k picassoda olur

        takipEdiliyor(kullanici.getId(),holder.btn_takipEt);

        if(kullanici.getId().equals(firebaseUser.getUid())){ //Kullan??c?? kendisinin yan??nda takip et butonu g??rmesin
            holder.btn_takipEt.setVisibility(View.GONE);//butonu g??r??nmez yapt??k
            holder.img_mesaj.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            //Kullan??c??lara t??klad??g??mda sharedpref yard??m??yla person activitye gidiyoruz
            public void onClick(View v) {
                if(isfragment) {

                    
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", kullanici.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                            new PersonFragment()).commit();
                }
                else {

                    Toast.makeText(mContext, "hesdasdayyo", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, AnaSayfaActivity.class);
                    intent.putExtra("gonderenId",kullanici.getId());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.img_mesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, SohbetActivity.class);
                intent.putExtra("hisUid",kullanici.getId());
                //intent.putExtra("profileid",kullanici.getId());

                mContext.startActivity(intent);
            }
        });






        holder.btn_takipEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_takipEt.getText().toString().equals("Takip Et")){

                    //Ben takip ediyorsam kar???? taraf?? benim takip ettiklerime ekliyor
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(kullanici.getId()).setValue(true);

                    //Kar???? taraf beni takip ediyorsa takip ettiklerine beni ekleyecek
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("Takipciler").child(firebaseUser.getUid()).setValue(true);

                    eklenenBildirimler(kullanici.getId());

                }
                //E??er zaten takip ediliyor takip ediyora t??klay??nca takipten ????kacak ve buton takip ol a d??n????ecek
                else {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(kullanici.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("Takipciler").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

    }

    private void eklenenBildirimler(String kullaniciId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bildirimler")
                .child(kullaniciId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("kullaniciId",firebaseUser.getUid());
        hashMap.put("text","seni takip etmeye ba??lad??");
        hashMap.put("etkinlikId","");
        hashMap.put("isGonderi",false);

        databaseReference.push().setValue(hashMap);



    }



    @Override
    public int getItemCount() {

        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView kullaniciadi;
        public TextView ad;
        public CircleImageView profil_image;
        public Button btn_takipEt;
        public ImageButton img_mesaj;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kullaniciadi = itemView.findViewById(R.id.txt_kullaniciadi_oge);
            ad = itemView.findViewById(R.id.txt_ad_Oge);
            profil_image = itemView.findViewById(R.id.profil_image_oge);
            btn_takipEt = itemView.findViewById(R.id.btn_takipEt_oge);
            img_mesaj = itemView.findViewById(R.id.img_btn_mesaj_kullaniciOgesi);

        }
    }

    private void takipEdiliyor(String kullaniciId,Button button){
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(firebaseUser.getUid()).child("TakipEdilenler");
        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(kullaniciId).exists()){ //verilerin i??inde takip edilen kullan??c?? var ise
                    button.setText("Takip Ediliyor");
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

}
