package com.rock.drawboard.ui.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.rock.drawboard.Contral.BitmapUtil;
import com.rock.drawboard.Contral.PlygonCtl;
import com.rock.drawboard.R;
import com.rock.drawboard.constant.EventConfig;
import com.rock.drawboard.event.BoardEvent;
import com.rock.drawboard.event.LoginEvent;
import com.rock.drawboard.module.Command;
import com.rock.drawboard.module.DataPackage;
import com.rock.drawboard.module.Point;
import com.rock.drawboard.module.SelectColor;
import com.rock.drawboard.module.StrokeWidth;
import com.rock.drawboard.socket.ClientAction;
import com.rock.drawboard.ui.view.SketchpadView;
import com.rock.drawboard.utils.LogUtils;
import com.rock.drawboard.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView m_pen,m_undo,m_redo,m_eraser,m_rect,m_cycle,m_line,m_color;
    private SeekBar seekBar;
    private boolean isExit;
    private SketchpadView m_view;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_view = (SketchpadView) this.findViewById(R.id.SketchadView);
        m_pen = (ImageView) this.findViewById(R.id.buttonpen_ID);
        m_undo = (ImageView) this.findViewById(R.id.buttonundo_ID);
        m_redo = (ImageView) this.findViewById(R.id.buttonredo_ID);
        m_eraser = (ImageView) this.findViewById(R.id.buttoneraser_ID);
        m_rect = (ImageView) this.findViewById(R.id.buttonrect_ID);
        m_cycle = (ImageView) this.findViewById(R.id.buttoncycle_ID);
        m_line = (ImageView) this.findViewById(R.id.buttonline_ID);
        m_color = (ImageView) this.findViewById(R.id.buttoncolor_ID);
        findViewById(R.id.buttonclear_ID).setOnClickListener(this);
        findViewById(R.id.buttonabout_ID).setOnClickListener(this);
        m_pen.setOnClickListener(this);
        m_undo.setOnClickListener(this);
        m_redo.setOnClickListener(this);
        m_eraser.setOnClickListener(this);
        m_rect.setOnClickListener(this);
        m_cycle.setOnClickListener(this);
        m_line.setOnClickListener(this);
        m_color.setOnClickListener(this);
        SeekBar seekBarEa = (SeekBar) findViewById(R.id.seekBar_ea);
        seekBarEa.setMax(4);
        seekBarEa.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                switch (seekBar.getProgress()) {
                    case 0:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(0,SketchpadView.STROKE_ERASER));
                        SketchpadView.setStrokeSize(15, SketchpadView.STROKE_ERASER);
                        break;
                    case 1:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(1,SketchpadView.STROKE_ERASER));
                        SketchpadView.setStrokeSize(20, SketchpadView.STROKE_ERASER);
                        break;
                    case 2:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(2,SketchpadView.STROKE_ERASER));
                        SketchpadView.setStrokeSize(30, SketchpadView.STROKE_ERASER);
                        break;
                    case 3:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(3,SketchpadView.STROKE_ERASER));
                        SketchpadView.setStrokeSize(50, SketchpadView.STROKE_ERASER);
                        break;
                    case 4:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(4,SketchpadView.STROKE_ERASER));
                        SketchpadView.setStrokeSize(100, SketchpadView.STROKE_ERASER);
                        break;
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(4);
        //进度条事件监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                switch (seekBar.getProgress()) {
                    case 0:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(0,SketchpadView.STROKE_PEN));
                        SketchpadView.setStrokeSize(5, SketchpadView.STROKE_PEN);
                        break;
                    case 1:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(1,SketchpadView.STROKE_PEN));
                        SketchpadView.setStrokeSize(8, SketchpadView.STROKE_PEN);
                        break;
                    case 2:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(2,SketchpadView.STROKE_PEN));
                        SketchpadView.setStrokeSize(10, SketchpadView.STROKE_PEN);
                        break;
                    case 3:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(3,SketchpadView.STROKE_PEN));
                        SketchpadView.setStrokeSize(15, SketchpadView.STROKE_PEN);
                        break;
                    case 4:
                        SketchpadView.sendDataToPc(DataPackage.DataType.STROKE,new StrokeWidth(4,SketchpadView.STROKE_PEN));
                        SketchpadView.setStrokeSize(20, SketchpadView.STROKE_PEN);
                        break;
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonpen_ID:
                OnPenClick(v);
                break;
            case R.id.buttonundo_ID:
                OnUndoClick(v);
                break;
            case R.id.buttoneraser_ID:
                OnEraserClick(v);
                break;
            case R.id.buttonredo_ID:
                onRedoClick(v);
                break;
            case R.id.buttonrect_ID:
                OnRectClick(v);
                break;
            case R.id.buttoncycle_ID:
                OnCycleClick(v);
                break;
            case R.id.buttonline_ID:
                OnLineClick(v);
                break;
            case R.id.buttoncolor_ID:
                OnColorClick(v);
                break;
            case R.id.buttonclear_ID:
                m_view.clearAllStrokes();
                break;
            case R.id.buttonabout_ID:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (isExit) {
                this.finish();
            } else {
                isExit = true;
                ToastUtil.makeToastShort("在按一次退出");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientAction.close();
    }



    private void OnLineClick(View v) {
        m_view.setStrokeType(m_view.STROKE_LINE);
    }

    private void OnCycleClick(View v) {
        m_view.setStrokeType(m_view.STROKE_CIRCLE);
    }

    private void OnRectClick(View v) {
        m_view.setStrokeType(m_view.STROKE_RECT);
    }

    private void OnEraserClick(View v) {
        //设置橡皮擦的类型
        m_view.setStrokeType(m_view.STROKE_ERASER);
    }
    private void onRedoClick(View v) {
        m_view.redo();//响应redo事件
    }

    private void OnUndoClick(View v) {
        //响应undo事件
        m_view.undo();
    }

    private void OnPenClick(View v) {
        m_view.setStrokeType(m_view.STROKE_PEN);//设置画笔的类型
    }
    private void OnColorClick(View v) {
        //跳转到GridViewDemoActivity
        Intent intent = new Intent(MainActivity.this, GridViewColorActivity.class);
        MainActivity.this.startActivity(intent);
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
    @Subscribe
    public void onEvent(final BoardEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Object object = event.getObject();
                    switch (event.what) {
                        case EventConfig.POINT:
                            Point point = (Point) object;
                            handlePoint(point);
                            break;
                        case EventConfig.COMMAND:
                            Command command = (Command) object;
                            handleCommand(command);
                            break;
                        case EventConfig.COLOR:
                            SelectColor color = (SelectColor) object;
                            handleColor(color);
                            break;
                        case EventConfig.STROKE:
                            StrokeWidth strokeWidth = (StrokeWidth) object;
                            handleStroke(strokeWidth);
                            break;
                    }
                }catch (Exception e) {
                    LogUtils.e(TAG,"handle board event fail");
                }

            }
        });
    }

    private void handleStroke(StrokeWidth strokeWidth) {
        int type = strokeWidth.getType();
        int width = strokeWidth.getStroke();
        switch (type) {
            case SketchpadView.STROKE_PEN:
                SketchpadView.setStrokeSize(width, SketchpadView.STROKE_PEN);
                break;
            case SketchpadView.STROKE_ERASER:
                SketchpadView.setStrokeSize(width, SketchpadView.STROKE_ERASER);
                break;
        }
    }


    private void handlePoint(Point point) {
        int x = point.getX();
        int y = point.getY();
        switch (point.getState()) {
            case SketchpadView.ACTION_DOWN:
                m_view.action_down(x,y);
                break;
            case SketchpadView.ACTION_MOVE:
                m_view.action_move(x,y);
                break;
            case SketchpadView.ACTION_UP:
                m_view.action_up(x,y);
                break;
        }
    }
    private void handleCommand(Command command) {
        switch (command.getType()) {
            case SketchpadView.UNDO:
                m_view.undo();
                break;
            case SketchpadView.REDO:
                m_view.redo();
                break;
            case SketchpadView.CLEAR_BOARD:
                m_view.clearAllStrokes();
                break;
            default:
                m_view.setStrokeType(command.getType());
                break;
        }
    }
    private void handleColor(SelectColor color) {
        int rgb = Color.rgb(color.getR(), color.getG(), color.getB());
        m_view.setStrokeColor(rgb);
    }


}