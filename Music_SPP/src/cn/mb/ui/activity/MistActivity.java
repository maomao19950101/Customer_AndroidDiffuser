package cn.mb.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.ui.base.BleInBaseActivity;
import cn.mb.ui.view.BasePopupWindow.onSubmitListener;
import cn.mb.ui.view.TwoPickerView;
import cn.mb.util.CommUtils;
import cn.mb.util.PreferencesUtils;
import cn.sixpower.spp.CHexConver;

public class MistActivity extends BleInBaseActivity implements OnClickListener {
    private ImageView onOffBtn;

    private TwoPickerView timePV;
    private TextView workTimeTV, sleepTimeTV,startTimeTV;

    @Override
    protected void setAfter(Bundle savedInstanceState) {
        super.setAfter(savedInstanceState);
        onOffBtn = (ImageView) findViewById(R.id.pv_mist_onoff_btn);
        onOffBtn.setOnClickListener(this);
        workTimeTV = (TextView) findViewById(R.id.pv_mist_work_time);
        workTimeTV.setOnClickListener(this);
        sleepTimeTV = (TextView) findViewById(R.id.pv_mist_sleep_time);
        sleepTimeTV.setOnClickListener(this);
        startTimeTV = (TextView) findViewById(R.id.pv_mist_start_time);
        startTimeTV.setOnClickListener(this);
    }

//    private SppManager bleManager;
//    private SppConnectHelper sppHelper;

    public void sendOnOff() {
        mh.sendEmptyMessageDelayed(1, 10);
        i++;
    }

    private int i = 0;

    private Handler mh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (bleManager != null) {
                    int k = i % 2;
                    bleManager.writeData(CHexConver.hexStr2Bytes(k == 0 ? AppConstant.HardwareCommands.CMD_MIST_OFF : AppConstant.HardwareCommands.CMD_MIST_ON), false);
                    onOffBtn.setImageResource(k == 0 ? R.drawable.pv_light_on : R.drawable.pv_light_off);
                }
            }else if(msg.what==2){
            	sendWorkCmd();
            	
            }else if(msg.what==3){
            	
            	sendIntermittentCom();
            }
        }

        ;
    };

    @Override
    protected void onResume() {
        super.onResume();
//        if (sppHelper == null) sppHelper = new SppConnectHelper("", null);
//        if (bleManager == null) bleManager = sppHelper.getInstance();
        workTimeTV.setText(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.MIST_WORK_TIME, "12:12"));
        sleepTimeTV.setText(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.MIST_SLEEP_TIME, "12:12"));
        startTimeTV.setText(PreferencesUtils.getString(AppAplication.getApplication(), AppConstant.KEY.MIST_START_TIME, "12:12"));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.pv_mist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pv_mist_onoff_btn://雾化开关
                sendOnOff();
                break;
            case R.id.pv_mist_start_time:
            	String startTime = startTimeTV.getText().toString().trim();
                String startTimes[] = startTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), startTimes[0], startTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                startTimeTV.setText(temp);
                                PreferencesUtils.put(MistActivity.this, AppConstant.KEY.MIST_START_TIME, temp);
                                mh.sendEmptyMessageDelayed(2, 10);
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
                                PreferencesUtils.put(MistActivity.this, AppConstant.KEY.MIST_WORK_TIME, temp);
                                mh.sendEmptyMessageDelayed(2, 10);
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
                                PreferencesUtils.put(MistActivity.this, AppConstant.KEY.MIST_SLEEP_TIME, temp);
                                mh.sendEmptyMessageDelayed(2, 10);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
           
        }
    }

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
