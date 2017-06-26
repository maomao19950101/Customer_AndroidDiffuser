package cn.mb.ui.activity;

import android.view.View;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class WelcomeSelectTypeActivity extends InBaseActivity {

	
	public void SSS(View v){
		CommUtils.startActivity(this, WelcomeOilListActivity.class);
	}
	public void SSS2(View v){
		CommUtils.startActivity(this, WelcomeSymptomActivity.class);
	}
	public void SSS3(View v){
		CommUtils.startActivity(this, WelcomeSelectProductActivity.class);
	}
	@Override
	protected int getLayoutResId() {
		return R.layout.welcome_select_type;
	}
}
