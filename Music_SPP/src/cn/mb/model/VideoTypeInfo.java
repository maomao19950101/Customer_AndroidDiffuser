package cn.mb.model;

import java.io.Serializable;

public class VideoTypeInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String typeImg;
	private String typeName;
	private String descImg;
	private String desc;
	private int sessionCount;
	private String timeTotal;
	public String getTypeImg() {
		return typeImg;
	}
	public void setTypeImg(String typeImg) {
		this.typeImg = typeImg;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDescImg() {
		return descImg;
	}
	public void setDescImg(String descImg) {
		this.descImg = descImg;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getSessionCount() {
		return sessionCount;
	}
	public void setSessionCount(int sessionCount) {
		this.sessionCount = sessionCount;
	}
	public String getTimeTotal() {
		return timeTotal;
	}
	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}
	
	
}
