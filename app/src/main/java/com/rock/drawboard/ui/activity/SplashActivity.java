package com.rock.drawboard.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import androidx.annotation.Nullable;

import com.rock.drawboard.R;
import com.rock.drawboard.socket.ClientAction;

/**
 * Created by 昌宜 on 2017/3/24.
 */

public class SplashActivity extends BaseActivity{
    private static final int GO_LOGIN = 0;
    private static final int GO_MAIN = 1;
    private static final int DELAY = 2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(GO_LOGIN, DELAY);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_LOGIN:
                    goActivity(LoginActivity.class);
                    break;
                case GO_MAIN:
                    goActivity(MainActivity.class);
                    break;
            }
        }
    };
    private void goActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);
        this.finish();
    }
}
