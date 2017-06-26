package cn.mb.ui.activity;

import java.util.Date;

import com.lidroid.xutils.exception.DbException;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.model.entity.FavoritesInfo;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.ui.view.ColorPickerView;
import cn.mb.ui.view.ColorPickerView.OnColorChangedListener;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class ColorPickerActivity extends InBaseActivity implements OnSeekBarChangeListener{

	private final String TAG = "ColorPickerActivity";
	private ColorPickerView colorView;
	private String currentColorHex = "ff0000";
	protected View mPickedColorPreview;
private float hsv[]=new float[3];

private SeekBar sb1,sb2;
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_color_picker;
	}

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		colorView = (ColorPickerView) findViewById(R.id.pv_color_picker_view);

		colorView.setmColorChangeListener(mColorChangeListener);

		this.mPickedColorPreview = findViewById(R.id.pv_color_picker_preview);

		mPickedColorPreview.getBackground().setColorFilter( AppAplication.colorRGB, Mode.SRC_ATOP);
	
		colorView.changCenterCircleColor(AppAplication.colorRGB);
		if (rightImgBtn != null) {
			rightImgBtn.setImageResource(R.drawable.pv_main_fav);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
		
		currentColorHex = Integer.toHexString(AppAplication.colorRGB).substring(2);
		
		sb1=	((SeekBar)findViewById(R.id.pv_color_brightness_sb));
		sb1.setOnSeekBarChangeListener(this);;
		sb2=((SeekBar)findViewById(R.id.pv_color_saturation_sb));
		sb2.setOnSeekBarChangeListener(this);;
		
//		Color.RGBToHSV(red, green, blue, hsv);
//		Color.HSVToColor(hsv);
	}

	private ColorPickerView.OnColorChangedListener mColorChangeListener = new OnColorChangedListener() {

		@Override
		public void colorChanged(int color) {
			VDLog.i(TAG, "mColorChangeListener:" + color);
			if(color==-16777216)color=AppAplication.colorRGB;
			
			Message msg = Message.obtain();
			msg.what = 1;
			msg.arg1 = color;
			mh.sendMessageDelayed(msg, 10);
		}

		@Override
		public void colorChanged(int R, int G, int B) {
			if(R==0&&G==0&&B==0)return;
			mPickedColorPreview.getBackground().setColorFilter(Color.rgb(R, G, B), Mode.SRC_ATOP);
		}
	};
	private SppManager bleManager;
	private SppConnectHelper sppHelper;

	@Override
	protected void onResume() {
		super.onResume();
		if (sppHelper == null)
			sppHelper = new SppConnectHelper("", null);
		if (bleManager == null)
			bleManager = sppHelper.getInstance();
	}



	private Handler mh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (bleManager != null) {
					currentColorHex = Integer.toHexString(msg.arg1).substring(2);

					String	command = AppConstant.HardwareCommands.CMD_RGB_SAVE.replace("EEFFFF", currentColorHex);
					bleManager.writeData(CHexConver.hexStr2Bytes(command), false);// CHexConver.hexStr2Bytes(
					AppAplication.colorRGB = msg.arg1;
					AppAplication.colorRGBProcess = 10;
					CommUtils.saveColor(ColorPickerActivity.this, AppAplication.colorRGB);
					sb1.setProgress(sb1.getMax());
					sb2.setProgress(sb2.getMax());
				}
			}else if(msg.what==2){//亮度
				if (bleManager != null) {
					int process = (Integer) msg.obj;
					hsv(process,2);
				}
				
			}else if(msg.what==3){//饱和度
				if (bleManager != null) {
					int process = (Integer) msg.obj;
					hsv(process,3);
				}
			}
		};
	};

	private void hsv(int process,int type){
		  int  c = AppAplication.colorRGB;
          if (c == 0) {
              c = -1;
          }
          currentColorHex = Integer.toHexString(c).substring(2);
          String Rstr = currentColorHex.substring(0, 2);//ff0000
          String Gstr = currentColorHex.substring(2, 4);
          String Bstr = currentColorHex.substring(4, 6);

          int R = Integer.valueOf(Rstr, 16);//十六进制转十进制
          int G = Integer.valueOf(Gstr, 16);
          int B = Integer.valueOf(Bstr, 16);
          
          Color.RGBToHSV(R, G, B, hsv);
   if(process==0)process=1;       
          //亮度 v
          if(type==2){
        	  hsv[2] = hsv[2]*(((process)*1.0f/100));
          }else if(type==3){
        	  hsv[1] = hsv[1]*((process)*1.0f/100);
          }
          
          //饱和度 s
          
     c  =     Color.HSVToColor(hsv);
    String  bufColorHex = Integer.toHexString(c).substring(2);
    mPickedColorPreview.getBackground().setColorFilter(c, Mode.SRC_ATOP);
     if (bleManager != null) {
         bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB_SAVE.replace("EEFFFF", bufColorHex)), false);//CHexConver.hexStr2Bytes(
     }
	}
	
	protected void rightImgBtnClick() {

		FavoritesInfo entity = new FavoritesInfo();

		entity.setColorHex(currentColorHex);
		entity.setDateTime(CommUtils.getDateString(new Date()));

		try {
			AppAplication.db.save(entity);
			// favBtn.setEnabled(false);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser){
			Message msg = mh.obtainMessage();
			msg.obj = progress;
			if(seekBar.getId()==R.id.pv_color_brightness_sb){
				msg.what=2;
			}else if(seekBar.getId()==R.id.pv_color_saturation_sb){
				msg.what=3;
			}
			mh.sendMessage(msg);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	};
	
	
}
