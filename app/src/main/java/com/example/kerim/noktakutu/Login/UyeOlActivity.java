package com.example.kerim.noktakutu.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kerim.noktakutu.Database;
import com.example.kerim.noktakutu.Firebase_Database.InventorModel;
import com.example.kerim.noktakutu.OyunSecimActivity;
import com.example.kerim.noktakutu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;


public class UyeOlActivity extends AppCompatActivity {

    public  int puan;
    private Map<String, Object> postValues;
    private EditText uyeEmail,uyeParola,uyeKullaniciAdi;
    private Button yeniUyeButton,uyeGirisButton;
    private FirebaseAuth auth;
    private InventorModel inventorModel;
    private FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_uye_ol_layout);

        myRef = mdatabase.getReference();

        //FirebaseAuth sınıfının referans olduğu nesneleri kullanmak için getInstance methodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();
        uyeKullaniciAdi = (EditText) findViewById(R.id.kullaniciAdi);
        uyeEmail = (EditText) findViewById(R.id.uyeEmail);
        uyeParola = (EditText) findViewById(R.id.uyeParola);
        yeniUyeButton = (Button) findViewById(R.id.yeniUyeButton);
        uyeGirisButton = (Button) findViewById(R.id.uyeGirisButton);


        yeniUyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uyeEmail.getText().toString();
                String parola = uyeParola.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Lütfen emailinizi giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(parola)) {
                    Toast.makeText(getApplicationContext(), "Lütfen parolanızı giriniz", Toast.LENGTH_SHORT).show();
                }
                if (parola.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Parola en az 6 haneli olmalıdır", Toast.LENGTH_SHORT).show();
                }

                //FirebaseAuth ile email,parola parametrelerini kullanarak yeni bir kullanıcı oluşturuyoruz.
                auth.createUserWithEmailAndPassword(email, parola)
                        .addOnCompleteListener(UyeOlActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //İşlem başarısız olursa kullanıcıya bir Toast mesajıyla bildiriyoruz.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(UyeOlActivity.this, "Yetkilendirme Hatası",
                                            Toast.LENGTH_SHORT).show();
                                }

                                //İşlem başarılı olduğu takdir de giriş yapılıp Main_Activity e yönlendiriyoruz.
                                else {
                                    saveEntry();
                                    startActivity(new Intent(UyeOlActivity.this, OyunSecimActivity.class));
                                    finish();
                                }

                            }
                        });
            }
        });


        uyeGirisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UyeOlActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void saveEntry() {
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(inventorModel == null) {
            //Yeni Kayıt
            inventorModel = new InventorModel();

            inventorModel.setSifre(uyeParola.getText().toString());
            inventorModel.setKullanici_adi(uyeKullaniciAdi.getText().toString());
            inventorModel.setEmail(uyeEmail.getText().toString());
            inventorModel.setPuan(puan);

            inventorModel.setAnahtar(key);
            FirebaseDatabase.getInstance().getReference().child("inventors").child(inventorModel.getAnahtar()).setValue(inventorModel);
            Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_SHORT).show();
            Log.i("SAVE", "saveEntry: Kaydedildi.");
        }else{
            //Güncelle
            inventorModel.setKullanici_adi(uyeKullaniciAdi.getText().toString());
            inventorModel.setSifre(uyeParola.getText().toString());
            inventorModel.setEmail(uyeEmail.getText().toString());
            inventorModel.setPuan(puan);
            postValues = inventorModel.toMap();
            FirebaseDatabase.getInstance().getReference().child("inventors").child(inventorModel.getAnahtar()).updateChildren(postValues);
            Toast.makeText(getApplicationContext(),"Güncellendi",Toast.LENGTH_SHORT).show();
            Log.i("SAVE", "saveEntry: Güncellendi.");
        }
    }

}