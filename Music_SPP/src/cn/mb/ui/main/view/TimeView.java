package cn.mb.ui.main.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.ui.view.BasePopupWindow.onSubmitListener;
import cn.mb.ui.view.TwoPickerView;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;

public class TimeView extends MainBaseView{

	private View menuDiv,alarmModelDiv,timeModelDiv;
	private ImageButton alarmBtn,timeBtn,alarmCloseBtn,timeCloseBtn;
	 private TwoPickerView timePV;
	private TextView startTimeTV,endTimeTV,delaySoundTimeTV,delayMistTimeTV,delayLightTimeTV,durationTV;
	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected int getResourceLayoutId() {
		return R.layout.pv_main_time;
	}

	@Override
	protected void initView() {
		menuDiv = view.findViewById(R.id.pv_main_time_menu_div);
		alarmModelDiv = view.findViewById(R.id.pv_main_time_alarm_div);
		timeModelDiv = view.findViewById(R.id.pv_main_time_time_div);
		
		alarmBtn = (ImageButton) view.findViewById(R.id.pv_main_time_alarm_btn);
		timeBtn = (ImageButton) view.findViewById(R.id.pv_main_time_time_btn);
		alarmCloseBtn = (ImageButton) view.findViewById(R.id.pv_main_time_alarm_close_btn);
		timeCloseBtn = (ImageButton) view.findViewById(R.id.pv_main_time_time_close_btn);
		
		
		alarmBtn.setOnClickListener(this);
		timeBtn.setOnClickListener(this);
		alarmCloseBtn.setOnClickListener(this);
		timeCloseBtn.setOnClickListener(this);
		
		
		startTimeTV = (TextView) view.findViewById(R.id.pv_clock_start_time_iv); startTimeTV.setOnClickListener(this);
		endTimeTV = (TextView) view.findViewById(R.id.pv_clock_end_time_iv);endTimeTV.setOnClickListener(this);
		delaySoundTimeTV = (TextView) view.findViewById(R.id.pv_clock_delay_sound_start_iv);delaySoundTimeTV.setOnClickListener(this);
		delayMistTimeTV = (TextView) view.findViewById(R.id.pv_clock_delay_mist_start_iv);delayMistTimeTV.setOnClickListener(this);
		delayLightTimeTV = (TextView) view.findViewById(R.id.pv_clock_delay_light_start_iv);delayLightTimeTV.setOnClickListener(this);
		durationTV = (TextView) view.findViewById(R.id.layout_time_timeet_duration);durationTV.setOnClickListener(this);
	}

	@Override
	public void show() {
//		menuDiv.startAnimation(animComeFromRight);
		menuDiv.startAnimation(animComeFromTop);
		menuDiv.setVisibility(View.VISIBLE);
		alarmModelDiv.setVisibility(View.GONE);
		timeModelDiv.setVisibility(View.GONE);
		setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		menuDiv.setVisibility(View.INVISIBLE);
		alarmModelDiv.setVisibility(View.GONE);
		timeModelDiv.setVisibility(View.GONE);
		setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.pv_clock_start_time_iv:
			 String startTime = startTimeTV.getText().toString().trim();
               String startTimes[] = startTime.split("\\:");
			timePV = new TwoPickerView(context, CommUtils.getHour(), CommUtils.getMinute2(), startTimes[0], startTimes[1],
                   new onSubmitListener() {
                       public void onSubmit(String text) {
                           String temp = text.replace(".", ":");
                           startTimeTV.setText(temp);
//                           timeHandler.sendEmptyMessageDelayed(1, 10);//modify 2017/3/23
                       }
                   });
           timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			
			break;
		case R.id.pv_clock_end_time_iv:
			String endTime = startTimeTV.getText().toString().trim();
           String endTimes[] = endTime.split("\\:");
			timePV = new TwoPickerView(context, CommUtils.getHour(), CommUtils.getMinute2(), endTimes[0], endTimes[1],
                   new onSubmitListener() {
                       public void onSubmit(String text) {
                           String temp = text.replace(".", ":");
                           endTimeTV.setText(temp);
                           timeHandler.sendEmptyMessageDelayed(1, 10);
                       }
                   });
           timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			break;
		case R.id.pv_clock_delay_sound_start_iv:
			String delaySoundTime = startTimeTV.getText().toString().trim();
           String delaySoundTimes[] = delaySoundTime.split("\\:");
			timePV = new TwoPickerView(context, CommUtils.getHour(), CommUtils.getMinute2(), delaySoundTimes[0], delaySoundTimes[1],
                   new onSubmitListener() {
                       public void onSubmit(String text) {
                           String temp = text.replace(".", ":");
                           delaySoundTimeTV.setText(temp);
                           timeHandler.sendEmptyMessageDelayed(2, 10);
                       }
                   });
           timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			break;
		case R.id.pv_clock_delay_mist_start_iv:
			String delayMistTime = startTimeTV.getText().toString().trim();
           String delayMistTimes[] = delayMistTime.split("\\:");
			timePV = new TwoPickerView(context, CommUtils.getHour(), CommUtils.getMinute2(), delayMistTimes[0], delayMistTimes[1],
                   new onSubmitListener() {
                       public void onSubmit(String text) {
                           String temp = text.replace(".", ":");
                           delayMistTimeTV.setText(temp);
                           timeHandler.sendEmptyMessageDelayed(3, 10);
                       }
                   });
           timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			break;
		case R.id.pv_clock_delay_light_start_iv:
			String delayLightTime = startTimeTV.getText().toString().trim();
           String delayLightTimes[] = delayLightTime.split("\\:");
			timePV = new TwoPickerView(context, CommUtils.getHour(), CommUtils.getMinute2(), delayLightTimes[0], delayLightTimes[1],
                   new onSubmitListener() {
                       public void onSubmit(String text) {
                           String temp = text.replace(".", ":");
                           delayLightTimeTV.setText(temp);
                           timeHandler.sendEmptyMessageDelayed(4, 10);
                       }
                   });
           timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			break;
		case R.id.layout_time_timeet_duration:
			String duration = startTimeTV.getText().toString().trim();
           String durations[] = duration.split("\\:");
//			timePV = new TwoPickerView(context, CommUtils.getHour(), CommUtils.getMinute2(), durations[0], durations[1],
			timePV = new TwoPickerView(context, CommUtils.getMinute2(), CommUtils.getMinute2(), durations[0], durations[1],
                   new onSubmitListener() {
                       public void onSubmit(String text) {
                           String temp = text.replace(".", ":");
                           durationTV.setText(temp);
                           
              //TODO             //
                           timeHandler.sendEmptyMessageDelayed(5, 10); //需要发送指令？
                       }
                   });
           timePV.showAtLocation(view.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); 
			break;
		
		case R.id.pv_main_time_alarm_btn:
			timeModelDiv.setVisibility(View.GONE);
			alarmModelDiv.startAnimation(animComeFromTop);
			alarmModelDiv.setVisibility(View.VISIBLE);
//			menuDiv.startAnimation(animGoToRight);
//			menuDiv.setVisibility(View.INVISIBLE);
			break;
case R.id.pv_main_time_time_btn:
	alarmModelDiv.setVisibility(View.GONE);
	timeModelDiv.startAnimation(animComeFromTop);
	timeModelDiv.setVisibility(View.VISIBLE);
//	menuDiv.startAnimation(animGoToRight);
//	menuDiv.setVisibility(View.INVISIBLE);
			break;
case R.id.pv_main_time_alarm_close_btn:
case R.id.pv_main_time_time_close_btn:
//	menuDiv.startAnimation(animComeFromRight);
//	menuDiv.setVisibility(View.VISIBLE);
	timeModelDiv.setVisibility(View.GONE);
	alarmModelDiv.setVisibility(View.GONE);
	break;
		default:
			break;
		}
	}

	private Handler timeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {//TODO 计算startTime 及endTime相差多久 多少分钟  最大255
			/*	int[] starts = CommUtils.hourMin2Int(startTimeTV.getText().toString());
		        String startCmd = CommUtils.int2HexString(starts[0]) + CommUtils.int2HexString(starts[1]);
		        int[] ends = CommUtils.hourMin2Int(endTimeTV.getText().toString());
		        String endCmd = CommUtils.int2HexString(ends[0]) + CommUtils.int2HexString(ends[1]);
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_START_END_TIME.replace("HHMMHHMM", startCmd+endCmd)), false);
			*/
			
				int[] starts = CommUtils.hourMin2Int(startTimeTV.getText().toString());
		        String startCmd = CommUtils.int2HexString(starts[0]) + CommUtils.int2HexString(starts[1]);
		        int[] ends = CommUtils.hourMin2Int(endTimeTV.getText().toString());
//		        String endCmd = CommUtils.int2HexString(ends[0]) + CommUtils.int2HexString(ends[1]);
				
		        //开始时，开始分   结束时 结束分  22：22   23：13
		        //20：12 18：11  
//		        （18-20）*60+（11-12）《0 
		     int jiange = (ends[0]-starts[0]) *60 +(ends[1]-starts[1])   ;
		       
		     if(jiange<0){
		    	 jiange = jiange+(60*24);
		     }
		     if(jiange>255)jiange=255;
		     String endCmd = CommUtils.int2HexString(jiange);
		        bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_START_END_TIME.replace("HHMMMM", startCmd+endCmd)), false);
				
				
				
			
			} else if (what == 2) {
				int[] temps = CommUtils.hourMin2Int(delaySoundTimeTV.getText().toString());
		        String tempCmd = CommUtils.int2HexString(temps[0]) + CommUtils.int2HexString(temps[1]);
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_DELAY_SOUND_TIME.replace("HHMM", tempCmd)), false);
			} else if (what == 3) {
				int[] temps = CommUtils.hourMin2Int(delayMistTimeTV.getText().toString());
		        String tempCmd = CommUtils.int2HexString(temps[0]) + CommUtils.int2HexString(temps[1]);
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_DELAY_MIST_TIME.replace("HHMM", tempCmd)), false);
			}else if (what == 4) {
				int[] temps = CommUtils.hourMin2Int(delayLightTimeTV.getText().toString());
		        String tempCmd = CommUtils.int2HexString(temps[0]) + CommUtils.int2HexString(temps[1]);
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_DELAY_LIGHT_TIME.replace("HHMM", tempCmd)), false);
			}else if (what == 5) {//duration 2017/3/23
				int[] temps = CommUtils.hourMin2Int(durationTV.getText().toString());
		        String tempCmd = CommUtils.int2HexString(temps[0]) + CommUtils.int2HexString(temps[1]);
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_DURATION_LIGHT_TIME.replace("MMMM", tempCmd)), false);
			}
		};
	};
}
