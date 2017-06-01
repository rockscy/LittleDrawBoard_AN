package com.rock.drawboard.module;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataPackage implements Serializable {
	public enum DataType {LOGIN,POINT,COMMAND,COLOR,STROKE}
	
	private DataType type;
	private Object data;
	
	public DataPackage(DataType type) {
		this(type, null);
	}
	
	public DataPackage(DataType type, Object data) {
		this.type = type;
		this.data = data;
	}
	
	public DataType getType() {
		return type;
	}

	public Object getData() {
		return data;
	}
}
