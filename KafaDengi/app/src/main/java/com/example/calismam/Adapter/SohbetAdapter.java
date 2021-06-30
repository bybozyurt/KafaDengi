package com.example.calismam.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calismam.Model.Sohbet;
import com.example.calismam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SohbetAdapter extends RecyclerView.Adapter<SohbetAdapter.MyHolder>{

    private static final int MSJ_SOL = 0;
    private static final int MSJ_SAG = 1;
    Context mContext;
    List<Sohbet> sorbetList;
    String imageUrl;
    FirebaseUser firebaseUser;

    public SohbetAdapter(Context mContext, List<Sohbet> sohbetList, String imageUrl) {
        this.mContext = mContext;
        this.sorbetList = sohbetList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public SohbetAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSJ_SAG){

            View view = LayoutInflater.from(mContext).inflate(R.layout.sag_satir_chat, parent, false);
            return new SohbetAdapter.MyHolder(view);

        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.sol_satir_chat, parent, false);
            return new SohbetAdapter.MyHolder(view);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String message = sorbetList.get(position).getMessage();
        String timeStamp = sorbetList.get(position).getTimestamp();



        //zamanı eng ye göre yapalım

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();












        /*Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        try {
            cal.setTimeInMillis(Long.parseLong(zaman));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        String tarihSaat = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();*/


        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(holder.profileIv);

        }
        catch (Exception e){

        }

        //goruldu ayarlama

        if(position == sorbetList.size()-1){
            if(sorbetList.get(position).isGoruldu()){
                holder.gorulduTv.setText("Görüldü");
            }
            else {
                holder.gorulduTv.setText("Gönderildi");
            }

        }
        else {
            holder.gorulduTv.setVisibility(View.GONE);

        }




    }

    @Override
    public int getItemCount() {
        return sorbetList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //oturumu açık olan kullanıcı

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(sorbetList.get(position).getGonderen().equals(firebaseUser.getUid())){


            return MSJ_SAG;
            //mesajı gonderen kişinin oturumu acık ise sağ layout inflate edilecek

        }
        else {
            return MSJ_SOL;

        }
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView messageTv, timeTv, gorulduTv;



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            gorulduTv = itemView.findViewById(R.id.gorulduTv);


        }
    }
}
