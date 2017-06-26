package cn.mb.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.mb.app.AppAplication;
import cn.mb.app.ProductType;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class WelcomeSelectProductActivity extends InBaseActivity implements OnClickListener{

//yun
	//OIL
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.setAfter(savedInstanceState);

		
		findViewById(R.id.select_product_pz01).setOnClickListener(this);
		findViewById(R.id.select_product_pz02).setOnClickListener(this);
	}
	
	@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.select_product_pz01:
				AppAplication.productType = ProductType.PZ01;
					 CommUtils.startActivity(this, FiveSensesActivity.class);
				break;
case R.id.select_product_pz02:
	AppAplication.productType = ProductType.PZ02;
	CommUtils.startActivity(this, Pz02VideoListActivity.class);
				break;
			default:
				break;
			}
			
		}

	@Override
	protected int getLayoutResId() {
		return R.layout.welcome_select_product;
	}
}
