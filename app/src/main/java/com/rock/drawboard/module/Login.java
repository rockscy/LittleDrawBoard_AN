package com.rock.drawboard.module;

import java.io.Serializable;

/**
 * Created by 昌宜 on 2017/3/27.
 */

public class Login implements Serializable{
    private boolean isLogin;

    public Login(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
