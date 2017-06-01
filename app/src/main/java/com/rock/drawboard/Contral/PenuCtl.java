package com.rock.drawboard.Contral;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class PenuCtl implements ISketchpadDraw{

	private Path m_path=new Path();
	private Paint m_paint=new Paint();
	private boolean m_hasDrawn = false;
	private float mX=0, mY=0;
	private static final float TOUCH_TOLERANCE = 4;
	
	public PenuCtl(int penSize, int penColor)
	{
		m_paint.setAntiAlias(true);
		m_paint.setDither(true);
		m_paint.setColor(penColor);
		m_paint.setStyle(Paint.Style.STROKE);
		m_paint.setStrokeJoin(Paint.Join.ROUND);
		m_paint.setStrokeCap(Paint.Cap.ROUND);
		m_paint.setStrokeWidth(penSize);
	}
	
	public void cleanAll() {
		// TODO Auto-generated method stub
		m_path.reset();
	}
	
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
//		m_paint.setColor(SketchpadView.getStrokeColor());
//		m_paint.setStrokeWidth(SketchpadView.getStrokeSize());
		if (null != canvas)
		{
			//canvas.drawPoint(mX, mY,m_paint);
			canvas.drawPath(m_path, m_paint);
		}
	}
	
	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return m_hasDrawn;
	}
	
	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		m_path.moveTo(x, y);
		m_path.lineTo(x, y);
        mX = x;
        mY = y;
	}
	
	public void touchMove(float x, float y) {
		// TODO Auto-generated method stub
		float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            m_path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            m_hasDrawn = true;
            mX = x;
            mY = y;
            m_path.lineTo(x, y);
        }
	}
	
	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		m_path.lineTo(mX, mY);
		mX=x;
		mY=y;
	}
}
