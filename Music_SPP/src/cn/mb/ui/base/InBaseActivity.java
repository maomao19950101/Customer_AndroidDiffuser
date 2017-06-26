package cn.mb.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;


public abstract class InBaseActivity extends BaseActivity {

	protected String TAG = this.getClass().getName();
	// 头部
	// 底部
	protected View view;
	protected Button rightBtn;// 右边的按钮
	protected ImageButton rightImgBtn;
	// 搜索 购物车
//	protected AppAplication appContext;
//	protected SimpleHttpUtil httpUtil;
	protected ImageView backBtn;
	protected TextView titleTV;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		appContext = (AppAplication) getApplication();
//		view = initView(savedInstanceState);
		view = LayoutInflater.from(this).inflate(getLayoutResId(), null, true);
		
		view.setBackgroundResource(AppAplication.bgResId);
//		httpUtil = new SimpleHttpUtil();
		
		// 处理头部文字和返回键
		backBtn = (ImageView) view.findViewById(R.id.sys_header_back);
		if (backBtn != null) {
			backBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exitActivity();
				}
			});
		}
		titleTV = (TextView) view.findViewById(R.id.sys_header_title);
		if (titleTV != null)
			titleTV.setText(getTitle());

		setBefore(savedInstanceState);
		setContentView(view);

		rightBtn = (Button) view.findViewById(R.id.sys_header_rightbtn);
		rightImgBtn = (ImageButton) view.findViewById(R.id.sys_header_rightbtn_ib);

		if (rightBtn != null)
			rightBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rightBtnClick();
				}
			});
		if (rightImgBtn != null)
			rightImgBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					rightImgBtnClick();
				}
			});
		setAfter(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		view.setBackgroundResource(AppAplication.bgResId);
	}
	
//	protected abstract View initView(Bundle savedInstanceState);
	protected abstract int getLayoutResId();



	protected void setBefore(Bundle savedInstanceState) {
	}// view set之前的操作

	protected void setAfter(Bundle savedInstanceState) {
	}// view 之后的操作

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void rightBtnClick() {

	}

	protected void rightImgBtnClick() {

	}

//	protected boolean isFinsh(){
//		return true;
//	}
	
	
}
