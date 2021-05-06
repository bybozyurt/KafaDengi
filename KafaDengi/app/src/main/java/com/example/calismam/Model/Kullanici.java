package com.example.calismam.Model;

public class Kullanici {

    private String id;
    private String AdSoy;
    private String KullaniciAdi;
    private String bio;
    private String resimurl;



    public Kullanici() {

    }

    public Kullanici(String id, String adSoy, String kullaniciAdi, String bio, String resimurl) {
        this.id = id;
        this.AdSoy = adSoy;
        this.KullaniciAdi = kullaniciAdi;
        this.bio = bio;
        this.resimurl = resimurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdSoy() {
        return AdSoy;
    }

    public void setAdSoy(String adSoy) {
        AdSoy = adSoy;
    }

    public String getKullaniciAdi() {
        return KullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        KullaniciAdi = kullaniciAdi;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }
}
