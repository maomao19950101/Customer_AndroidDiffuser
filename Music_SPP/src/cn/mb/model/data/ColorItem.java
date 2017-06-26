package cn.mb.model.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class ColorItem  implements Parcelable {
    public static final Creator<ColorItem> CREATOR;
    protected int mColor;
    protected final long mCreationTime;
    protected transient String mHexString;
    protected transient String mHsvString;
    protected final long mId;
    protected String mName;
    protected transient String mRgbString;

    public ColorItem(long id, int color) {
        this.mId = id;
        this.mColor = color;
        this.mCreationTime = System.currentTimeMillis();
    }

    private ColorItem(Parcel in) {
        this.mId = in.readLong();
        this.mColor = in.readInt();
        this.mCreationTime = in.readLong();
        this.mName = in.readString();
    }

    public ColorItem(int color) {
        long currentTimeMillis = System.currentTimeMillis();
        this.mCreationTime = currentTimeMillis;
        this.mId = currentTimeMillis;
        this.mColor = color;
    }

    public long getId() {
        return this.mId;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int color) {
        if (this.mColor != color) {
            this.mColor = color;
            this.mHexString = makeHexString(this.mColor);
            this.mRgbString = makeRgbString(this.mColor);
            this.mHsvString = makeHsvString(this.mColor);
        }
    }

    public long getCreationTime() {
        return this.mCreationTime;
    }

    public String getHexString() {
        if (this.mHexString == null) {
            this.mHexString = makeHexString(this.mColor);
        }
        return this.mHexString;
    }

    public String getRgbString() {
        if (this.mRgbString == null) {
            this.mRgbString = makeRgbString(this.mColor);
        }
        return this.mRgbString;
    }

    public String getHsvString() {
        if (this.mHsvString == null) {
            this.mHsvString = makeHsvString(this.mColor);
        }
        return this.mHsvString;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public static String makeHexString(int value) {
        return "#" + Integer.toHexString(value).substring(2);
    }

    public static String makeRgbString(int value) {
        return "rgb(" + Color.red(value) + ", " + Color.green(value) + ", " + Color.blue(value) + ")";
    }

    public static String makeHsvString(int value) {
        float[] hsv = new float[3];
        Color.colorToHSV(value, hsv);
        return "hsv(" + ((int) hsv[0]) + "\u00b0, " + ((int) (hsv[1] * 100.0f)) + "%, " + ((int) (hsv[2] * 100.0f)) + "%)";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeInt(this.mColor);
        dest.writeLong(this.mCreationTime);
        dest.writeString(this.mName);
    }

    static {
        CREATOR = new Creator<ColorItem>() {
            public ColorItem createFromParcel(Parcel source) {
                return new ColorItem(null);
            }

            public ColorItem[] newArray(int size) {
                return new ColorItem[size];
            }
        };
    }
}