package cn.mb.model.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="play_video_bak")
public class PlayVideoBakInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1558523100827746806L;
	//存储id，存储地址
//	private int _id;
//
//	public int get_id() {
//		return _id;
//	}
//
//	public void set_id(int _id) {
//		this._id = _id;
//	}
	@Id
	@Column(column="video_id")
	private String videoId;
	
	@Column(column="video_url")
	private String videoUrl;
	
	@Column(column="video_type")
	private String videoType;
	
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	
	/*public boolean equals(Object obj) { 
        if (this == obj) 
            return true; 
        if (obj == null) 
            return false; 
        if (getClass() != obj.getClass()) 
            return false; 
        final Order other = (Order) obj; 
         if(this.getOrderid()!=other.getOrderid()) 
            return false; 
        return true; 
   }*/ 
	
	public boolean equals(String videoId) { 
        if (videoId == null) 
            return false; 
       if(videoId.equals(this.videoId))
        return true; 
       else return false;
   }
	
}
