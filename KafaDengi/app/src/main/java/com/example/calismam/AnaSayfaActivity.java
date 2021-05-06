package com.example.calismam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.calismam.Cerceve.AddFragment;
import com.example.calismam.Cerceve.NotificationsFragment;
import com.example.calismam.Cerceve.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.example.calismam.Cerceve.HomeFragment;
import com.example.calismam.Cerceve.SearchFragment;
import com.google.firebase.database.FirebaseDatabase;

public class AnaSayfaActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        setTitle("Ana Sayfa");


        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();

        if(intent != null){
            String gonderen = intent.getString("gonderenId");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileId",gonderen);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new PersonFragment()).commit();
        }

        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new HomeFragment()).commit();
        }




    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){

                        case R.id.nav_home:

                            seciliCerceve = new HomeFragment();

                            setTitle("Ana Sayfa");

                            break;

                        case R.id.nav_search:

                            seciliCerceve = new SearchFragment();

                            setTitle("Ara");



                            break;

                        case R.id.nav_add:

                            seciliCerceve = new AddFragment();

                            setTitle("Gönderi Ekle");



                            break;

                        case R.id.nav_notifications:

                            seciliCerceve = new NotificationsFragment();

                            setTitle("Bildirimler");

                            break;

                        case R.id.nav_person:

                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getUid());
                            editor.commit();


                            //Person Page
                            seciliCerceve = new PersonFragment();

                            setTitle("Kullanıcı Profili");

                            break;
                    }

                    if(seciliCerceve != null){ //Secili cerceve aktif olduğunda
                            //Framelayout id si olan cerceve_kapsayici yerine seciliCerceve gelecek.
                            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,seciliCerceve).commit();


                    }


                    return true;
                }
            };



    //Menu bağlamada kullandığımız metod
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        //getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    //Menude sectigimiz itemleri kullancagımız metod
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.signout){
            firebaseAuth.signOut();
            Intent intetToKayitActivity = new Intent(AnaSayfaActivity.this, GirisActivity.class);
            startActivity(intetToKayitActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }





}



