package cn.mb.ui.main.view;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lidroid.xutils.exception.DbException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.model.entity.FavoritesInfo;
import cn.mb.ui.activity.CameraColorPickerActivity;
import cn.mb.ui.activity.ColorPickerActivity;
import cn.mb.ui.activity.FavoriteListActivity;
import cn.mb.ui.activity.WelcomeVideoActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;

public class LightView extends MainBaseView implements OnSeekBarChangeListener{

	private final String TAG = "LightView";
	private ImageButton colorBtn,cameraBtn,closeBtn,favBtn,movieBtn;
	private View menuDiv,colorControlDiv;
	// sb view
	private ImageButton colorViewBtn;
	private SeekBar brightnessSB;
	private ExecutorService pool = Executors.newFixedThreadPool(3);
	private int sbMax = 10;
	private int intentA=1;//color 还是 camera
	public LightView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected int getResourceLayoutId() {
		return R.layout.pv_main_light;
	}

	@Override
	protected void initView() {
		menuDiv = view.findViewById(R.id.pv_main_light_menu_div);
		colorControlDiv = view.findViewById(R.id.pv_main_light_control_div);
		colorBtn = (ImageButton) view.findViewById(R.id.pv_main_light_color_btn);
		cameraBtn = (ImageButton) view.findViewById(R.id.pv_main_light_camera_btn);
		closeBtn = (ImageButton) view.findViewById(R.id.pv_main_light_close_btn);
		favBtn = (ImageButton) view.findViewById(R.id.pv_main_light_fav_btn);
		movieBtn = (ImageButton) view.findViewById(R.id.pv_main_light_movie_btn);
		
		colorBtn.setOnClickListener(this);
		cameraBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);
		favBtn.setOnClickListener(this);
		movieBtn.setOnClickListener(this);
		
		colorViewBtn = (ImageButton) view.findViewById(R.id.pv_main_light_control_color_btn);
		brightnessSB = (SeekBar) view.findViewById(R.id.pv_main_light_control_brightness_sb);
		sbMax = brightnessSB.getMax();
		colorViewBtn.setOnClickListener(this);
		brightnessSB.setOnSeekBarChangeListener(this);
	}

	@Override
	public void show() {
		if(menuDiv==null||colorControlDiv==null)return;
//		menuDiv.startAnimation(animComeFromRight);
		menuDiv.startAnimation(animComeFromTop);
		menuDiv.setVisibility(View.VISIBLE);
		colorControlDiv.setVisibility(View.GONE);
		setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		if(menuDiv==null||colorControlDiv==null)return;
		menuDiv.setVisibility(View.INVISIBLE);
		colorControlDiv.setVisibility(View.GONE);
		setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_main_light_color_btn:
			/*intentA=1;
			colorControlDiv.startAnimation(animComeFromTop);
			colorControlDiv.setVisibility(View.VISIBLE);
			menuDiv.startAnimation(animGoToRight);
			menuDiv.setVisibility(View.INVISIBLE);
			setColorViewBtnBg();*/
			CommUtils.startActivity(context, new Intent(context, ColorPickerActivity.class));
			break;
		case R.id.pv_main_light_camera_btn:
			/*intentA=2;
			colorControlDiv.startAnimation(animComeFromTop);
			colorControlDiv.setVisibility(View.VISIBLE);
			menuDiv.startAnimation(animGoToRight);
			menuDiv.setVisibility(View.INVISIBLE);
			setColorViewBtnBg();*/
			CommUtils.startActivity(context, new Intent(context, CameraColorPickerActivity.class));
			break;
		case R.id.pv_main_light_close_btn:
			menuDiv.startAnimation(animComeFromRight);
			menuDiv.setVisibility(View.VISIBLE);
			colorControlDiv.setVisibility(View.GONE);
			break;
		case R.id.pv_main_light_control_color_btn:
			Intent intent = new Intent();
			if(intentA==2){
				intent.setClass(context, CameraColorPickerActivity.class);
			}else{
				intent.setClass(context, ColorPickerActivity.class);
			}
			CommUtils.startActivity(context, intent);
			
			menuDiv.startAnimation(animComeFromRight);
			menuDiv.setVisibility(View.VISIBLE);
			colorControlDiv.setVisibility(View.GONE);
			break;
		case R.id.pv_main_light_fav_btn:
//			saveFavColor();
			CommUtils.startActivity(context, FavoriteListActivity.class);
			break;
		case R.id.pv_main_light_movie_btn:
			Intent i = new Intent();
			i.setClass(context, WelcomeVideoActivity.class);
			i.putExtra("isVideo", false);
			CommUtils.startActivity(context,i);
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	
		  if (fromUser && progress > 0) {
	            pool.execute(new SendCmdThread(progress));
	        }
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	
	
	private class SendCmdThread extends Thread {
        private int progress = 1;
        private String command;
        private String currentColorHex = "ff0000";
        public SendCmdThread(int progress) {
            this.progress = progress;
            if(progress<1)progress=1;
        }

        @Override
        public void run() {
          int  c = AppAplication.colorRGB;
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
            
            
            VDLog.i(TAG, "laterR:" + laterR + " laterG:" + laterG + " laterB:" + laterB);

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

            this.command =AppConstant.HardwareCommands.CMD_RGB_SAVE.replace("EEFFFF", bufColorHex);
            if (bleManager != null) {
                bleManager.writeData(CHexConver.hexStr2Bytes(this.command), false);//CHexConver.hexStr2Bytes(
//                AppAplication.colorRGB = Color.rgb(laterR, laterG, laterB);
//                colorViewBtn.getBackground().setColorFilter(AppAplication.colorRGB, Mode.SRC_ATOP);
                colorViewBtn.getBackground().setColorFilter(Color.rgb(laterR, laterG, laterB), Mode.SRC_ATOP);
//                CommUtils.saveColor(context, AppAplication.colorRGB);
            }
        }
    }
	
	private void saveFavColor() {
		FavoritesInfo entity = new FavoritesInfo();
		entity.setColorHex(Integer.toHexString(AppAplication.colorRGB).substring(2));
		entity.setDateTime(CommUtils.getDateString(new Date()));
		try {
			AppAplication.db.save(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
  }
	private void setColorViewBtnBg(){
		 colorViewBtn.getBackground().setColorFilter(AppAplication.colorRGB, Mode.SRC_ATOP);
		 brightnessSB.setProgress(AppAplication.colorRGBProcess);
	}
}
