package com.example.calismam.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.calismam.Cerceve.EtkinlikDetayFragment;
import com.example.calismam.Model.Etkinlik;
import com.example.calismam.R;

import java.util.List;

public class ProfilAdapter extends RecyclerView.Adapter<ProfilAdapter.ViewHolder> {

    private Context mContext;
    private List<Etkinlik> mEtkinlik;


    public ProfilAdapter(Context mContext, List<Etkinlik> mEtkinlik) {
        this.mContext = mContext;
        this.mEtkinlik = mEtkinlik;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.etkinlik_profil,parent,false);


        return new ProfilAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Etkinlik etkinlik = mEtkinlik.get(position);

        Glide.with(mContext).load(etkinlik.getEtkinlikResmi()).into(holder.profil_etkinligi);


        holder.profil_etkinligi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("etkinlikId", etkinlik.getEtkinlikId());
                editor.apply();


                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new EtkinlikDetayFragment()).commit();



            }
        });






    }

    @Override
    public int getItemCount() {

        return mEtkinlik.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profil_etkinligi;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_etkinligi = itemView.findViewById(R.id.profil_gonderisi);



        }
    }
}
