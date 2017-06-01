package com.rock.drawboard.module;

import java.io.Console;
import java.io.Serializable;

/**
 * Created by 石昌宜 on 2017/3/21.
 */
@SuppressWarnings("serial")
public class Point implements Serializable {
    private int x;
    private int y;
    private int state;

    public Point(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", state=" + state +
                '}';
    }

}
