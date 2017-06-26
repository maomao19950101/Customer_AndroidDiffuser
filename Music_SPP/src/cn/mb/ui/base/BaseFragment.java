package cn.mb.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;
import cn.mb.ui.activity.MainActivity;

public abstract class BaseFragment extends android.support.v4.app.Fragment {

	protected FragmentActivity defaultActivity;
	protected View view;
	protected AppAplication appContext;
//	protected boolean isVisible;  //懒加载
	private TextView titleTV;
	protected ImageButton rightImgBtn;
//	protected UserInfo userInfo;
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		initPrepare();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		defaultActivity = getActivity();
		appContext = (AppAplication)defaultActivity.getApplication();
		view = intitView(inflater, container, savedInstanceState);
//		userInfo = appContext.getUserInfo();
		titleTV = (TextView) view.findViewById(R.id.sys_header_title);
		if(titleTV!=null){
			titleTV.setText(getSelfTitle());
		}
		rightImgBtn = (ImageButton) view.findViewById(R.id.sys_header_rightbtn_ib);
		if (rightImgBtn != null)
			rightImgBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rightImgBtnClick();
				}
			});
		setAfter();
		return view;
	}

	public abstract View intitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	public void setAfter(){
		if(view==null)return;
	}
	
	
	public abstract String getSelfTitle();
	
	public boolean isNetworkConnected() {
		boolean b= true;
		if (defaultActivity instanceof MainActivity) {
			b = ((MainActivity) defaultActivity).isNetworkConnected();
		}
		return b;
	}
	
	protected void rightImgBtnClick() {

	}
	
/*	private boolean isPrepared;
	private synchronized void initPrepare() {
		  if (isPrepared) {
	             onFirstUserVisible();
	         } else {
	              isPrepared = true;
	          }
		
	}


*//**
   * 第一次fragment可见（进行初始化工作）
   *//*
  public void onFirstUserVisible() {
	  System.out.println("-----------------------111--11"+DateUtil.getDate16());
  }


  private boolean isFirstVisible = true;
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
      super.setUserVisibleHint(isVisibleToUser);
      if (isVisibleToUser) {//可见
          if (isFirstVisible) {//第一次可见  加载生成页面
              isFirstVisible = false;
              initPrepare();
          } else {
        	  onUserResume();
          }
      } else {//不可见
          if (isFirstInvisible) {
              isFirstInvisible = false;
              onFirstUserInvisible();
          } else {
              onUserInvisible();
          }
    	  onUserPause();
      }
  }
  
  private boolean isFirstResume=true;
  @Override
public void onResume() {
	super.onResume();
	
	if (isFirstResume) {
        isFirstResume = false;
        return;
    }
    if (getUserVisibleHint()) {
    	onUserResume();//onResume
    }
}

protected void onUserResume(){
	
}
protected void onUserPause(){
	
}
@Override
public void onPause() {
    super.onPause();
    if (getUserVisibleHint()) {
    	onUserPause();//onPause
    }
}*/
}
