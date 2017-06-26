package cn.mb.model.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;

public class PlayedRecipeBean implements Serializable {

	private static final long serialVersionUID = 6711285638146150223L;

	// ID
	// video id
	// recipe id
	private int _id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	@Column(column = "video_id")
	private String videoId;

	@Column(column = "recipe_id")
	private String recipeId;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
	
}
