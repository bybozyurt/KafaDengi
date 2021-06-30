package com.example.calismam.Model;

public class Sohbet {

    String message, alan, gonderen, timestamp;
    boolean goruldu;

    public Sohbet() {
    }

    public Sohbet(String mesaj, String alan, String gondoren, String zaman, boolean goruldu) {
        this.message = mesaj;
        this.alan = alan;
        this.gonderen = gondoren;
        this.timestamp = zaman;
        this.goruldu = goruldu;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlan() {
        return alan;
    }

    public void setAlan(String alan) {
        this.alan = alan;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isGoruldu() {
        return goruldu;
    }

    public void setGoruldu(boolean goruldu) {
        this.goruldu = goruldu;
    }
}
