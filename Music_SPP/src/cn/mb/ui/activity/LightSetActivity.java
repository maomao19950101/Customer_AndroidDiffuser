package cn.mb.ui.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class LightSetActivity extends InBaseActivity implements OnClickListener, OnSeekBarChangeListener {
    private ExecutorService pool = Executors.newFixedThreadPool(3);
    private SeekBar seekBar;
    private ImageView onOffBtn;
    private String currentColorHex = "ff0000";
    private int sbMax = 10;
    private ImageButton orderIM, randomIM;

    @Override
    protected void setAfter(Bundle savedInstanceState) {
        super.setAfter(savedInstanceState);

        onOffBtn = (ImageView) findViewById(R.id.pv_light_onoff_btn);
        onOffBtn.setOnClickListener(this);
        findViewById(R.id.pv_colorpicker_btn).setOnClickListener(this);
        findViewById(R.id.pv_cameracolor_btn).setOnClickListener(this);
        findViewById(R.id.pv_rgb_fav_btn).setOnClickListener(this);
        findViewById(R.id.pv_light_default_btn).setOnClickListener(this);

        findViewById(R.id.pv_light_order_btn).setOnClickListener(this);
        findViewById(R.id.pv_light_random_btn).setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.sb_lightness_sb);

        seekBar.setOnSeekBarChangeListener(this);
        sbMax = seekBar.getMax();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.pv_light_set;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pv_light_onoff_btn:
                sendOnOff();
                break;
            case R.id.pv_colorpicker_btn:
                CommUtils.startActivity(this, ColorPickerActivity.class);
                break;
            case R.id.pv_cameracolor_btn:
                CommUtils.startActivity(this, CameraColorPickerActivity.class);
                break;
            case R.id.pv_rgb_fav_btn:
                CommUtils.startActivity(this, FavoriteListActivity.class);
                break;
            case R.id.pv_light_default_btn:
                mh.sendEmptyMessageDelayed(2, 10);
                break;
            case R.id.pv_light_order_btn:
                mh.sendEmptyMessage(3);
                break;
            case R.id.pv_light_random_btn:
                mh.sendEmptyMessage(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && progress > 0) {
            //
            pool.execute(new SendCmdThread(progress));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    //	private String commandOnOff[]={"BA04AA0F01CCEEDA","BA04AA0F00CCEEDA"};
    private String commandOnOff[] = {AppConstant.HardwareCommands.CMD_LIGHT_ON, AppConstant.HardwareCommands.CMD_LIGHT_OFF};
    private SppManager bleManager;
    private SppConnectHelper sppHelper;

    public void sendOnOff() {
        mh.sendEmptyMessageDelayed(1, 10);
        i++;
    }

    private int i = 0;
    private Handler mh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {//开关
                if (bleManager != null) {
                    int k = i % 2;
                    bleManager.writeData(CHexConver.hexStr2Bytes(commandOnOff[k]), false);
                    if (k == 0) {
                        onOffBtn.setImageResource(R.drawable.pv_light_on);
                    } else {
                        onOffBtn.setImageResource(R.drawable.pv_light_off);
                    }
                }
            } else if (msg.what == 2) {//重置清除
                if (bleManager != null) {
                    bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB_CLEAR), false);
                }
            } else if (msg.what == 3) {//顺序
                if (bleManager != null) {
                    bleManager.writeData(CHexConver.hexStr2Bytes("BA10AAAA010FFFEE0D0A"), false);
                }
            } else if (msg.what == 4) {//随机
                if (bleManager != null) {
                    bleManager.writeData(CHexConver.hexStr2Bytes("BA10AAAA020FFFEE0D0A"), false);
                }
            }
        }

        ;
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (sppHelper == null) sppHelper = new SppConnectHelper("", null);
        if (bleManager == null) bleManager = sppHelper.getInstance();

        seekBar.setProgress(sbMax);
    }


    private class SendCmdThread extends Thread {
        private int progress = 1;
        private String command;

        public SendCmdThread(int progress) {
            this.progress = progress;
        }

        @Override
        public void run() {

            int c = (int) (AppAplication.colorRGB * ((progress * 1.0f) / sbMax));
            c = AppAplication.colorRGB;
            if (c == 0) {
                c = -1;
            }
            currentColorHex = Integer.toHexString(c).substring(2);


            String Rstr = currentColorHex.substring(0, 2);//ff0000
            String Gstr = currentColorHex.substring(2, 4);
            String Bstr = currentColorHex.substring(4, 6);


            int fR = Integer.valueOf(Rstr, 16);//十六进制转十进制
            int fG = Integer.valueOf(Gstr, 16);
            int fB = Integer.valueOf(Bstr, 16);


            float ratio = ((progress * 1.0f) / sbMax);
            int laterR = (int) (fR * ratio);  //?
            int laterG = (int) (fG * ratio);  //?
            int laterB = (int) (fB * ratio);  //?

            //   Log.i(TAG, "laterR:" + laterR + " laterG:" + laterG + " laterB:" + laterB);

            String hexR = Integer.toHexString(laterR);//十进制转十六进制
            String hexG = Integer.toHexString(laterG);
            String hexB = Integer.toHexString(laterB);

            VDLog.i(TAG, "hexR:" + hexR + " hexG:" + hexG + " hexB:" + hexB);
            if (hexR.length() <= 1) {
                hexR = "0" + hexR;
            }
            if (hexG.length() <= 1) {
                hexG = "0" + hexG;
            }
            if (hexB.length() <= 1) {
                hexB = "0" + hexB;
            }


            String bufColorHex = hexR + hexG + hexB;


            this.command = "BA0F" + bufColorHex + "0FFFEE0D0A";
            if (bleManager != null) {
                bleManager.writeData(CHexConver.hexStr2Bytes(this.command), false);//CHexConver.hexStr2Bytes(
            }
        }
    }
}
