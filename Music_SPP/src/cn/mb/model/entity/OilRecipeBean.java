package cn.mb.model.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.AbsListView.SelectionBoundsAdjuster;

/**
 * Created by lihongshi on 16/3/3.
 */
@Table(name="oli_recipe")
public class OilRecipeBean implements Serializable {

	private static final long serialVersionUID = -2227065635462472799L;
	@Id
    private String id;
    private String name;
    
    @SerializedName("CategoryName")
    @Column(column="category_name")
    private String categoryName;
    private String img;
    
    @Column(column="oil_one")
    private String oilOne;
    @Column(column="oil_two")
    private String oilTwo;
    @Column(column="oil_three")
    private String oilThree;
   
    @Column(column="is_played")
    private boolean isPlayed;
    
    public OilRecipeBean() {
    }

    public OilRecipeBean(Parcel pc) {
        id = pc.readString();
        name = pc.readString();
        categoryName = pc.readString();
        img = pc.readString();
        oilOne = pc.readString();
        oilTwo = pc.readString();
        oilThree = pc.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOilOne() {
        return oilOne;
    }

    public void setOilOne(String oilOne) {
        this.oilOne = oilOne;
    }

    public String getOilTwo() {
        return oilTwo;
    }

    public void setOilTwo(String oilTwo) {
        this.oilTwo = oilTwo;
    }

    public String getOilThree() {
        return oilThree;
    }

    public void setOilThree(String oilThree) {
        this.oilThree = oilThree;
    }


   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(categoryName);
        dest.writeString(img);
        dest.writeString(oilOne);
        dest.writeString(oilTwo);
        dest.writeString(oilThree);
    }


    private final Parcelable.Creator<OilRecipeBean> CREATOR = new Creator<OilRecipeBean>() {
        @Override
        public OilRecipeBean[] newArray(int size) {
            return new OilRecipeBean[size];
        }

        @Override
        public OilRecipeBean createFromParcel(Parcel in) {
            return new OilRecipeBean(in);
        }
    };
    */
    public String getOilOneName(){
    	return parseString(this.oilOne,0);
    }
    public int getOilOnePower(){
    	return Integer.parseInt(parseString(this.oilOne,1));
    }
    
    public String getOilTwoName(){
    	return parseString(this.oilTwo,0);
    }
    public int getOilTwoPower(){
    	return Integer.parseInt(parseString(this.oilTwo,1));
    }
    
    public String getOilThreeName(){
    	return parseString(this.oilThree,0);
    }
    public int getOilThreePower(){
    	return Integer.parseInt(parseString(this.oilThree,1));
    }
    
    
    private String parseString(String s,int i){
    	String ss[]=s.split(":");
    	if(ss.length>i)
    		return ss[i].trim();
    	else return "10";
    }

	public boolean isPlayed() {
		return isPlayed;
	}

	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}
}
