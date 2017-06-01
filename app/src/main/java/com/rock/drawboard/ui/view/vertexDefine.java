package com.rock.drawboard.ui.view;

public class vertexDefine {


	private float x;
	private float y;
	
	public vertexDefine(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public vertexDefine() {
		this(0,0);
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setPoint(float x,float y) {
		this.x = x;
		this.y = y;
	}	
}
