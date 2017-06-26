package cn.mb.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.app.ProductType;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;
import cn.mb.util.PreferencesUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class VideoSetActivity extends InBaseActivity {
//name time playBtn
	private ImageButton playBtn;
	private TextView videoNameTV,timeTV;
	private SeekBar seekBar;
	private String currentName;
	private boolean isPlaying=true;
	private int currentInt,duration;
	  private ToggleButton  mistTB, soundTB, lightTB, timeTB, motorTB;
	    private String command = "";
	    private SppManager bleManager;
	    private SppConnectHelper helper;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
		playBtn = (ImageButton) findViewById(R.id.pv_video_set_pay_iv);
		videoNameTV = (TextView) findViewById(R.id.pv_video_set_name_tv);
		timeTV = (TextView) findViewById(R.id.pv_video_set_name_iv);
	
		seekBar =(SeekBar) findViewById(R.id.pv_video_set_sb);
	
		initData();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstant.Action.PLAY_VIDEO_TIME_ACTION);
		registerReceiver(receiver, filter);
		
		playBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPlaying){
					isPlaying=false;
					playBtn.setImageResource(R.drawable.v_play);
				}else{
					isPlaying=true;
					playBtn.setImageResource(R.drawable.v_pause);
				}
				
				sendBroadcast(new Intent(AppConstant.Action.PLAY_VIDEO_PLAY_ACTION));
			}
		});
		
		
		
		
		
		
		initStatus();
		
		
		
	}
    @Override
    public void onResume() {
        super.onResume();
        if (helper == null) {
            helper = new SppConnectHelper("", null);
        }
        if (bleManager == null) {
            bleManager = helper.getInstance();
        }
    }
	
	private void initData(){
		Intent intent = getIntent();
		currentName = intent.getStringExtra("videoName");
		isPlaying = intent.getBooleanExtra("isPlay", true);
		currentInt = intent.getIntExtra("currentInt", 0);
		duration = intent.getIntExtra("duration", 0);
		if(isPlaying)
			playBtn.setImageResource(R.drawable.v_pause);
		else 
			playBtn.setImageResource(R.drawable.v_play);
		
		videoNameTV.setText(currentName);
		timeTV.setText(CommUtils.toTime(currentInt)+"\\"+CommUtils.toTime(duration));
	}
	
	
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_set;
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String s =intent.getAction();
			
			if(s.equals(AppConstant.Action.PLAY_VIDEO_TIME_ACTION)){
				currentInt = intent.getIntExtra("currentInt", 0);
				duration = intent.getIntExtra("duration", 0);
				seekBar.setMax(duration);
				seekBar.setProgress(currentInt);
				timeTV.setText(CommUtils.toTime(currentInt)+"\\"+CommUtils.toTime(duration));
			}
		}
		
	};
	
	protected void onDestroy() {
		super.onDestroy();
		if(receiver!=null)unregisterReceiver(receiver);
	};
	

	private void initStatus(){
		 if (ProductType.PZ02.getName().equalsIgnoreCase(AppAplication.productType.getName())) {
            view.findViewById(R.id.pv_main_motor_div).setVisibility(View.VISIBLE);
            view.findViewById(R.id.pv_main_mist_div).setVisibility(View.GONE);
         }else{
       	  AppAplication.productType=ProductType.PZ01;
       	  view.findViewById(R.id.pv_main_mist_div).setVisibility(View.VISIBLE);
       	  view.findViewById(R.id.pv_main_motor_div).setVisibility(View.GONE);
         }


         mistTB = (ToggleButton) view.findViewById(R.id.pv_main_mist_tg);
         soundTB = (ToggleButton) view.findViewById(R.id.pv_main_sound_tg);
         lightTB = (ToggleButton) view.findViewById(R.id.pv_main_light_tg);
         timeTB = (ToggleButton) view.findViewById(R.id.pv_main_time_tg);
         motorTB = (ToggleButton) view.findViewById(R.id.pv_main_motor_tg);


         mistTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 PreferencesUtils.putBoolean(VideoSetActivity.this, AppConstant.KEY.MUSIC_ONOFF, isChecked);
                 command = isChecked ? AppConstant.HardwareCommands.CMD_MIST_ON : AppConstant.HardwareCommands.CMD_MIST_OFF;
                 mh.sendEmptyMessage(1);
             }
         });
         soundTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 PreferencesUtils.putBoolean(VideoSetActivity.this, AppConstant.KEY.FOG_ONOFF, isChecked);
                 if (isChecked)
                     command = AppConstant.HardwareCommands.CMD_MUSIC_ON;
                 else
                     command = AppConstant.HardwareCommands.CMD_MUSIC_OFF;
                 mh.sendEmptyMessage(1);
             }
         });

         lightTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 PreferencesUtils.putBoolean(VideoSetActivity.this, AppConstant.KEY.FOG_ONOFF, isChecked);
                 command = isChecked ? AppConstant.HardwareCommands.CMD_LIGHT_ON : AppConstant.HardwareCommands.CMD_LIGHT_OFF;
                 mh.sendEmptyMessage(1);
             }
         });
         timeTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 PreferencesUtils.putBoolean(VideoSetActivity.this, AppConstant.KEY.FOG_ONOFF, isChecked);
                 if (isChecked)
                     command = "BA050000010fFFEE0D0A";
                 else
                     command = "BA06aaaa000fFFEE0D0A";

                 command = "BA02090C0E0FFFEE0D0A";

                 mh.sendEmptyMessage(1);

             }
         });

         motorTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                 if (isChecked) {
//                     sendPhoneTimeCom();
                 }
             }
         });

         
         mh.sendEmptyMessageDelayed(2, 30);
	}
	 private Handler mh = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            if (msg.what == 1) {
	                if (bleManager != null && !TextUtils.isEmpty(command)) {
	                    VDLog.i(TAG, "发送的命令:" + command);
	                    bleManager.writeData(CHexConver.hexStr2Bytes(command), false);// CHexConver.hexStr2Bytes(
	                }
	            }else if(msg.what==2){
//	            	sendPhoneTimeCom();
	            }
	        }

	        ;
	    };
}
