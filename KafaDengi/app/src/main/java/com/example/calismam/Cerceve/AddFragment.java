package com.example.calismam.Cerceve;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calismam.AnaSayfaActivity;
import com.example.calismam.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

import java.util.HashMap;


public class AddFragment extends Fragment {


    private static final String TAG = "";
    Uri resimUri;
    String benimUrim = "";

    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;

    TextView textView;
    EditText Etkinlik_baslik, Etkinlik_min, Etkinlik_max, Etkinlik_baslangic, Etkinlik_bitis, Etkinlik_sehir, Etkinlik_ilce, Etkinlik_adres,
    Etkinlik_detay, Etkinlik_ucret;
    ImageView etkinlik_resmi;
    Button btn_yukle;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = (View) inflater.inflate(R.layout.fragment_add, container, false);

        getActivity().setTitle("Etkinlik Olustur");




        Etkinlik_baslik = view.findViewById(R.id.edt_Etkinlik_baslik);
        Etkinlik_min = view.findViewById(R.id.edt_Etkinlik_min);
        Etkinlik_max = view.findViewById(R.id.edt_Etkinlik_max);
        Etkinlik_adres = view.findViewById(R.id.edt_Etkinlik_adres);
        Etkinlik_baslangic = view.findViewById(R.id.edt_Etkinlik_baslangic);
        Etkinlik_bitis = view.findViewById(R.id.edt_Etkinlik_bitis);
        Etkinlik_detay = view.findViewById(R.id.edt_Etkinlik_detay);
        Etkinlik_sehir = view.findViewById(R.id.edt_Etkinlik_sehir);
        Etkinlik_ilce = view.findViewById(R.id.edt_Etkinlik_ilce);
        Etkinlik_ucret = view.findViewById(R.id.edt_Etkinlik_ucret);

        btn_yukle = view.findViewById(R.id.btn_yukle);

        etkinlik_resmi = view.findViewById(R.id.yuklenecek_resim);

        resimYukleYolu = FirebaseStorage.getInstance().getReference("Etkinlikler");


        btn_yukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
            }
        });


        etkinlik_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galeridenResimSec();



                /*CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(getActivity());*/


            }
        });




        return view;

    }




    private String dosyaUzantisiAl(Uri uri){

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void resimYukle() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Gönderiliyor...");
        progressDialog.show();

        if(resimUri != null){
            StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    +"."+dosyaUzantisiAl(resimUri));
            yuklemeGorevi = dosyaYolu.putFile(resimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){

                        throw task.getException();
                    }

                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri indirmeUrisi = task.getResult();
                        benimUrim = indirmeUrisi.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Etkinlikler");

                        String etkinlikId = veriYolu.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("etkinlikId",etkinlikId);
                        hashMap.put("etkinlikResmi",benimUrim);
                        hashMap.put("gonderen",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("etkinlik_baslik",Etkinlik_baslik.getText().toString());
                        hashMap.put("etkinlik_adres",Etkinlik_adres.getText().toString());
                        hashMap.put("etkinlik_baslangic",Etkinlik_baslangic.getText().toString());
                        hashMap.put("etkinlik_bitis",Etkinlik_bitis.getText().toString());
                        hashMap.put("etkinlik_min",Etkinlik_min.getText().toString());
                        hashMap.put("etkinlik_max",Etkinlik_max.getText().toString());
                        hashMap.put("sehir",Etkinlik_sehir.getText().toString());
                        hashMap.put("etkinlik_ilce",Etkinlik_ilce.getText().toString());
                        hashMap.put("etkinlik_ucret",Etkinlik_ucret.getText().toString());
                        hashMap.put("etkinlik_detay",Etkinlik_detay.getText().toString());




                        veriYolu.child(etkinlikId).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(getActivity(),AnaSayfaActivity.class));
                        getActivity().finish();

                    }
                    else {
                        Toast.makeText(getActivity(), "Gönderme Başarısız!", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        else {
            Toast.makeText(getActivity(),"Seçilen resim yok", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: Çalıştı");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){


            resimUri = data.getData();

            Picasso.get()
                    .load(resimUri)
                    .centerCrop()
                    .fit()
                    .into(etkinlik_resmi);



           /*CropImage.activity(resimUri)
                    .setAspectRatio(1,1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(getContext(), AddFragment.this);*/


            //etkinlik_resmi.setImageURI(resimUri);

        }

        else  {
            Toast.makeText(getActivity(), "Resim seçilemedi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), AnaSayfaActivity.class));
            getActivity().finish();


        }
    }

    // Galeriye girebilmek için izinlerimizi alıyoruz
    private void galeridenResimSec() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            Log.d(TAG, "onClick: Daha önceden izin verilmediğinden izin istendi");
        } else {
            Intent intentGaleri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGaleri, 2);
            Log.d(TAG, "onClick: Daha önceden izin verildiğinden kullanıcı Galeriye yönlendirildi");
        }

    }

    
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentGaleri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGaleri, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

















}