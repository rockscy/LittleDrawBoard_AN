package com.rock.drawboard.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.rock.drawboard.Contral.BitmapCtl;
import com.rock.drawboard.Contral.BitmapUtil;
import com.rock.drawboard.Contral.Circlectl;
import com.rock.drawboard.Contral.EraserCtl;
import com.rock.drawboard.Contral.ISketchpadDraw;
import com.rock.drawboard.Contral.IUndoRedoCommand;
import com.rock.drawboard.Contral.LineCtl;
import com.rock.drawboard.Contral.OvaluCtl;
import com.rock.drawboard.Contral.PenuCtl;
import com.rock.drawboard.Contral.PlygonCtl;
import com.rock.drawboard.Contral.RectuCtl;
import com.rock.drawboard.Contral.Spraygun;
import com.rock.drawboard.MyApplication;
import com.rock.drawboard.R;
import com.rock.drawboard.constant.CommonDef;
import com.rock.drawboard.module.Command;
import com.rock.drawboard.module.DataPackage;
import com.rock.drawboard.module.Point;
import com.rock.drawboard.module.SelectColor;
import com.rock.drawboard.socket.ClientAction;
import com.rock.drawboard.ui.activity.MainActivity;
import com.rock.drawboard.utils.LogUtils;


import java.util.ArrayList;


public class SketchpadView extends View implements IUndoRedoCommand {

    //设置画笔常量
    public static final int STROKE_PEN = 12;       //画笔1
    public static final int STROKE_ERASER = 2;    //橡皮擦2
    public static final int STROKE_PLYGON = 10;   //多边形3
    public static final int STROKE_RECT = 9;      //矩形 4
    public static final int STROKE_CIRCLE = 8;    //圆 5
    public static final int STROKE_OVAL = 7;      //椭圆 6
    public static final int STROKE_LINE = 6;      //直线7
    public static final int STROKE_SPRAYGUN = 5;      //喷枪8
    public static final int UNDO_SIZE = 20;       //撤销栈的大小
    public static final int BITMAP_WIDTH = 650;        //画布高
    public static final int BITMAP_HEIGHT = 400;    //画布宽
    public static final int REDO = 201;  //redo
    public static final int UNDO = 202;  //undo
    public static final int CLEAR_BOARD = 203;  //清空面板
    public static final int ACTION_DOWN = 301;
    public static final int ACTION_MOVE = 302;
    public static final int ACTION_UP = 303;
    private int m_strokeType = STROKE_PEN;   //画笔风格
    private static int m_strokeColor = Color.BLACK;   //画笔颜色
    private static int m_penSize = CommonDef.SMALL_PEN_WIDTH;         //画笔大小
    private static int m_eraserSize = CommonDef.LARGE_ERASER_WIDTH;   //橡皮擦大小

    //实例新画布
    private boolean m_isEnableDraw = true;   //标记是否可以画
    private boolean m_isDirty = false;     //标记
    private boolean m_isTouchUp = false;    //标记是否鼠标弹起
    private boolean m_isSetForeBmp = false;   //标记是否设置了前bitmap
    private int m_bkColor = Color.WHITE;    //背景色

    private int m_canvasWidth = 100;    //画布宽
    private int m_canvasHeight = 100;    //画布高
    private boolean m_canClear = true;   //标记是否可清除

    private Bitmap m_foreBitmap = null;     //用于显示的bitmap
    private Bitmap m_tempForeBitmap = null; //用于缓冲的bitmap
    private Bitmap m_bkBitmap = null;       //用于背后画的bitmap

    private Canvas m_canvas;     //画布
    private Paint m_bitmapPaint = null;   //画笔
    private SketchPadUndoStack m_undoStack = null;//栈存放执行的操作
    private ISketchpadDraw m_curTool = null;   //记录操作的对象画笔类

    boolean myLoop = false;// 喷枪结束标识符
    private Bitmap bgBitmap = null;


    ///////////////// paint and Bk//////////////////////////////
    //画布参数设计
    public boolean isDirty() {
        return m_isDirty;   //
    }

    public void setBkColor(int color) {   //设置背景颜色
        if (m_bkColor != color) {
            m_bkColor = color;
            invalidate();
        }
    }

    public static void setStrokeSize(int size, int type) {   //设置画笔的大小和橡皮擦大小
        switch (type) {
            case STROKE_PEN:
                m_penSize = size;
                break;

            case STROKE_ERASER:
                m_eraserSize = size;
                break;
        }
    }

    public static void setStrokeColor(int color) {   //设置画笔颜色
        m_strokeColor = color;
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        sendDataToPc(DataPackage.DataType.COLOR,new SelectColor(red,green,blue));
    }

    public static int getStrokeSize() {   //得到画笔的大小
        return m_penSize;
    }

    public static int getEraser() {   //得到橡皮擦的大小
        return m_eraserSize;
    }

    public static int getStrokeColor() {   //得到画笔的大小
        return m_strokeColor;
    }

    ////////////////////////////////////////////////////////////
    public void clearAllStrokes() {   //清空设置
        if (m_canClear) {
            // 清空撤销栈
            m_undoStack.clearAll();
            // 设置当前的bitmap对象为空
            if (null != m_tempForeBitmap) {
                m_tempForeBitmap.recycle();
                m_tempForeBitmap = null;
            }
            // Create a new fore bitmap and set to canvas.
            createStrokeBitmap(m_canvasWidth, m_canvasHeight);

            invalidate();
            m_isDirty = true;
            m_canClear = false;
            sendDataToPc(DataPackage.DataType.COMMAND,new Command(CLEAR_BOARD));
        }
    }
    protected void createStrokeBitmap(int w, int h) {
        m_canvasWidth = w;
        m_canvasHeight = h;
        Bitmap bitmap = Bitmap.createBitmap(m_canvasWidth, m_canvasHeight, Bitmap.Config.ARGB_8888);
        if (null != bitmap) {
            m_foreBitmap = bitmap;
            // Set the fore bitmap to m_canvas to be as canvas of strokes.
            m_canvas.setBitmap(m_foreBitmap);
        }
    }

    protected void setTempForeBitmap(Bitmap tempForeBitmap) {
        if (null != tempForeBitmap) {
            if (null != m_foreBitmap) {
                m_foreBitmap.recycle();
            }
            m_foreBitmap = BitmapCtl.duplicateBitmap(tempForeBitmap);
            if (null != m_foreBitmap && null != m_canvas) {
                m_canvas.setBitmap(m_foreBitmap);
                invalidate();
            }
        }
    }

    protected void setCanvasSize(int width, int height) {//设置画布大小
        if (width > 0 && height > 0) {
            if (m_canvasWidth != width || m_canvasHeight != height) {
                m_canvasWidth = width;
                m_canvasHeight = height;
                createStrokeBitmap(m_canvasWidth, m_canvasHeight);
            }
        }
    }

    //初始化数据   调用
    protected void initialize() {
        m_canvas = new Canvas();//实例画布用于整个绘图操作
        m_bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  //实例化画笔用于bitmap设置画布canvas
        m_undoStack = new SketchPadUndoStack(this, UNDO_SIZE);//实例化队列
    }

    //启动设置画笔的颜色和大小    调用修改
    private int command = STROKE_PEN;
    public void setStrokeType(int type) {
        command = type;
        sendDataToPc(DataPackage.DataType.COMMAND,new Command(type));
        m_strokeColor = SketchpadView.getStrokeColor();
        m_penSize = SketchpadView.getStrokeSize();
        switch (type) {
            case STROKE_PEN:
                m_curTool = new PenuCtl(m_penSize, m_strokeColor);
                break;

            case STROKE_ERASER:
                m_curTool = new EraserCtl(m_eraserSize);
                break;
            case STROKE_PLYGON:
                m_curTool = new PlygonCtl(m_penSize, m_strokeColor);
                break;
            case STROKE_RECT:
                m_curTool = new RectuCtl(m_penSize, m_strokeColor);
                break;
            case STROKE_CIRCLE:
                m_curTool = new Circlectl(m_penSize, m_strokeColor);
                break;
            case STROKE_OVAL:
                m_curTool = new OvaluCtl(m_penSize, m_strokeColor);
                break;
            case STROKE_LINE:
                m_curTool = new LineCtl(m_penSize, m_strokeColor);
                break;
            case STROKE_SPRAYGUN:
                m_curTool = new Spraygun(m_penSize, m_strokeColor);
                break;
        }
        //用于记录操作动作名称
        m_strokeType = type;
    }

    public SketchpadView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initialize();
    }

    public SketchpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgBitmap = ((BitmapDrawable) (getResources()
                .getDrawable(R.mipmap.pic1))).getBitmap();
        // TODO Auto-generated constructor stub
        initialize();
    }

    public SketchpadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initialize();
    }

    public boolean canRedo() {
        // TODO Auto-generated method stub
        if (null != m_undoStack) {
            return m_undoStack.canUndo();
        }
        return false;
    }

    public boolean canUndo() {
        // TODO Auto-generated method stub
        if (null != m_undoStack) {
            return m_undoStack.canRedo();
        }
        return false;
    }

    public void onDeleteFromRedoStack() {
        // TODO Auto-generated method stub
    }

    public void onDeleteFromUndoStack() {
        // TODO Auto-generated method stub
    }

    public void redo() {
        // TODO Auto-generated method stub
        if (null != m_undoStack) {
            m_undoStack.redo();
        }
    }

    public void undo() {
        // TODO Auto-generated method stub
        if (null != m_undoStack) {
            m_undoStack.undo();
        }
    }

    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (null != m_bkBitmap) {
            RectF dst = new RectF(getLeft(), getTop(), getRight(), getBottom());
            Rect rst = new Rect(0, 0, m_bkBitmap.getWidth(), m_bkBitmap.getHeight());
            canvas.drawBitmap(m_bkBitmap, rst, dst, m_bitmapPaint);
        }
        if (null != m_foreBitmap) {
            canvas.drawBitmap(m_foreBitmap, 0, 0, m_bitmapPaint);
        }
        if (null != m_curTool) {
            if (STROKE_ERASER != m_strokeType) {
                if (!m_isTouchUp) {   //调用绘图功能
                    m_curTool.draw(canvas);
                }
            }
        }
    }

    private int oldWidth;
    private int oldHeight;
    private final int CONFIRM_WIDGHT = 898;
    private final int CONFIRM_HEIGHT = 412;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        //取得view原始宽高
        oldWidth = w>oldWidth ? w : oldWidth;
        oldHeight = h>oldHeight ? h: oldHeight;
        setMeasuredDimension(CONFIRM_WIDGHT,CONFIRM_HEIGHT);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        LogUtils.e("onSizeChanged","width: "+w+"height:　"+h);
        //根据原始大小进行缩放
        float scaleX = (float) oldWidth/(float) w;
        float scaleY = (float) oldHeight/(float) h;
        setScaleX(scaleX);
        setScaleY(scaleY);
        //缩放后平移到原始位置
        setTranslationX(-(oldWidth-w)/2);
        setTranslationY((oldHeight-h)/2);
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        if (!m_isSetForeBmp) {
            setCanvasSize(w, h);
        }
        m_canvasWidth = w;
        m_canvasHeight = h;
        m_isSetForeBmp = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (m_isEnableDraw)   //判断是否可绘图
        {
            m_isTouchUp = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendDataToPc(DataPackage.DataType.POINT, new Point(x, y, ACTION_DOWN));
                   action_down(x,y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    sendDataToPc(DataPackage.DataType.POINT, new Point(x, y, ACTION_MOVE));
                   action_move(x,y);
                    break;
                case MotionEvent.ACTION_UP:
                    sendDataToPc(DataPackage.DataType.POINT, new Point(x, y, ACTION_UP));
                   action_up(x,y);
                    break;
            }
        }
        return true;
    }
    public void action_down(int x,int y) {
        //根据m_strokeType进行重新生成对象且记录下操作对象
        setStrokeType(m_strokeType);
        m_curTool.touchDown(x, y);
        invalidate();
    }
    public void action_move(int x,int y) {
        m_curTool.touchMove(x, y);
        //若果当前操作为橡皮擦或喷枪则调用绘图操作
        if (STROKE_ERASER == m_strokeType) {
            m_curTool.draw(m_canvas);
        }
        if (STROKE_SPRAYGUN == m_strokeType) {
            m_curTool.draw(m_canvas);
        }
        invalidate();
        m_isDirty = true;
        m_canClear = true;
    }
    public void action_up(int x,int y) {
        m_isTouchUp = true;
        if (m_curTool.hasDraw()) {
            // Add to undo stack.
            m_undoStack.push(m_curTool);
        }
        m_curTool.touchUp(x, y);
        // Draw strokes on bitmap which is hold by m_canvas.
        m_curTool.draw(m_canvas);
        invalidate();
        m_isDirty = true;
        m_canClear = true;
        myLoop = false;
    }
    public static void sendDataToPc(DataPackage.DataType type,Object object) {
        if(!MyApplication.isVisitor) {
            ClientAction.sendData(type,object);
        }
    }

    ////////////////////undo栈/////////////////////
    public class SketchPadUndoStack {
        private int m_stackSize = 0;   //栈大小
        private SketchpadView m_sketchPad = null;  //视图对象
        private ArrayList<ISketchpadDraw> m_undoStack = new ArrayList<ISketchpadDraw>();
        private ArrayList<ISketchpadDraw> m_redoStack = new ArrayList<ISketchpadDraw>();
        private ArrayList<ISketchpadDraw> m_removedStack = new ArrayList<ISketchpadDraw>();

        public SketchPadUndoStack(SketchpadView sketchPad, int stackSize) {
            m_sketchPad = sketchPad;
            m_stackSize = stackSize;
        }

        public void push(ISketchpadDraw sketchPadTool) {
            if (null != sketchPadTool) {
                if (m_undoStack.size() == m_stackSize && m_stackSize > 0) {
                    ISketchpadDraw removedTool = m_undoStack.get(0);
                    m_removedStack.add(removedTool);
                    m_undoStack.remove(0);
                }
                m_undoStack.add(sketchPadTool);
            }
        }

        //清空栈
        public void clearAll() {
            m_redoStack.clear();
            m_undoStack.clear();
            m_removedStack.clear();
        }

        public void undo() {
            if (canUndo() && null != m_sketchPad) {
                Log.i("sada022", "undo点击");
                ISketchpadDraw removedTool = m_undoStack.get(m_undoStack.size() - 1);
                m_redoStack.add(removedTool);
                m_undoStack.remove(m_undoStack.size() - 1);

                if (null != m_tempForeBitmap) {
                    // Set the temporary fore bitmap to canvas.
                    m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
                } else {
                    // Create a new bitmap and set to canvas.
                    m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
                }
                Canvas canvas = m_sketchPad.m_canvas;
                // First draw the removed tools from undo stack.
                for (ISketchpadDraw sketchPadTool : m_removedStack) {
                    sketchPadTool.draw(canvas);
                }
                for (ISketchpadDraw sketchPadTool : m_undoStack) {
                    sketchPadTool.draw(canvas);
                }
                sendDataToPc(DataPackage.DataType.COMMAND,new Command(UNDO));
                m_sketchPad.invalidate();
            }
        }

        public void redo() {
            if (canRedo() && null != m_sketchPad) {
                ISketchpadDraw removedTool = m_redoStack.get(m_redoStack.size() - 1);
                m_undoStack.add(removedTool);
                m_redoStack.remove(m_redoStack.size() - 1);

                if (null != m_tempForeBitmap) {
                    // Set the temporary fore bitmap to canvas.
                    m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
                } else {
                    // Create a new bitmap and set to canvas.
                    m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
                }
                Canvas canvas = m_sketchPad.m_canvas;

                // First draw the removed tools from undo stack.
                for (ISketchpadDraw sketchPadTool : m_removedStack) {
                    sketchPadTool.draw(canvas);
                }
                for (ISketchpadDraw sketchPadTool : m_undoStack) {
                    sketchPadTool.draw(canvas);
                }
                sendDataToPc(DataPackage.DataType.COMMAND,new Command(REDO));
                m_sketchPad.invalidate();
            }
        }

        public boolean canUndo() {//
            return (m_undoStack.size() > 0);
        }

        public boolean canRedo() {//判断栈的大小
            return (m_redoStack.size() > 0);
        }

    }

}