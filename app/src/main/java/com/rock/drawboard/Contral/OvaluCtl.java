package com.rock.drawboard.Contral;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class OvaluCtl implements ISketchpadDraw{
	private Paint mPaint=new Paint();
    private boolean m_hasDrawn = false; 
    private RectF rectf=new RectF();
    public OvaluCtl(int penSize, int penColor)
    {
    	mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(penColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(penSize);
    }
	
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (null != canvas)
        {
			canvas.drawOval(rectf, mPaint); 
        }
	}
	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return m_hasDrawn;
		//return false;
	}	
	public void cleanAll() {
		// TODO Auto-generated method stub	
	}	
	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		rectf.left=x;
		rectf.top=y;
		rectf.right=x;
		rectf.bottom=y;
	}
	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		rectf.right=x;
		rectf.bottom=y;
		m_hasDrawn=true;
	}	
	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		rectf.right=x;
		rectf.bottom=y;
	}

}
