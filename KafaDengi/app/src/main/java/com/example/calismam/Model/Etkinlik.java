package com.example.calismam.Model;

public class Etkinlik {

    private String etkinlikId;
    private String gonderen;
    private String etkinlikResmi;
    private String etkinlik_baslik;
    private String etkinlik_min;
    private String etkinlik_max;
    private String etkinlik_baslangic;
    private String etkinlik_bitis;
    private String sehir;
    private String etkinlik_ilce;
    private String etkinlik_adres;
    private String etkinlik_detay;
    private String etkinlik_ucret;




    public String getEtkinlikId() {
        return etkinlikId;
    }

    public void setEtkinlikId(String etkinlikId) {
        this.etkinlikId = etkinlikId;
    }


    public String getEtkinlikResmi() {
        return etkinlikResmi;
    }

    public void setEtkinlikResmi(String etkinlikResmi) {
        this.etkinlikResmi = etkinlikResmi;
    }



    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getEtkinlik_baslik() {
        return etkinlik_baslik;
    }

    public void setEtkinlik_baslik(String etkinlik_baslik) {
        this.etkinlik_baslik = etkinlik_baslik;
    }

    public String getEtkinlik_min() {
        return etkinlik_min;
    }

    public void setEtkinlik_min(String etkinlik_min) {
        this.etkinlik_min = etkinlik_min;
    }

    public String getEtkinlik_max() {
        return etkinlik_max;
    }

    public void setEtkinlik_max(String etkinlik_max) {
        this.etkinlik_max = etkinlik_max;
    }

    public String getEtkinlik_baslangic() {
        return etkinlik_baslangic;
    }

    public void setEtkinlik_baslangic(String etkinlik_baslangic) {
        this.etkinlik_baslangic = etkinlik_baslangic;
    }

    public String getEtkinlik_bitis() {
        return etkinlik_bitis;
    }

    public void setEtkinlik_bitis(String etkinlik_bitis) {
        this.etkinlik_bitis = etkinlik_bitis;
    }

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getEtkinlik_ilce() {
        return etkinlik_ilce;
    }

    public void setEtkinlik_ilce(String etkinlik_ilce) {
        this.etkinlik_ilce = etkinlik_ilce;
    }

    public String getEtkinlik_adres() {
        return etkinlik_adres;
    }

    public void setEtkinlik_adres(String etkinlik_adres) {
        this.etkinlik_adres = etkinlik_adres;
    }

    public String getEtkinlik_detay() {
        return etkinlik_detay;
    }

    public void setEtkinlik_detay(String etkinlik_detay) {
        this.etkinlik_detay = etkinlik_detay;
    }

    public String getEtkinlik_ucret() {
        return etkinlik_ucret;
    }

    public void setEtkinlik_ucret(String etkinlik_ucret) {
        this.etkinlik_ucret = etkinlik_ucret;
    }

    public Etkinlik() {

    }

    public Etkinlik(String etkinlikId, String gonderen, String etkinlikResmi, String etkinlikbaslik, String etkinlikkatilimmin
            , String etkinlikkatilimmax, String etkinlikbaslangic, String etkinlikbitis, String sehir
            , String etkinlikilce, String etkinlikadres, String etkinlikdetay, String etkinlikucret) {

        this.etkinlikId = etkinlikId;
        this.gonderen = gonderen;
        this.etkinlikResmi = etkinlikResmi;
        etkinlik_baslik = etkinlikbaslik;
        etkinlik_min = etkinlikkatilimmin;
        etkinlik_max = etkinlikkatilimmax;
        etkinlik_baslangic = etkinlikbaslangic;
        etkinlik_bitis = etkinlikbitis;
        this.sehir = sehir;
        etkinlik_ilce = etkinlikilce;
        etkinlik_adres = etkinlikadres;
        etkinlik_detay = etkinlikdetay;
        etkinlik_ucret = etkinlikucret;

    }

    public Etkinlik(String etkinlikkatilimmin , String etkinlikkatilimmax, String etkinlikilce, String etkinlikadres, String etkinlikdetay, String etkinlikucret) {


        etkinlik_min = etkinlikkatilimmin;
        etkinlik_max = etkinlikkatilimmax;
        etkinlik_ilce = etkinlikilce;
        etkinlik_adres = etkinlikadres;
        etkinlik_detay = etkinlikdetay;
        etkinlik_ucret = etkinlikucret;

    }








}
