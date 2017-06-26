package cn.mb.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import cn.mb.model.entity.OilRecipeBean;

public class VideoInfo implements Serializable {

    private static final long serialVersionUID = 3397035693627754657L;

    @SerializedName("id")
    private String videoId;
    private String name;

    @SerializedName("vname")
    private String vName;

    @SerializedName("subtitle")
    private String subTitle;
    private String imgUrl;
    private String videoUrl;

    @SerializedName("addtime")
    private String addTime;


    @SerializedName("colorList")
    private List<VideoInfoConfig> cmdConfigs;

    @SerializedName("RecipesList")
    private List<OilRecipeBean>  recipeConfigs;
    
    private boolean isPlayed;
    
    @SerializedName("duration")
    private String time;
    
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

	public List<VideoInfoConfig> getCmdConfigs() {
		return cmdConfigs;
	}

	public void setCmdConfigs(List<VideoInfoConfig> cmdConfigs) {
		this.cmdConfigs = cmdConfigs;
	}

	public boolean isPlayed() {
		return isPlayed;
	}

	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<OilRecipeBean> getRecipeConfigs() {
		return recipeConfigs;
	}

	public void setRecipeConfigs(List<OilRecipeBean> recipeConfigs) {
		this.recipeConfigs = recipeConfigs;
	}
}
