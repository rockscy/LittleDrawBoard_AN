package com.rock.drawboard.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.rock.drawboard.MyApplication;
import com.rock.drawboard.R;
import com.rock.drawboard.event.LoginEvent;
import com.rock.drawboard.module.DataPackage;
import com.rock.drawboard.module.Login;
import com.rock.drawboard.socket.ClientAction;
import com.rock.drawboard.ui.view.ClearEditText;
import com.rock.drawboard.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 昌宜 on 2017/3/24.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText mLoginEdt;
    private Button mLoginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mLoginEdt = ((ClearEditText) findViewById(R.id.login_user_edt));
        mLoginBtn = ((Button) findViewById(R.id.login_btn));
        mLoginBtn.setOnClickListener(this);
        findViewById(R.id.youke_btn).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册EventBus，首先判断是否进行了注册，如果没有注册则添加注册
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        //取消注册，如果已经注册了，则取消注册
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    private boolean isLogin;
    /**
     * 处理事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent userEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isLogin = true;
                ToastUtil.makeToastShort("链接成功");
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.youke_btn:
                MyApplication.isVisitor = true;
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.login_btn:
                MyApplication.isVisitor = false;
                String ip = mLoginEdt.getText().toString();
                if(TextUtils.isEmpty(ip)) {
                    ToastUtil.makeToastShort("ip地址不能为空~");
                    return;
                }
                new ClientAction(ip).start();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!isLogin) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.makeToastShort("链接失败，请检查IP地址");
                                }
                            });
                        }
                    }
                }, 3000);
                break;
        }
    }
}
