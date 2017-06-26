package cn.mb.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class VideoClassifyModel implements Serializable {

	private static final long serialVersionUID = -7061879590071178655L;

	@SerializedName("ID")
	private String classifyId;
	
	@SerializedName("CategoryName")
	private String classifyName;

	@SerializedName("Sum")
	private String sessionCount;

	@SerializedName("Img")
	private String imgUrl;
	
    @SerializedName("Imgs")
    private String imgUrl2;
	

	@SerializedName("Time")
	private String totalTime;

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getSessionCount() {
		return sessionCount;
	}

	public void setSessionCount(String sessionCount) {
		this.sessionCount = sessionCount;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	
	

}
