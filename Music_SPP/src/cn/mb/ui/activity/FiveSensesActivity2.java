package cn.mb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class FiveSensesActivity2 extends InBaseActivity implements OnClickListener {
//VideoClassifyAdapter
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		findViewById(R.id.pv_five_type1).setOnClickListener(this);
		findViewById(R.id.pv_five_type2).setOnClickListener(this);
		findViewById(R.id.pv_five_type3).setOnClickListener(this);
	
		 if(rightImgBtn!=null){
	    	   rightImgBtn.setImageResource(R.drawable.pv_main_search);
	    	   rightImgBtn.setVisibility(View.VISIBLE);
	       }
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_five_senses;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
			i.setClass(this, VideoListActivity.class);

		switch (v.getId()) {
		case R.id.pv_five_type1:
			i.putExtra("videType", "1");
			i.putExtra("videTypeTitle", getString(R.string.pv_five_power));
			break;
		case R.id.pv_five_type2:
			i.putExtra("videType", "2");
			i.putExtra("videTypeTitle", getString(R.string.pv_five_relax));
			break;
		case R.id.pv_five_type3:
			i.putExtra("videType", "3");
			i.putExtra("videTypeTitle", getString(R.string.pv_five_meditation));
			break;
		/*case R.id.pv_five_sleep:
			i.putExtra("videType", "4");
			i.putExtra("videTypeTitle", getString(R.string.pv_five_sleep));
			break;*/
		default:
			break;
		}

		CommUtils.startActivity(this, i);
	}
	
	@Override
	protected void rightImgBtnClick() {
		super.rightImgBtnClick();
		CommUtils.startActivity(FiveSensesActivity2.this, OilTreatmentsActivity.class);
	}
	
}
