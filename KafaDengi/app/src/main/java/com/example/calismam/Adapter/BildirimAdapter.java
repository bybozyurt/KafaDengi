package com.example.calismam.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calismam.Cerceve.EtkinlikDetayFragment;
import com.example.calismam.Cerceve.PersonFragment;
import com.example.calismam.Model.Bildirim;
import com.example.calismam.Model.Etkinlik;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BildirimAdapter extends RecyclerView.Adapter<BildirimAdapter.ViewHolder>{

    private Context mContext;
    private List<Bildirim> mBildirim;

    public BildirimAdapter(Context mContext, List<Bildirim> mBildirim) {
        this.mContext = mContext;
        this.mBildirim = mBildirim;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);

        return new BildirimAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bildirim bildirim = mBildirim.get(position);

        holder.text.setText(bildirim.getText());

        kullaniciBilgisiAl(holder.image_profil,holder.kullaniciAdi,bildirim.getKullaniciId());

        if(!bildirim.isIspost()){
            holder.image_etkinlik.setVisibility(View.VISIBLE);
            etkinlikResmiAl(holder.image_etkinlik,bildirim.getEtkinlikId());
        }
        else {
            holder.image_etkinlik.setVisibility(View.GONE);
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bildirim.isIspost()){

                    Toast.makeText(mContext, "heyyo", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("etkinlikId",bildirim.getEtkinlikId());
                    editor.apply();



                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                            new EtkinlikDetayFragment()).commit();

                }

                else {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("profileid",bildirim.getKullaniciId());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                            new PersonFragment()).commit();


                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return mBildirim.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profil, image_etkinlik;
        public TextView kullaniciAdi, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profil = itemView.findViewById(R.id.image_profil);
            image_etkinlik = itemView.findViewById(R.id.image_etkinlik);
            kullaniciAdi = itemView.findViewById(R.id.txt_kullaniciadi_notification_item);
            text = itemView.findViewById(R.id.text);


        }
    }

    private void kullaniciBilgisiAl(final ImageView imageView, final TextView kullaniciAdi, String gonderenId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                .child(gonderenId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);
                kullaniciAdi.setText(kullanici.getKullaniciAdi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void etkinlikResmiAl(final ImageView imageView, final String etkinlikId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Etkinlikler")
                .child(etkinlikId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Etkinlik etkinlik = snapshot.getValue(Etkinlik.class);
                Glide.with(mContext).load(etkinlik.getEtkinlikResmi()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
