package cn.mb.ui.main.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.ui.activity.OilListPopupWindow;
import cn.mb.ui.activity.OilListPopupWindow.OnSelectItemListener;
import cn.mb.ui.activity.OilRecipeListNameActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppService.ResponseOrNotifyListener;

public class OilsView extends MainBaseView implements OnSeekBarChangeListener {

	private View menuDiv, controlDiv;
	private TextView recipeName, oil1Name, oil2Name, oil3Name,oil1Value,oil2Value,oil3Value;
	private SeekBar oil1Sb, oil2Sb, oil3Sb;
	private boolean isFirst=true;
	private OilListPopupWindow oilListView;
	public OilsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected int getResourceLayoutId() {
		return R.layout.pv_main_oils;
	}

	@Override
	protected void initView() {
		menuDiv = view.findViewById(R.id.pv_main_oils_menu_div);
		controlDiv = view.findViewById(R.id.pv_main_oils_control_div);

		recipeName = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_name_tv);
		oil1Name = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_oil1_tv);
		oil2Name = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_oil2_tv);
		oil3Name = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_oil3_tv);

		oil1Sb = (SeekBar) view.findViewById(R.id.pv_main_oils_control_recipe_oil1_sb);
		oil2Sb = (SeekBar) view.findViewById(R.id.pv_main_oils_control_recipe_oil2_sb);
		oil3Sb = (SeekBar) view.findViewById(R.id.pv_main_oils_control_recipe_oil3_sb);

		recipeName.setOnClickListener(this);
		oil1Sb.setOnSeekBarChangeListener(this);
		oil2Sb.setOnSeekBarChangeListener(this);
		oil3Sb.setOnSeekBarChangeListener(this);
		
		
		oil1Value = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_oil1_sb_value);
		oil2Value = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_oil2_sb_value);
		oil3Value = (TextView) view.findViewById(R.id.pv_main_oils_control_recipe_oil3_sb_value);
		
		
		oil1Name.setOnClickListener(this);
		oil2Name.setOnClickListener(this);
		oil3Name.setOnClickListener(this);
	}

	@Override
	public void show() {
		// menuDiv.startAnimation(animComeFromRight);
		menuDiv.setVisibility(View.INVISIBLE);
		controlDiv.startAnimation(animComeFromTop);
		controlDiv.setVisibility(View.VISIBLE);
		setVisibility(View.VISIBLE);
		
		if(bleManager!=null){
			
			bleManager.setResponseOrNotifyListener(oilsValueListener);
			getOilsValue();
			isFirst=false;
		}
	}

	@Override
	public void hide() {
		menuDiv.setVisibility(View.INVISIBLE);

		animGoToTop.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				controlDiv.setVisibility(View.GONE);
				setVisibility(View.GONE);
			}
		});
		controlDiv.startAnimation(animGoToTop);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_main_oils_control_recipe_name_tv:
			CommUtils.startActivity(context, OilRecipeListNameActivity.class);
			break;
		case R.id.pv_main_oils_control_recipe_oil1_tv:
			oilListView = new OilListPopupWindow(context,new OnSelectItemListener() {
				
				@Override
				public void oItemSelect(String text) {
					oil1Name.setText(text);
				}
			});
			
			oilListView.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.pv_main_oils_control_recipe_oil2_tv:
oilListView = new OilListPopupWindow(context,new OnSelectItemListener() {
	
	@Override
	public void oItemSelect(String text) {
		oil2Name.setText(text);
	}
});
			
			oilListView.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			
			break;
		case R.id.pv_main_oils_control_recipe_oil3_tv:
oilListView = new OilListPopupWindow(context,new OnSelectItemListener() {
	
	@Override
	public void oItemSelect(String text) {
		oil3Name.setText(text);
	}
});
			
			oilListView.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			Message msg = Message.obtain();
			msg.obj = progress;
			switch (seekBar.getId()) {
			case R.id.pv_main_oils_control_recipe_oil1_sb:
				msg.what = 1;
				break;
			case R.id.pv_main_oils_control_recipe_oil2_sb:
				msg.what = 2;
				break;
			case R.id.pv_main_oils_control_recipe_oil3_sb:
				msg.what = 3;
				break;
			}
			oilHandler.sendMessage(msg);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	private Handler oilHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			String processStr = "00";int process =10;
			if (msg.obj != null) {
				process= (Integer) msg.obj;
				String endC = Integer.toHexString(process);
				processStr = endC.length() <= 1 ? ("0" + endC) : endC;
			}
			if (what == 1) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_MOTOR1.replace("NN", processStr)), false);
				oil1Value.setText(process+"");
			} else if (what == 2) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_MOTOR2.replace("NN", processStr)), false);
				oil2Value.setText(process+"");
			} else if (what == 3) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_MOTOR3.replace("NN", processStr)), false);
				oil3Value.setText(process+"");
			}else if (what == 4) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "16")), false);
			}else if (what == 5) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "17")), false);
			}else if (what == 6) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "18")), false);
			}
		};
	};
	
	
	private void getOilsValue(){
		oilHandler.sendEmptyMessageDelayed(4, 20);
    	oilHandler.sendEmptyMessageDelayed(5, 50);
    	oilHandler.sendEmptyMessageDelayed(6, 80);
	}
private ResponseOrNotifyListener  oilsValueListener = new ResponseOrNotifyListener() {
		
		@Override
		public void responseOrNotify(int[] data) {
			int l = data.length;
			if (l > 2) {
				switch (data[1]) {
				case 0x16:
					oil1Sb.setProgress(data[4]);
					break;
				case 0x17:
					oil2Sb.setProgress(data[4]);
					break;
				case 0x18:
					oil3Sb.setProgress(data[4]);
					break;
				default:
					break;
				}

			}
			System.out.println(data + "   " + l);
		}
	};
}
