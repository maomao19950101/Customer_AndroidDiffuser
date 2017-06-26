package cn.mb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.ui.base.BleInBaseActivity;
import cn.mb.ui.view.BasePopupWindow.onSubmitListener;
import cn.mb.ui.view.TwoPickerView;
import cn.mb.util.CommUtils;
import cn.mb.util.PreferencesUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppService.ResponseOrNotifyListener;

/**
 * Created by lihongshi on 16/1/26.
 */
public class MotorActivity extends BleInBaseActivity implements OnSeekBarChangeListener,OnClickListener {
//    private EditText motor01_et, motor02_et, motor03_et;

    private SeekBar motor01SB,motor02SB,motor03SB;
    private TwoPickerView timePV;
    private TextView workTimeTV, sleepTimeTV,startTimeTV;
	@Override
	protected int getLayoutResId() {
		return R.layout.activity_motor;
	}
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
//        motor01_et = (EditText) this.findViewById(R.id.motor01_et);
//        motor01_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
//
//        motor02_et = (EditText) this.findViewById(R.id.motor02_et);
//        motor02_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
//
//        motor03_et = (EditText) this.findViewById(R.id.motor03_et);
//        motor03_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
//
//
//        this.findViewById(R.id.motor01_btn).setOnClickListener(this);
//        this.findViewById(R.id.motor02_btn).setOnClickListener(this);
//        this.findViewById(R.id.motor03_btn).setOnClickListener(this);
        
        
        motor01SB = (SeekBar) findViewById(R.id.seekBar1);motor01SB.setOnSeekBarChangeListener(this);
        motor02SB = (SeekBar) findViewById(R.id.seekBar2);motor02SB.setOnSeekBarChangeListener(this);
        motor03SB = (SeekBar) findViewById(R.id.seekBar3);motor03SB.setOnSeekBarChangeListener(this);
        
        
        workTimeTV = (TextView) findViewById(R.id.pv_mist_work_time);
        workTimeTV.setOnClickListener(this);
        sleepTimeTV = (TextView) findViewById(R.id.pv_mist_sleep_time);
        sleepTimeTV.setOnClickListener(this);
        startTimeTV = (TextView) findViewById(R.id.pv_mist_start_time);
        startTimeTV.setOnClickListener(this);
    }
	
	
	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessageDelayed(7, 10);
		handler.sendEmptyMessageDelayed(4, 20);
		handler.sendEmptyMessageDelayed(5, 30);
		handler.sendEmptyMessageDelayed(6, 40);
		//读取指令值
//		bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("CC", "")), false);
		
		
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser){
			Message msg = Message.obtain();
			msg.obj = progress;
			switch (seekBar.getId()) {
			case R.id.seekBar1:
				msg.what=1;
				handler.sendMessage(msg);
				break;
			case R.id.seekBar2:
				msg.what=2;
				handler.sendMessage(msg);
				break;
			case R.id.seekBar3:
				msg.what=3;
				handler.sendMessage(msg);
				break;
			}
		}
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what=msg.what;
			String  processStr="00";
			if(msg.obj!=null)
			{int process = (Integer) msg.obj;
            	String endC = Integer.toHexString(process);
              processStr = endC.length() <= 1 ? ("0" + endC) : endC;
			}
			if (what == 1) {
                 bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_MOTOR1.replace("NN", processStr)), false);
			} else if (what == 2) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_MOTOR2.replace("NN", processStr)), false);
			} else if (what == 3) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_MOTOR3.replace("NN", processStr)), false);
			} else if (what ==4) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "16")), false);
			} else if (what == 5) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "17")), false);
			} else if (what == 6) {
				bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "18")), false);
			}else if(what==7){
				bleManager.setResponseOrNotifyListener(responseListener);
			}else if(msg.what==8){
            	sendWorkCmd();
            	
            }else if(msg.what==9){
            	
            	sendIntermittentCom();
            }
			
		};
	};
	
	
	private ResponseOrNotifyListener responseListener =new ResponseOrNotifyListener() {
		
		@Override
		public void responseOrNotify(int[] data) {
			if(data.length>3){
				if(data[1]==2&&data[2]==22){
					VDLog.i(TAG, "------");
				}else if(data[1]==2&&data[2]==23){
					VDLog.i(TAG, "------======");
				}else if(data[1]==2&&data[2]==24){
					VDLog.i(TAG, "------======------");
				}
			}
		}
	};

	
	public void onClick(android.view.View v) {
		
		switch (v.getId()) {
		   case R.id.pv_mist_start_time:
           	String startTime = startTimeTV.getText().toString().trim();
               String startTimes[] = startTime.split("\\:");
               timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), startTimes[0], startTimes[1],
                       new onSubmitListener() {
                           public void onSubmit(String text) {
                               String temp = text.replace(".", ":");
                               startTimeTV.setText(temp);
                               PreferencesUtils.put(MotorActivity.this, AppConstant.KEY.MIST_START_TIME, temp);
                               handler.sendEmptyMessageDelayed(8, 10);
                           }
                       });
               timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
           	
           	break;
           case R.id.pv_mist_work_time://连续工作
               String lightOnTime = workTimeTV.getText().toString().trim();
               String lightOnTimes[] = lightOnTime.split("\\:");
               timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), lightOnTimes[0], lightOnTimes[1],
                       new onSubmitListener() {
                           public void onSubmit(String text) {
                               String temp = text.replace(".", ":");
                               workTimeTV.setText(temp);
                               PreferencesUtils.put(MotorActivity.this, AppConstant.KEY.MIST_WORK_TIME, temp);
                               handler.sendEmptyMessageDelayed(8, 10);
                           }
                       });
               timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
               break;
           case R.id.pv_mist_sleep_time://间歇工作
               String lightOffTime = sleepTimeTV.getText().toString().trim();
               String lightOffTimes[] = lightOffTime.split("\\:");
               timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), lightOffTimes[0], lightOffTimes[1],
                       new onSubmitListener() {
                           public void onSubmit(String text) {
                               String temp = text.replace(".", ":");
                               sleepTimeTV.setText(temp);
                               PreferencesUtils.put(MotorActivity.this, AppConstant.KEY.MIST_SLEEP_TIME, temp);
                               handler.sendEmptyMessageDelayed(8, 10);
                           }
                       });
               timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
               break;
		default:
			break;
		}
	};
	
	 private void sendWorkCmd() {
	        int[] starts = CommUtils.hourMin2Int(startTimeTV.getText().toString());
	        String startCmd = CommUtils.int2HexString(starts[0]) + CommUtils.int2HexString(starts[1]);
	        
	        int[] works=CommUtils.hourMin2Int(workTimeTV.getText().toString());
	        String worksCmd = CommUtils.int2HexString((works[0] * 60 + works[1])/5);
	        
	        int[] sleeps=CommUtils.hourMin2Int(sleepTimeTV.getText().toString());
	        String sleepsCmd = CommUtils.int2HexString((sleeps[0] * 60 + sleeps[1])/5);
	        
	        
	        String command = AppConstant.HardwareCommands.CMD_ATOMIZATION_WORK_TIME.replace("hhmmhhmm", startCmd+worksCmd+sleepsCmd);
	        
	        if (bleManager != null) {
	            bleManager.writeData(CHexConver.hexStr2Bytes(command), false);//CHexConver.hexStr2Bytes(
	        }
	    }
	    /**
	     * 发送间歇命令
	     * @param time
	     */
	    private void sendIntermittentCom() {
			int[] sleeps = CommUtils.hourMin2Int(sleepTimeTV.getText().toString());
			String sleepCmd = CommUtils.int2HexString(sleeps[0] * 60 + sleeps[1]);
			String command = AppConstant.HardwareCommands.CMD_INTERMITTENT_WORK_TIME.replace("mmmm", sleepCmd + sleepCmd);
			if (bleManager != null) {
				bleManager.writeData(CHexConver.hexStr2Bytes(command), false);
			}
	    }
}
