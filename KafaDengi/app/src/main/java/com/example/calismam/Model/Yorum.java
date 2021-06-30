package com.example.calismam.Model;

public class Yorum {

    private String yorum;
    private String gonderen;
    private String yorumid;


    public Yorum() {

    }

    public Yorum(String yorum, String gonderen, String yorumid) {
        this.yorum = yorum;
        this.gonderen = gonderen;
        this.yorumid = yorumid;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getYorumid() {
        return yorumid;
    }

    public void setYorumid(String yorumid) {
        this.yorumid = yorumid;
    }
}
