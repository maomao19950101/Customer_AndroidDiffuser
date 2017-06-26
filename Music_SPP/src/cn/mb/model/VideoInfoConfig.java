package cn.mb.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class VideoInfoConfig implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5042952856137022227L;
   
    private String id;
    
    @SerializedName("fogTime")
    private int mistTime;
    
    @SerializedName("lampTime")
    private int lightWorkTime;
    
    @SerializedName("colourA")
    private String rgb1;
    
    @SerializedName("colourB")
    private String rgb2;
    
    @SerializedName("colourC")
    private String rgb3;
    
    @SerializedName("status")
    private int rgbModel;

    public int getMistTime() {
        return mistTime;
    }

    public void setMistTime(int mistTime) {
        this.mistTime = mistTime;
    }

    public int getLightWorkTime() {
        return lightWorkTime;
    }

    public void setLightWorkTime(int lightWorkTime) {
        this.lightWorkTime = lightWorkTime;
    }

    public String getRgb1() {
        return rgb1;
    }

    public void setRgb1(String rgb1) {
        this.rgb1 = rgb1;
    }

    public String getRgb2() {
        return rgb2;
    }

    public void setRgb2(String rgb2) {
        this.rgb2 = rgb2;
    }

    public String getRgb3() {
        return rgb3;
    }

    public void setRgb3(String rgb3) {
        this.rgb3 = rgb3;
    }

    public int getRgbModel() {
        return rgbModel;
    }

    public void setRgbModel(int rgbModel) {
        this.rgbModel = rgbModel;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
