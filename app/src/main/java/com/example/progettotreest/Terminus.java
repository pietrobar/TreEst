package com.example.progettotreest;

public class Terminus {
    private final String sname;
    private final int did;

    public Terminus(String sname, int did) {
        this.sname = sname;
        this.did = did;
    }

    public String getSname() {
        return sname;
    }

    public int getDid() {
        return did;
    }
}
