package cn.sixpower.spp;

import android.app.Application;

public class SppAppApplication extends Application {
	public static SppAppApplication mApplication = null;
	public static volatile boolean isReconnect = false;
	public static volatile boolean mConnect = false;
	public static volatile String connectDeviceMac=null;
	public static volatile String connectDeviceName=null;
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
	}

	public static SppAppApplication getBleApplication() {
		return mApplication;
	}
}
