package cn.mb.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;

public class WelcomeOilDetailActivity extends InBaseActivity {

	private ImageView detailIV;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
		int resId = getIntent().getIntExtra("detailBg", R.drawable.oil001);
		
		detailIV = (ImageView) findViewById(R.id.welcome_oil_detail_iv);
		
		detailIV.setBackground(getResources().getDrawable(resId));
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.welcome_oil_detail;
	}
}
