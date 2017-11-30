package com.example.kerim.noktakutu;

/**
 * Created by Kerim on 22.6.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NoktaKutuOyunu";//database adi
     private static final String TABLE_NAME = "NoktaKutuOyunu";
    private static String OYUNCU1 = "oyuncu1";
    private static String OYUNCU2 = "oyuncu2";
    private static String OYUNCU = "oyuncu";
    private static String ID = "id";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NoktaKutuOyunu (id INTEGER PRIMARY KEY AUTOINCREMENT,oyuncu1 TEXT,oyuncu2 TEXT,oyuncu TEXT);");


    }



    public void veriEkle(String oyuncu1, String oyuncu2,String oyuncu) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OYUNCU1, oyuncu1);
        values.put(OYUNCU2, oyuncu2);
        values.put(OYUNCU, oyuncu);



        db.insert(TABLE_NAME, null, values);
        db.close(); //Bağlanti kapatildi.
    }


    public HashMap<String, String> sonucEkle(int id) {
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alinir.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String, String> sonuc = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM NoktaKutuOyunu  WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            sonuc.put(OYUNCU1, cursor.getString(1));
            sonuc.put(OYUNCU2, cursor.getString(2));
            sonuc.put(OYUNCU, cursor.getString(3));

        }
        cursor.close();
        db.close();
        // return kelime
        return sonuc;
    }

    public ArrayList<HashMap<String, String>> sonuc() {

        //Bu methodda ise tablodaki tüm değerleri aliyoruz
        //ArrayList adi üstünde Array lerin listelendiği bir Array. Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM NoktaKutuOyunu" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                list.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return list;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE If EXIST NoktaKutuOyunu");

        onCreate(db);

    }


    public void veriSil(int id){ //id si belli olan row u silmek i�in

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}