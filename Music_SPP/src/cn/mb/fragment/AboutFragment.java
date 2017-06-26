package cn.mb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import cn.mb.hk.music.R;
import cn.mb.ui.base.BaseFragment;

public class AboutFragment extends BaseFragment{
	@Override
	public View intitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pv_about, container, false);
		return view;
	}

	@Override
	public String getSelfTitle() {
		return "About";
	}

	@Override
	public void setAfter() {
		super.setAfter();
		WebView webView = (WebView) view.findViewById(R.id.qz_browse_get);
		webView.loadUrl("file:///android_asset/about.html"); 
//		webView.loadUrl(RequestUrls.ABOUT); 
	}
}