package com.rock.drawboard.socket;

import android.util.Log;
import android.widget.Switch;

import com.rock.drawboard.constant.Config;
import com.rock.drawboard.constant.EventConfig;
import com.rock.drawboard.event.BoardEvent;
import com.rock.drawboard.event.LoginEvent;
import com.rock.drawboard.module.Client;
import com.rock.drawboard.module.DataPackage;
import com.rock.drawboard.module.Login;
import com.rock.drawboard.module.Point;
import com.rock.drawboard.utils.LogUtils;
import com.rock.drawboard.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by 昌宜 on 2017/3/20.
 */

public class ClientAction extends Thread{
    private static final String TAG = ClientAction.class.getSimpleName();
    private static Client client;
    private String ip;
    private int port = 0;
    public ClientAction(String ip,int port) {
        this.port = port;
        this.ip = ip;
    }
    public ClientAction(String ip) {
        this.ip = ip;
    }
    private void initSocket() {
        try {
        Socket socket;
        if(port==0) {
             socket = new Socket(ip, Config.PORT);
        }else {
             socket = new Socket(ip,port);
        }
            client = new Client(socket);

        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void run() {
        initSocket();
        if(client!=null) {
            while (!client.isClosed()) {
                try {
                    DataPackage dtpg = (DataPackage) client.receive();
                    Object data = dtpg.getData();
                    switch (dtpg.getType()) {
                        case LOGIN:
                            EventBus.getDefault().post(new LoginEvent(EventConfig.LGOIN));
                            break;
                        case POINT:
                            EventBus.getDefault().post(new BoardEvent(EventConfig.POINT,data));
                            break;
                        case COMMAND:
                            EventBus.getDefault().post(new BoardEvent(EventConfig.COMMAND,data));
                            break;
                        case COLOR:
                            EventBus.getDefault().post(new BoardEvent(EventConfig.COLOR,data));
                            break;
                        case STROKE:
                            EventBus.getDefault().post(new BoardEvent(EventConfig.STROKE,data));
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
            }
        }
    }
    public static void sendData(DataPackage.DataType type, Object data) {
        DataPackage dp = new DataPackage(type, data);
        sendDataPackage(dp);
    }

    public static void sendDataPackage(DataPackage dp) {
        if (client != null && !client.isClosed()) {
            try {
                client.send(dp);
            } catch (IOException e) {
                close();
            }
        }
    }

    public static void close() {
        if (client != null) {
            client.close();
            Log.e(TAG,"client close");
        }
    }
}
