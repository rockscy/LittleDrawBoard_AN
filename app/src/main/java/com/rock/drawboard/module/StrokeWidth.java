package com.rock.drawboard.module;

import java.io.Serializable;

/**
 * Created by 昌宜 on 2017/4/12.
 */

public class StrokeWidth implements Serializable{
    private int stroke;
    private int type;
    public StrokeWidth(int stroke,int type) {
        this.stroke = stroke;
        this.type = type;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
