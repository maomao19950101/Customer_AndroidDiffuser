package cn.mb.ui.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.mb.app.AppConstant;
import cn.mb.app.PreferencesModelConvert;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.model.ClockSetModel;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.ui.view.BasePopupWindow.onSubmitListener;
import cn.mb.ui.view.TwoPickerView;
import cn.mb.util.CommUtils;
import cn.mb.util.PreferencesUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class ClockSetActivity extends InBaseActivity implements OnClickListener {

    private ExecutorService pool = Executors.newFixedThreadPool(3);
    private TwoPickerView timePV;
    private TextView lightOnTV, lightOffTV, musicOnTV, musicOffTV, fogOnTV, fogOffTV;

    private ToggleButton lightTB, musicTB, fogTB;

    private ClockSetModel setModel;
    private PreferencesModelConvert convert;

    @Override
    protected void setAfter(Bundle savedInstanceState) {
        super.setAfter(savedInstanceState);
        lightOnTV = (TextView) findViewById(R.id.pv_clock_light_on);
        lightOffTV = (TextView) findViewById(R.id.pv_clock_light_off);
        musicOnTV = (TextView) findViewById(R.id.pv_clock_music_on);
        musicOffTV = (TextView) findViewById(R.id.pv_clock_music_off);
        fogOnTV = (TextView) findViewById(R.id.pv_clock_fog_on);
        fogOffTV = (TextView) findViewById(R.id.pv_clock_fog_off);

        lightTB = (ToggleButton) findViewById(R.id.pv_clock_light_tb);
        musicTB = (ToggleButton) findViewById(R.id.pv_clock_music_tb);
        fogTB = (ToggleButton) findViewById(R.id.pv_clock_fog_tb);


        lightOnTV.setOnClickListener(this);
        lightOffTV.setOnClickListener(this);
        musicOnTV.setOnClickListener(this);
        musicOffTV.setOnClickListener(this);
        fogOnTV.setOnClickListener(this);
        fogOffTV.setOnClickListener(this);


        lightOnTV.setEnabled(lightTB.isChecked());
        lightOffTV.setEnabled(lightTB.isChecked());
        musicOnTV.setEnabled(musicTB.isChecked());
        musicOffTV.setEnabled(musicTB.isChecked());
        fogOnTV.setEnabled(fogTB.isChecked());
        fogOffTV.setEnabled(fogTB.isChecked());


        lightTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lightOnTV.setEnabled(lightTB.isChecked());
                lightOffTV.setEnabled(lightTB.isChecked());

                setModel.setLightOnOff(isChecked);
                PreferencesUtils.putBoolean(getApplicationContext(), AppConstant.KEY.LIGHT_ONOFF, isChecked);
                if (isChecked) command = "BA0Eaaaa010fFFEE0D0A";
                else command = "BA0Eaaaa000fFFEE0D0A";
                mh.sendEmptyMessage(1);

            }
        });

        musicTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                musicOnTV.setEnabled(musicTB.isChecked());
                musicOffTV.setEnabled(musicTB.isChecked());

                setModel.setMusicOnOff(isChecked);
                PreferencesUtils.putBoolean(getApplicationContext(), AppConstant.KEY.MUSIC_ONOFF, isChecked);
                if (isChecked) command = "BA0C0a0a0001FFEE0D0A";
                else command = "BA0C0a0a0101FFEE0D0A";
                mh.sendEmptyMessage(1);

            }
        });
        fogTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                fogOnTV.setEnabled(fogTB.isChecked());
                fogOffTV.setEnabled(fogTB.isChecked());

                setModel.setFogOnOff(isChecked);
                PreferencesUtils.putBoolean(getApplicationContext(), AppConstant.KEY.FOG_ONOFF, isChecked);

                String openTime = isChecked ? setModel.getFogOnTime() : "00:00";
                String closeTime = isChecked ? setModel.getFogOffTime() : "24:00";
                sendFogCom(openTime, closeTime);
                
                
                command = isChecked ? AppConstant.HardwareCommands.CMD_MIST_ON : AppConstant.HardwareCommands.CMD_MIST_OFF;
            	
                mh.sendEmptyMessage(1);

            }
        });

        convert = new PreferencesModelConvert();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.pv_clock_set;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setModel = convert.getClockSetModel();

        if (sppHelper == null){
            sppHelper = new SppConnectHelper("", null);
        }
        if (bleManager == null) {
            bleManager = sppHelper.getInstance();
        }
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pv_clock_light_on:
                String lightOnTime = lightOnTV.getText().toString().trim();
                String lightOnTimes[] = lightOnTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), lightOnTimes[0], lightOnTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                lightOnTV.setText(temp);
                                PreferencesUtils.put(ClockSetActivity.this, AppConstant.KEY.LIGHT_ONOFF_ON, temp);
                                setModel.setLightOnTime(temp);
                                sendCmd(1);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.pv_clock_light_off:
                String lightOffTime = lightOffTV.getText().toString().trim();
                String lightOffTimes[] = lightOffTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), lightOffTimes[0], lightOffTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                lightOffTV.setText(temp);
                                PreferencesUtils.put(ClockSetActivity.this, AppConstant.KEY.LIGHT_ONOFF_OFF, temp);
                                setModel.setLightOffTime(temp);
                                sendCmd(1);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.pv_clock_music_on:
                String musicOnTime = musicOnTV.getText().toString().trim();
                String musicOnTimes[] = musicOnTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), musicOnTimes[0], musicOnTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                musicOnTV.setText(temp);
                                PreferencesUtils.put(ClockSetActivity.this, AppConstant.KEY.MUSIC_ONOFF_ON, temp);
                                setModel.setMusicOnTime(temp);
                                sendCmd(2);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.pv_clock_music_off:
                String musicOffTime = musicOffTV.getText().toString().trim();
                String musicOffTimes[] = musicOffTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), musicOffTimes[0], musicOffTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                musicOffTV.setText(temp);
                                PreferencesUtils.put(ClockSetActivity.this, AppConstant.KEY.MUSIC_ONOFF_OFF, temp);
                                setModel.setMusicOffTime(temp);
                                sendCmd(2);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.pv_clock_fog_on:
                String fogOnTime = fogOnTV.getText().toString().trim();
                String fogOnTimes[] = fogOnTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), fogOnTimes[0], fogOnTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                fogOnTV.setText(temp);
                                PreferencesUtils.put(ClockSetActivity.this, AppConstant.KEY.FOG_ONOFF_ON, temp);
                                setModel.setFogOnTime(temp);
                                sendCmd(3);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.pv_clock_fog_off:
                String fogOffTime = fogOffTV.getText().toString().trim();
                String fogOffTimes[] = fogOffTime.split("\\:");
                timePV = new TwoPickerView(this, CommUtils.getHour(), CommUtils.getMinute2(), fogOffTimes[0], fogOffTimes[1],
                        new onSubmitListener() {
                            public void onSubmit(String text) {
                                String temp = text.replace(".", ":");
                                fogOffTV.setText(temp);
                                PreferencesUtils.put(ClockSetActivity.this, AppConstant.KEY.FOG_ONOFF_ON, temp);
                                setModel.setFogOffTime(temp);
                                sendCmd(3);
                            }
                        });
                timePV.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            default:
                break;
        }
    }

    private void initData() {
        if (setModel != null) {
            lightTB.setChecked(setModel.isLightOnOff());
            musicTB.setChecked(setModel.isMusicOnOff());
            fogTB.setChecked(setModel.isFogOnOff());

            lightOnTV.setText(setModel.getLightOnTime());
            lightOffTV.setText(setModel.getLightOffTime());
            musicOnTV.setText(setModel.getMusicOnTime());
            musicOffTV.setText(setModel.getMusicOffTime());
            fogOnTV.setText(setModel.getFogOnTime());
            fogOffTV.setText(setModel.getFogOffTime());
        }

    }

    private String command = "";
    private SppManager bleManager;
    private SppConnectHelper sppHelper;
    private Handler mh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (bleManager != null && !TextUtils.isEmpty(command)) {
                    VDLog.i(TAG, "发送的命令:" + command);
                    bleManager.writeData(CHexConver.hexStr2Bytes(command), false);
                }
            }
        }

        ;
    };

    private void sendCmd(int type) {//1、light 2、music 3、fog
        switch (type) {
            case 1:

                if (setModel.isLightOnOff()) {
                    //开始
                    command = "BA0E0a0a0001FFEE0D0A";

                    int[] starts = CommUtils.hourMin2Int(setModel.getLightOnTime());
                    int[] ends = CommUtils.hourMin2Int(setModel.getLightOffTime());

                    //
                    command = "BA0E" + CommUtils.int2HexString(starts[0]) + CommUtils.int2HexString(starts[1]) + "0101FFEE0D0A";
//				 (byte) (0xFF & Integer.decode().intValue());
                    pool.execute(new SendCmdThread(command));

                    command = "BA0E" + CommUtils.int2HexString(ends[0]) + CommUtils.int2HexString(ends[0]) + "0001FFEE0D0A";
                    //结束
                    pool.execute(new SendCmdThread(command));
                }

                break;
            case 2:

                if (setModel.isMusicOnOff()) {
                    int[] starts = CommUtils.hourMin2Int(setModel.getMusicOnTime());
                    int[] ends = CommUtils.hourMin2Int(setModel.getMusicOffTime());

                    command = "BA0C" + CommUtils.int2HexString(starts[0]) + CommUtils.int2HexString(starts[1]) + "0101FFEE0D0A";
                    pool.execute(new SendCmdThread(command));
                    command = "BA0C" + CommUtils.int2HexString(ends[0]) + CommUtils.int2HexString(ends[0]) + "0001FFEE0D0A";
                    pool.execute(new SendCmdThread(command));
                }

                break;
            case 3:
                if (setModel.isFogOnOff()) {
//                    int[] starts = CommUtils.hourMin2Int(setModel.getFogOnTime());
//                    int[] ends = CommUtils.hourMin2Int(setModel.getFogOffTime());
//                    command = "BA04" + CommUtils.int2HexString(starts[0]) + CommUtils.int2HexString(starts[1]) + "0101FFEE0D0A";
//                    pool.execute(new SendCmdThread(command));
//                    command = "BA04" + CommUtils.int2HexString(ends[0]) + CommUtils.int2HexString(ends[0]) + "0001FFEE0D0A";
//                    pool.execute(new SendCmdThread(command));

//                    sendFogCom(setModel.getFogOnTime(), setModel.getFogOffTime());

                }
                break;
            default:
                break;
        }


    }

    private void sendFogCom(String openTime, String closeTime) {
    	
//    	command = isChecked ? AppConstant.HardwareCommands.CMD_MIST_ON : AppConstant.HardwareCommands.CMD_MIST_OFF;
    	
        command = AppConstant.HardwareCommands.CMD_ATOMIZATION_WORK_TIME.replace("hhmmhhmm", openTime.concat(closeTime).replace(":", ""));
//        mh.sendEmptyMessage(1);
    }


    private class SendCmdThread extends Thread {
        private String command;

        public SendCmdThread(String command) {
            this.command = command;
        }

        @Override
        public void run() {
            if (bleManager != null) {
                //		bleManager.writeData(CHexConver.hexStr2Bytes(this.command),false);//CHexConver.hexStr2Bytes(
            }
        }
    }
}
