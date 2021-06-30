package com.example.calismam.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calismam.AnaSayfaActivity;
//import com.example.calismam.Cerceve.EtkinlikDetayFragment;
import com.example.calismam.Model.Kullanici;
import com.example.calismam.Model.Yorum;
import com.example.calismam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ViewHolder>{

    private Context mContext;
    private List<Yorum> mYorumListesi;
    private String etkinlikId;


    private FirebaseUser mevcutKullanici;


    public YorumAdapter(Context mContext, List<Yorum> mYorumListesi, String etkinlikId) {
        this.mContext = mContext;
        this.mYorumListesi = mYorumListesi;
        this.etkinlikId = etkinlikId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_ogesi,parent,false);

        return new YorumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        Yorum yorum = mYorumListesi.get(position);

        holder.txt_yorum.setText(yorum.getYorum());

        kullaniciBilgisiAl(holder.profil_resmi,holder.txt_kullanici_adi,yorum.getGonderen());

        holder.txt_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AnaSayfaActivity.class);
                intent.putExtra("gonderenId",yorum.getGonderen());
                mContext.startActivity(intent);


            }
        });


        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, AnaSayfaActivity.class);
                intent.putExtra("gonderenId",yorum.getGonderen());
                mContext.startActivity(intent);


                /*SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("gonderenId", yorum.getGonderen());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new PersonFragment()).commit();*/


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //yorumu atan bensem
                if (yorum.getGonderen().equals(mevcutKullanici.getUid())){

                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Silmek istiyor musun?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference("Yorumlar")
                                    .child(etkinlikId).child(yorum.getYorumid())
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(mContext, "Yorumunuz silindi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();

                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mYorumListesi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profil_resmi;

        public TextView txt_kullanici_adi, txt_yorum;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_yorum_ogesi);
            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_yorum_ogesi);
            txt_yorum = itemView.findViewById(R.id.txt_yorum_yorum_ogesi);



        }
    }

    private void kullaniciBilgisiAl(ImageView imageView,TextView kullaniciadi, String gonderenId){

        DatabaseReference gonderenIdYolu = FirebaseDatabase.getInstance().getReference()
                .child("Kullanıcılar")
                .child(gonderenId);

        gonderenIdYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);
                kullaniciadi.setText(kullanici.getAdSoy());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
