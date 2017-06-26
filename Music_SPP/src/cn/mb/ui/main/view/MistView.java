package cn.mb.ui.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.ui.view.BasePopupWindow.onSubmitListener;
import cn.mb.ui.view.OnePickerView;
import cn.mb.ui.view.TwoPickerView;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;

public class MistView extends MainBaseView{

	private View menuDiv,cycleDiv,randomDiv;
	private ImageButton cycleBtn,randomBtn,cycleCloseBtn,randomCloseBtn;
	private TextView cycleTV,randomTV;
	private OnePickerView timePV;
	private TwoPickerView timePV2;
	public MistView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected int getResourceLayoutId() {
		return R.layout.pv_main_mist;
	}

	@Override
	protected void initView() {
		menuDiv = view.findViewById(R.id.pv_main_mist_menu_div);
		cycleDiv = view.findViewById(R.id.pv_main_mist_cycle_div);
		randomDiv = view.findViewById(R.id.pv_main_mist_random_div);
		
		cycleBtn = (ImageButton) view.findViewById(R.id.pv_main_mist_cycle_btn);
		randomBtn = (ImageButton) view.findViewById(R.id.pv_main_mist_radom_btn);
		cycleCloseBtn = (ImageButton) view.findViewById(R.id.pv_main_mist_cycle_close_btn);
		randomCloseBtn = (ImageButton) view.findViewById(R.id.pv_main_mist_random_close_btn);
		
		cycleTV = (TextView) view.findViewById(R.id.layout_mist_cycle_et_duration);
		randomTV = (TextView) view.findViewById(R.id.layout_mist_radom_et_duration);
		randomTV.setOnClickListener(this);
		cycleTV.setOnClickListener(this);
		
		cycleBtn.setOnClickListener(this);
		randomBtn.setOnClickListener(this);
		cycleCloseBtn.setOnClickListener(this);
		randomCloseBtn.setOnClickListener(this);
	}

	@Override
	public void show() {
//		menuDiv.startAnimation(animComeFromRight);
		menuDiv.startAnimation(animComeFromTop);
		menuDiv.setVisibility(View.VISIBLE);
		cycleDiv.setVisibility(View.GONE);
		randomDiv.setVisibility(View.GONE);
//		setVisibility(View.VISIBLE);
		setVisibility(View.GONE);
	}

	@Override
	public void hide() {
		menuDiv.setVisibility(View.INVISIBLE);
		cycleDiv.setVisibility(View.GONE);
		randomDiv.setVisibility(View.GONE);
		setVisibility(View.GONE);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.layout_mist_cycle_et_duration:
         String cycleTime = cycleTV.getText().toString().trim();
       String cycleTimes[] = cycleTime.split("\\:");
		timePV2 = new TwoPickerView(context, CommUtils.getHour5(), CommUtils.getMinute2(), cycleTimes[0], cycleTimes[1],
             new onSubmitListener() {
                 public void onSubmit(String text) {
                	  String temp = text.replace(".", ":");
                      cycleTV.setText(temp);
                      sendIntermittentCom2(temp);
                 }
             });
     timePV2.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
         
			break;
		case R.id.layout_mist_radom_et_duration:
			 String randomTime = randomTV.getText().toString().trim();
//             String randomTimes[] = randomTime.split("\\:");
			timePV = new OnePickerView(context, CommUtils.getMinute30(),  randomTime,
                 new onSubmitListener() {
                     public void onSubmit(String text) {
                         String temp = text.replace(".", ":");
                         randomTV.setText(temp);
                         sendIntermittentCom(temp);
                     }
                 });
         timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			
			break;
		case R.id.pv_main_mist_cycle_btn:
			randomDiv.setVisibility(View.GONE);
			cycleDiv.startAnimation(animComeFromTop);
			cycleDiv.setVisibility(View.VISIBLE);
//			menuDiv.startAnimation(animGoToRight);
//			menuDiv.setVisibility(View.INVISIBLE);
			break;
		case R.id.pv_main_mist_radom_btn:
			cycleDiv.setVisibility(View.GONE);
			randomDiv.startAnimation(animComeFromTop);
			randomDiv.setVisibility(View.VISIBLE);
//			menuDiv.startAnimation(animGoToRight);
//			menuDiv.setVisibility(View.INVISIBLE);
			break;
		case R.id.pv_main_mist_random_close_btn:
		case R.id.pv_main_mist_cycle_close_btn:
//			menuDiv.startAnimation(animComeFromRight);
//			menuDiv.setVisibility(View.VISIBLE);
			cycleDiv.setVisibility(View.GONE);
			randomDiv.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	 private void sendIntermittentCom2(String timeStr) {
	int[] sleeps = CommUtils.hourMin2Int(timeStr);
	
	sendIntermittentCom((sleeps[0]*60+sleeps[1])+"");
	 }
	 private void sendIntermittentCom(String timeStr) {
//			int[] sleeps = CommUtils.hourMin2Int(timeStr);
		 int m = Integer.parseInt(timeStr);
			String sleepCmd = CommUtils.int2HexString(m);
			String command = AppConstant.HardwareCommands.CMD_INTERMITTENT_WORK_TIME.replace("mmmm", sleepCmd + sleepCmd);
			if (bleManager != null) {
				bleManager.writeData(CHexConver.hexStr2Bytes(command), false);
			}
	    }
}
