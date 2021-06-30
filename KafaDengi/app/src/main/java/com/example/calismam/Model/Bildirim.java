package com.example.calismam.Model;

public class Bildirim {
    private String kullaniciId;
    private String text;
    private String etkinlikId;
    private boolean ispost;

    public Bildirim(String kullaniciId, String text, String etkinlikId, boolean etkinlik) {
        this.kullaniciId = kullaniciId;
        this.text = text;
        this.etkinlikId = etkinlikId;
        this.ispost = etkinlik;
    }

    public Bildirim() {
    }

    public String getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEtkinlikId() {
        return etkinlikId;
    }

    public void setEtkinlikId(String etkinlikId) {
        this.etkinlikId = etkinlikId;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
