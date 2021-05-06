package com.example.calismam.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder>{

    private Context mContext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser firebaseUser;

    public KullaniciAdapter(Context mContext, List<Kullanici> mKullanicilar) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;
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

        holder.btn_takipEt.setVisibility(View.VISIBLE); //gone idi visibile yaptık
        holder.kullaniciadi.setText(kullanici.getKullaniciAdi()); // modelde ki getKullaniciAdi
        holder.ad.setText(kullanici.getAdSoy());
        Glide.with(mContext).load(kullanici.getResimurl()).into(holder.profil_image); //Glide kullandık picassoda olur

        takipEdiliyor(kullanici.getId(),holder.btn_takipEt);

        if(kullanici.getId().equals(firebaseUser.getUid())){ //Kullanıcı kendisinin yanında takip et butonu görmesin
            holder.btn_takipEt.setVisibility(View.GONE);//butonu görünmez yaptık
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            //Kullanıcılara tıkladıgımda sharedpref yardımıyla person activitye gidiyoruz
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",kullanici.getId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new PersonFragment()).commit();
            }
        });

        holder.btn_takipEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_takipEt.getText().toString().equals("Takip Et")){

                    //Ben takip ediyorsam karşı tarafı benim takip ettiklerime ekliyor
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(kullanici.getId()).setValue(true);

                    //Karşı taraf beni takip ediyorsa takip ettiklerine beni ekleyecek
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("Takipciler").child(firebaseUser.getUid()).setValue(true);

                }
                //Eğer zaten takip ediliyor takip ediyora tıklayınca takipten çıkacak ve buton takip ol a dönüşecek
                else {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(kullanici.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("Takipciler").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

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



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kullaniciadi = itemView.findViewById(R.id.txt_kullaniciadi_oge);
            ad = itemView.findViewById(R.id.txt_ad_Oge);
            profil_image = itemView.findViewById(R.id.profil_image_oge);
            btn_takipEt = itemView.findViewById(R.id.btn_takipEt_oge);

        }
    }

    private void takipEdiliyor(String kullaniciId,Button button){
        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(firebaseUser.getUid()).child("TakipEdilenler");
        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(kullaniciId).exists()){ //verilerin içinde takip edilen kullanıcı var ise
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
