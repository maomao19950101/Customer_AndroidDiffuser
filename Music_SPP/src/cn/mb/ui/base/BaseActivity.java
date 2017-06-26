package cn.mb.ui.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;
import cn.mb.http.VDNotic;

public class BaseActivity extends FragmentActivity {
	protected AppAplication appContext;

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		boolean conn = ni != null && ni.isConnectedOrConnecting();

		if (!conn) {
			VDNotic.alert(this, "当前网络不可用");
		}
		return conn;
	}

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		appContext = (AppAplication) getApplication();
		AppAplication.listActivity.add(this);
		
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			int flagStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//			getWindow().addFlags(flagStatus);
			int flagNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//			getWindow().addFlags(flagNavigation);
		}
		

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppAplication.listActivity.remove(this);
	}


	protected View getEmptyView() {
		// View emptyView =
		// LayoutInflater.from(this).inflate(R.layout.pv_no_data,
		// (ViewGroup)listView.getParent(), false);
		View emptyView = findViewById(R.id.showconstantdataDiv);
		return emptyView;
	}

	protected View getEmptyView2(ListView listView) {
		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		emptyView.setText("没有数据！");
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		// listView.setEmptyView(emptyView);
		return emptyView;
	}

	protected View getEmptyView(ListView listView) {
		View emptyView = LayoutInflater.from(this).inflate(R.layout.pv_no_data, null, true);
		Button reload = (Button) emptyView.findViewById(R.id.reloadthebtn);

		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);

		if (reload != null) {
			reload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					reloadData();
				}
			});
		}
		return emptyView;
	}

	protected void reloadData() {
	}
	
	// 隐藏软件盘
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		@SuppressWarnings("static-access")
		InputMethodManager manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
		return manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//onKeyUp 解决
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitActivity();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void exitActivity() {
		if(finishBefore()){
				finish();
				overridePendingTransition(0, R.anim.slide_out_right);
			}
	}
	protected boolean finishBefore() {
		return true;
	}// 关闭前需要的处理
}
