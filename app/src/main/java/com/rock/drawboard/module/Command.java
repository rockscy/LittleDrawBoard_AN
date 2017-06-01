package com.rock.drawboard.module;

import java.io.Serializable;

/**
 * Created by 昌宜 on 2017/4/11.
 */

public class Command implements Serializable{
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Command(int type) {

        this.type = type;
    }
}
