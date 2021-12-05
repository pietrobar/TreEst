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


}
