package com.example.progettotreest;

import java.io.Serializable;

public class Line implements Serializable {
    private final Terminus terminus1;
    private final Terminus terminus2;

    public Line(Terminus terminus1, Terminus terminus2) {
        this.terminus1 = terminus1;
        this.terminus2 = terminus2;
    }

    public Terminus getTerminus1() {
        return terminus1;
    }

    public Terminus getTerminus2() {
        return terminus2;
    }


    public String getNameBasedOnDid(int did) {
        if (this.getTerminus1().getDid()==did){
            return getTerminus2().getSname() + " - " + getTerminus1().getSname();
        }else {
            return getTerminus1().getSname() + " - " + getTerminus2().getSname();
        }
    }
    //t1   t2 => did = 1 -> t2 - t1

    public String getBegin(int did){
        return getTerminus1().getDid()==did ? getTerminus2().getSname():getTerminus1().getSname();
    }

    public String getArrive(int did){
        return getTerminus2().getDid()==did ? getTerminus2().getSname():getTerminus1().getSname();
    }

}
