package cn.mb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class WelcomeSymptomActivity extends InBaseActivity {

	
private ImageButton sympBtn[]=new ImageButton[8];
private int[] category = {R.array.brainsystem,
		R.array.digestivesystem,
		R.array.heartsystem,
		R.array.musculebone,
		R.array.reproductiveandhormone,
		R.array.respiratorysystem,
		R.array.skin,R.array.other};

@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
//		sympBtn[8] =  (ImageButton) findViewById(R.id.pv_symptom_btn1);
//		sympBtn[9] =  (ImageButton) findViewById(R.id.pv_symptom_btn2);
		sympBtn[5] =  (ImageButton) findViewById(R.id.pv_symptom_btn3);
		sympBtn[6] =  (ImageButton) findViewById(R.id.pv_symptom_btn4);
		sympBtn[2] =  (ImageButton) findViewById(R.id.pv_symptom_btn5);
		sympBtn[1] =  (ImageButton) findViewById(R.id.pv_symptom_btn6);
		sympBtn[4] =  (ImageButton) findViewById(R.id.pv_symptom_btn7);
//		sympBtn[10] =  (ImageButton) findViewById(R.id.pv_symptom_btn8);
		sympBtn[3] =  (ImageButton) findViewById(R.id.pv_symptom_btn9);
		sympBtn[0] =  (ImageButton) findViewById(R.id.pv_symptom_btn10);
		sympBtn[7] =  (ImageButton) findViewById(R.id.pv_symptom_btn11);
		
		initListener();
		
	}
	
	
	private void initListener(){
		for (ImageButton btn : sympBtn) {
			btn.setOnClickListener(onClick);
		}
	}
	
	private View.OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for (int i=0;i<sympBtn.length;i++) {
				ImageButton btn=sympBtn[i];
				if(btn.getId()==v.getId()){
					if(i>=category.length)i=0;
//					if(i>7)i=7;
					Intent intent = new Intent();
					intent.setClass(WelcomeSymptomActivity.this, WelcomeSymptomListActivity.class);
					intent.putExtra("category",category[i]);
					CommUtils.startActivity(WelcomeSymptomActivity.this, intent);
					break;
				}
			}
		}
	};
	
	
	@Override
	protected int getLayoutResId() {
		return R.layout.welcome_symptom;
	}
}
