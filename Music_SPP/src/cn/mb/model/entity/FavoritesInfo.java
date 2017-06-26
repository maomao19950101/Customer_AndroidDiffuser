package cn.mb.model.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="table_fav")
public class FavoritesInfo implements Serializable {

	
	private static final long serialVersionUID = 1024482669875487740L;
	private int _id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	@Column(column="date_time")
	private String dateTime;
	
	@Column(column="color_hex")
	private String colorHex;
	
	@Column(column="color_hex_changed")
	private String colorHexChanged;
	
	


	public String getColorHexChanged() {
		return colorHexChanged;
	}

	public void setColorHexChanged(String colorHexChanged) {
		this.colorHexChanged = colorHexChanged;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getColorHex() {
		return colorHex;
	}

	public void setColorHex(String colorHex) {
		this.colorHex = colorHex;
	}
	
	public String colorHex(){
		return "#"+colorHex;
	}
	
}
