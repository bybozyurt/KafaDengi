package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calismam.Model.Kullanici;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfilDuzenleActivity extends AppCompatActivity {

    private static final String TAG = "";
    ImageView ic_kapat, image_profil;
    TextView kaydet, degis;
    EditText adSoy, kullaniciAdi, bio;

    FirebaseUser firebaseUser;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ic_kapat = findViewById(R.id.ic_kapat);
        image_profil = findViewById(R.id.img_profil_resmi_duzenle);
        kaydet = findViewById(R.id.txt_kaydet);
        degis = findViewById(R.id.txt_pp_degis);
        adSoy = findViewById(R.id.edt_duzenle_ad);
        kullaniciAdi = findViewById(R.id.edt_duzenle_kullanıcı_adı);
        bio = findViewById(R.id.edt_duzenle_biyo);
        setTitle("KAFADENGİ");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                adSoy.setText(kullanici.getAdSoy());
                kullaniciAdi.setText(kullanici.getKullaniciAdi());
                bio.setText(kullanici.getBio());

                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(image_profil);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ic_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        degis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galeridenResimSec();

                /*CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfilDuzenleActivity.this);*/


                
            }
        });

        image_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galeridenResimSec();

               /* CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfilDuzenleActivity.this);*/

            }
        });


        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilGuncelle(adSoy.getText().toString(),
                        kullaniciAdi.getText().toString(),
                        bio.getText().toString());
                finish();
            }


        });

    }

    private void profilGuncelle(String adSoy, String kullaniciAdi, String bio) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                .child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("AdSoy",adSoy);
        hashMap.put("kullaniciAdi",kullaniciAdi);
        hashMap.put("bio",bio);

        databaseReference.updateChildren(hashMap);

    }

    private String dosyaUzantisiAl(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void resimYukle(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Yükleniyor...");
        pd.show();

        if(mImageUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
            +"."+ dosyaUzantisiAl(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Kullanıcılar")
                                .child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("resimurl",""+myUrl);

                        reference.updateChildren(hashMap);
                        pd.dismiss();
                    }
                    else {
                        Toast.makeText(ProfilDuzenleActivity.this, "Gönderme başarısız oldu", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfilDuzenleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Seçilen resim yok", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: Çalıştı");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            mImageUri = data.getData();

            Picasso.get()
                    .load(mImageUri)
                    .centerCrop()
                    .fit()
                    .into(image_profil);

            resimYukle();



        }
        else {
            Toast.makeText(this, "Birşeyler ters gitti", Toast.LENGTH_SHORT).show();
        }



        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            Picasso.get()
                    .load(mImageUri)
                    .centerCrop()
                    .fit()
                    .into(image_profil);


            uploadImage();
        }
        else {
            Toast.makeText(this, "Birşeyler ters gitti", Toast.LENGTH_SHORT).show();
        }*/
    }


    private void galeridenResimSec() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
