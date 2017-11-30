package com.example.kerim.noktakutu.Firebase_Database;

/**
 * Created by Kerim on 17.3.2017.
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventorModel implements Serializable {


    private String kullanici_adi;
    private String sifre;
    private String email;
    private int puan;
private String anahtar;

    public InventorModel() {
    }


    public void setKullanici_adi(String kullanici_adi) {
        this.kullanici_adi = kullanici_adi;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPuan(Integer puan) {
        this.puan = puan;
    }


    public String getKullanici_adi() {
        return kullanici_adi;
    }

    public String getSifre() {
        return sifre;
    }
    public Integer getPuan() {
        return puan;
    }



    public String getEmail() {
        return email;
    }

    public String join(List<String> list, String delim) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for(String s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }

        return sb.toString();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("kullanici_adi", kullanici_adi);
        result.put("sifre", sifre);
        result.put("puan", puan);
        result.put("anahtar",anahtar);
        result.put("email", email);
        return result;
    }

    public String getAnahtar() {
        return anahtar;
    }

    public void setAnahtar(String anahtar) {
        this.anahtar = anahtar;
    }
}
