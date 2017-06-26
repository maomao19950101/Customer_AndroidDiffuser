package cn.mb.fragment;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	private SharedPreferences mPrefs;
	 private static Preferences mInstance;
	  private static final String BUCKET_NAME = "preferences_bucket";
	    public static final String KEY_VIDEOS_ENABLED = "videos_enabled";
    public static Preferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Preferences(context);
        }
        return getInstance();
    }

    public static Preferences getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new IllegalStateException("Preferences not initialized");
    }

    private Preferences(Context context) {
        this.mPrefs = context.getSharedPreferences(BUCKET_NAME, 0);
//        deviceId = CommonUtils.getDeviceId(context);
    }
    public boolean isVideosEnabled() {
        return this.mPrefs.getBoolean(KEY_VIDEOS_ENABLED, true);
    }

    public void setVideosEnabled(boolean z) {
        this.mPrefs.edit().putBoolean(KEY_VIDEOS_ENABLED, z).commit();
    }

}
