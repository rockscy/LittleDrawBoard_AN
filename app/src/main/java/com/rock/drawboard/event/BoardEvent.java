package com.rock.drawboard.event;

/**
 * Created by 昌宜 on 2017/4/19.
 */

public class BoardEvent extends BaseEvent{
    private Object object;

    public BoardEvent(int what, Object object) {
        super(what);
        this.object = object;
    }

    public BoardEvent(int what) {
        super(what);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
