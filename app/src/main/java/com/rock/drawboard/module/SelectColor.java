package com.rock.drawboard.module;

import java.io.Serializable;

/**
 * Created by 昌宜 on 2017/4/12.
 */

public class SelectColor implements Serializable{
    private int r;
    private int g;
    private int b;

    public SelectColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
