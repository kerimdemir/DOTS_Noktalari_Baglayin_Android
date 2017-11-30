package com.example.kerim.noktakutu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kerim.noktakutu.Firebase_Database.EnIyiOyuncular;
import com.example.kerim.noktakutu.Firebase_Database.InventorAdapter;
import com.example.kerim.noktakutu.Firebase_Database.InventorModel;
import com.example.kerim.noktakutu.Login.LoginActivity;
import com.example.kerim.noktakutu.Login.UyeOlActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.example.kerim.noktakutu.OyunKontrol.degeri;

/**
 * Created by Kerim on 7.3.2017.
 */

public class OyunSecimActivity extends Activity {
    //Login İşlemi
    ProgressDialog Dialog;

    private Button kullaniciSil, cikisYap, yeniOyun, ikikisilikoyun, eniyioyuncular, exitbutton;
    private ImageView imageProfil;
    private TextView textView;

    private List<InventorModel> inventorModels;
    private InventorAdapter inventorAdapter;
    public static int COUNT;

    private final String TAG = getClass().getSimpleName();
    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String kullaniciadi;
    public static Integer kullannicipuan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.secim_main);

        OyunKontrol.degeri=3;
        OyunActivityTekKisilik.sayac=1;

        imageProfil = (ImageView) findViewById(R.id.imageprofil);
        //Login
        kullaniciSil = (Button) findViewById(R.id.kullaniciSil);
        textView = (TextView) findViewById(R.id.tvKullanici_adi);
        cikisYap = (Button) findViewById(R.id.btn_cikis_yap);
        yeniOyun = (Button) findViewById(R.id.btnYeniOyun);
        ikikisilikoyun = (Button) findViewById(R.id.btnIkioyuncu);
        eniyioyuncular = (Button) findViewById(R.id.btnEniyiOyuncu);
        exitbutton = (Button) findViewById(R.id.exitbutton);

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/d-puntillas-E-Tiptoes-squid.ttf");
        yeniOyun.setTypeface(face1);
        yeniOyun.setTextSize(30);
        kullaniciSil.setTypeface(face1);
        kullaniciSil.setTextSize(30);
        ikikisilikoyun.setTypeface(face1);
        ikikisilikoyun.setTextSize(30);
        kullaniciSil.setTypeface(face1);
        kullaniciSil.setTextSize(30);
        cikisYap.setTypeface(face1);
        cikisYap.setTextSize(30);
        eniyioyuncular.setTypeface(face1);
        eniyioyuncular.setTextSize(30);
        Button btnhakkinda = (Button) findViewById(R.id.btnHakkinda);
        btnhakkinda.setTextSize(30);
        Button btnnasiloynanir = (Button) findViewById(R.id.btnNasilOynanir);
        btnnasiloynanir.setTextSize(30);
        btnhakkinda.setTypeface(face1);
        btnnasiloynanir.setTypeface(face1);
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);


        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance metodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();

        Log.e(TAG, "Email: " + auth.getCurrentUser().getEmail());

        fetchFirebaseDb();


        //Bir AuthStateListener dinleyicisi oluşturuyoruz ve bu dinleyici onAuthStateChanged bölümünü çalıştırır.
        // Buradaki getCurrentUser metodu ile kullanıcı verilerine ulaşabilmekteyiz.
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // Eğer geçerli bir kullanıcı oturumu yoksa LoginActivity e geçilir.
                // Oturum kapatma işlemi yapıldığında bu sayede LoginActivity e geçilir.
                if (user == null) {

                    startActivity(new Intent(OyunSecimActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        //getCurrentUser metodu üzerinden ulaştığımız kullanıcı verilerinde getEmail ile de kullanıcının mailini kullanarak,
        // kullanıcıya bir text gösteriyoruz.
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        yeniOyun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OyunSecimActivity.this, SplashScreen.class);
                startActivity(intent);
            }
        });

        ikikisilikoyun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OyunSecimActivity.this, IkiKisilikOyun.class);
                OyunKontrol.degeri = 6;
                startActivity(intent);
            }
        });

        eniyioyuncular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OyunSecimActivity.this, EnIyiOyuncular.class);
                startActivity(intent);
            }
        });
        kullaniciSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Silme işlemi başarılı oldugunda kullanıcıya bir mesaj gösterilip UyeOlActivity e geçiliyor.
                                    if (task.isSuccessful()) {
                                        Toast.makeText(OyunSecimActivity.this, "Hesabın silindi.Yeni bir hesap oluştur!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(OyunSecimActivity.this, UyeOlActivity.class));
                                        finish();

                                    } else {
                                        //İşlem başarısız olursa kullanıcı bilgilendiriliyor.
                                        Toast.makeText(OyunSecimActivity.this, "Hesap silinemedi!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });

        cikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FirebaseAuth.getInstance().signOut ile oturumu kapatabilmekteyiz.
                auth.signOut();

                startActivity(new Intent(OyunSecimActivity.this, LoginActivity.class));
                finish();
            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oyundanCikis();
            }
        });


        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.kerim.noktakutu", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //Login İşlemi
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Login İşlemi
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }


    public void onBackPressed() {
        Intent myIntent = new Intent(OyunSecimActivity.this, MainActivity.class);

        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
        return;
    }

    public void onhakkindaClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Kerim DEMİR \n Haziran 2017 \n © Tüm Hakları Saklıdır.")
                .setCancelable(false)

                .setTitle("DOT - Noktaları Bağlayın")
                .setIcon(R.drawable.alert_icon)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onnasilOynanirClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("")
                .setCancelable(false)

                .setTitle("Kurallar")
                .setMessage("   Dikey veya yatay bir çizgi işaretlemek için iki nokta arasında dokunun.\n\n " +
                        "   Amacınız dört tarafı çizgilerle tamamlayıp kutu oluşturmaktır.\n\n " +
                        "   Kutuyu tamamlayan oyuncu bir hamle daha oynamak zorundadır.\n\n " +
                        "   Oyun sonunda en fazla kutu yapan oyuncu oyunu kazanır.\n ")

                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        oyundanCikis();
        return super.onKeyDown(keyCode, event);
    }

    public void fetchFirebaseDb() {
          Dialog = new ProgressDialog(OyunSecimActivity.this);
       Dialog = new ProgressDialog(OyunSecimActivity.this);
        Dialog.setMessage("Giriş Yapılıyor");
        Dialog.setCancelable(false);
        Dialog.show();
        myRef = database.getReference();
        myRef.child("inventors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // inventorModels.clear();
                Log.d(TAG, "Value is: " + dataSnapshot.getChildrenCount());
                COUNT = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Log.e(TAG, "Email: " + auth.getCurrentUser().getEmail());

                    InventorModel inventorModel = data.getValue(InventorModel.class);

                    if (inventorModel != null) {
                        if (inventorModel.getEmail().equalsIgnoreCase(auth.getCurrentUser().getEmail())) {
                            Log.e(TAG, "onDataChange: " + inventorModel.getEmail());
                            textView.setText("Giriş Yapıldı, Hoşgeldiniz " + inventorModel.getKullanici_adi() + "\n" + "Puan " + inventorModel.getPuan());
                            kullaniciadi = inventorModel.getKullanici_adi();
                            kullannicipuan = inventorModel.getPuan();
                            Log.e(TAG, "onDataChange: " + "email bulunamadı");
                            break;
                        } else {
                            Log.e(TAG, "onDataChange: Eşleştirilen email: " + inventorModel.getEmail());
                            Log.e(TAG, "onDataChange: " + "email bulunamadı");
                        }
                    } else {
                        Log.i(TAG, "onDataChange: inventormodel is null");
                    }
                    // inventorModels.add(inventorModel);
                }

//                inventorAdapter.notifyDataSetChanged();
                Dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



public void oyundanCikis(){


    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OyunSecimActivity.this); //Mesaj Penceresini Yaratalım
    alertDialogBuilder.setTitle("Oyundan Çıkılsın Mı?").setCancelable(false).setPositiveButton("Evet", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int id) { //Eğer evet butonuna basılırsa
            dialog.dismiss();

            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);

//Uygulamamızı sonlandırıyoruz.
        }
    }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
//Eğer hayır butonuna basılırsa

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    alertDialogBuilder.create().show();
//son olarak alertDialogBuilder'ı oluşturup ekranda görüntületiyoruz.


}

}
