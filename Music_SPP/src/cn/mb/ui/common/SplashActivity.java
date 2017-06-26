package cn.mb.ui.common;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.mb.util.CommUtils;

public class SplashActivity extends Activity {
	boolean isFirstRun = false;

	private  final int GO_HOME = 1000;
	private  final int GO_GUIDE = 1001;

	private static final String IS_FIRST_RUN = "is_first_run";

	private Handler switchHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		SharedPreferences preferences = getSharedPreferences(IS_FIRST_RUN, MODE_PRIVATE);
		isFirstRun = preferences.getBoolean(IS_FIRST_RUN, true);
		if (!isFirstRun) {
			switchHandler.sendEmptyMessage(GO_HOME);
		} else {
			switchHandler.sendEmptyMessage(GO_GUIDE);
		}
	}

	private void goHome() {
		CommUtils.startActivity(this, WelcomeActivity.class);finish();
	}

	private void goGuide() {
		CommUtils.startActivity(this, GuideActivity.class);finish();
	}
}