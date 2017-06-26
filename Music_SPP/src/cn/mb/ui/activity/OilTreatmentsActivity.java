package cn.mb.ui.activity;


import android.os.Bundle;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;

public class OilTreatmentsActivity extends InBaseActivity{


	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
//		WebView webView = (WebView) findViewById(R.id.qz_browse_get);
//		webView.loadUrl("file:///android_asset/about.html"); 
	}
	

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_oil_treatments;
	}

}
