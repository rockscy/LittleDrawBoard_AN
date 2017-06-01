package com.rock.drawboard.Contral;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;


import java.util.Random;


/**function:喷枪功能：在点击点随机的取个圆，
 * 在圆里随机的取指定的点，
 * 然后将选取的点画到画布上，即实现喷枪功能
 */
public class Spraygun implements ISketchpadDraw{
	public float touchX;
	public float touchY;
	public int m_penSize;
	public int m_strokeColor;
	private Path path;
	private Paint paint ;
	private boolean m_hasDrawn = false;
	public Spraygun( int m_penSize,int m_strokeColor) {
		super();
		path = new Path();
		this.m_penSize = m_penSize;
		this.m_strokeColor = m_strokeColor;
		path.moveTo(touchX, touchY);
		path.lineTo(touchX, touchY);
		paint = new Paint();
		// 抗锯齿........
		paint.setAntiAlias(true);
		// 防抖动.......
		paint.setDither(true);
		paint.setColor(m_strokeColor);
		paint.setStrokeWidth(1);
	}

	public void draw(Canvas canvas) {
		canvas.drawPath(path, paint);
		randomPoint(canvas, paint, touchX, touchY, m_penSize);
	}

	public boolean hasDraw() {
		return m_hasDrawn;
	}

	public void cleanAll() {
		path.reset();
	}

	public void touchDown(float x, float y) {
		touchX = x;
		touchY = y;
	}

	public void touchMove(float mx, float my) {
		touchX = mx;
		touchY = my;
		path.moveTo(mx, my);
		m_hasDrawn = true;
	}

	public void touchUp(float x, float y) {
		touchX = x;
		touchY = y;
	}

	public void randomPoint(Canvas canvas, Paint paint, float x, float y,
							int size) {
		// 定义一个随机类
		Random random = new Random();
		// 随机的取50个点
		for (int i = 0; i < 160; i++) {
			// 随机的取[0,1)之间的点
			float tempX1 = random.nextFloat();
			float tempY1 = random.nextFloat();
			// 随机的取[0,size*10)之间的点
			int tempX2 = random.nextInt(size * 2);
			int tempY2 = random.nextInt(size * 2);
			float tempX = tempX1 + tempX2;
			float tempY = tempY1 + tempY2;
			// 取5的摸，在点击点四周都能画到点
			if (i % 4 == 0) {
				tempX = (tempX1 + tempX2) * (-1);
				tempY = (tempY1 + tempY2) * (-1);
			} else if (i % 4 == 1) {
				tempY = (tempY1 + tempY2) * (-1);
			} else if (i % 4 == 2) {
				tempX = (tempX1 + tempX2) * (-1);
			}
			/*
			 * 判断所取点到点击点之间的距离，只有在距离小于半径时才画出改点 *圆的半径取 size*10
			 */
			double sqrt = Math.sqrt(tempX * tempX + tempY * tempY);
			if (sqrt < size * 2) {
				// 画点
				canvas.drawPoint(tempX + x - size * 2, tempY + y - size * 2,
						paint);
				i++;
			}
		}
	}

}

