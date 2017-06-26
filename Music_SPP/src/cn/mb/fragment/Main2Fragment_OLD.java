package cn.mb.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.app.ProductType;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.ui.activity.FiveSensesActivity;
import cn.mb.ui.activity.Pz02VideoListActivity;
import cn.mb.ui.base.BaseFragment;
import cn.mb.ui.main.view.LightView;
import cn.mb.ui.main.view.MistView;
import cn.mb.ui.main.view.OilsView;
import cn.mb.ui.main.view.SoundView;
import cn.mb.ui.main.view.TimeView;
import cn.mb.ui.view.MistAnimView;
import cn.mb.ui.view.PaintState;
import cn.mb.ui.view.SunshineView;
import cn.mb.util.CommUtils;
import cn.mb.util.PreferencesUtils;
import cn.mb.util.Tools;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppAppApplication;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;
import cn.sixpower.spp.SppService.ResponseOrNotifyListener;

public class Main2Fragment_OLD extends BaseFragment implements OnClickListener {

	private ToggleButton mistTB, soundTB, lightTB, timeTB, motorTB,switchTB;
	private SppManager bleManager;
	private SppConnectHelper helper;
	private final String TAG = "Main2Fragment";

	private LinearLayout layoutLightAnim;
	private LinearLayout layoutMistAnim;
	private RelativeLayout layoutSoundAnim;
	private RelativeLayout layoutOilAnim;
	// sound
	private ImageView ivSound;
	private Animation rotateAnim;
	private AnimationDrawable rotateAnimDrawable;
	// Oil布局子布局
	private ImageView[] ivFlowers;
	private ImageView[] ivFlowersBg;
	// light
	private SunshineView sunshineView;
	// mist
	private MistAnimView mistAnimView;

	private String command = "";

	private OilsView oilsView;
	private LightView lightView;
	private MistView mistView;
	private SoundView soundView;
	private TimeView timeView;

	private ImageView productIV,productTypeIconIV;
	@Override
	public void setAfter() {
		super.setAfter();

//		defaultActivity.startService(new Intent(defaultActivity, SppService.class));

		TextView title = (TextView) view.findViewById(R.id.product_title);
		title.setText(SppAppApplication.connectDeviceName == null ? getResources().getString(R.string.pv_unknow_device)
				: SppAppApplication.connectDeviceName);

		AppAplication.colorRGB = getColor();
		productIV =  (ImageView) view.findViewById(R.id.activity_main_iv_pot);
		productTypeIconIV =  (ImageView) view.findViewById(R.id.product_type_iv);
		
		ivSound = (ImageView) view.findViewById(R.id.activity_main_iv_sound_anim);
		 if (ProductType.PZ02.getName().equalsIgnoreCase(title.getText().toString())) {
       	  AppAplication.productType=ProductType.PZ02;
       	  productIV.setImageDrawable(getResources().getDrawable(R.drawable.bg_pot2));
       	productTypeIconIV.setImageDrawable(getResources().getDrawable(R.drawable.pz_yun2));
       	  view.findViewById(R.id.pv_main_mist_div).setVisibility(View.GONE);
       	  view.findViewById(R.id.pv_main_motor_div).setVisibility(View.VISIBLE);
       	  
       	  
       	ivSound.setImageResource(R.drawable.bg_sound_2);
         }else{
       	  AppAplication.productType=ProductType.PZ01;
       	  productIV.setImageDrawable(getResources().getDrawable(R.drawable.bg_pot));
      	productTypeIconIV.setImageDrawable(getResources().getDrawable(R.drawable.pz_yun1));
       	  view.findViewById(R.id.pv_main_motor_div).setVisibility(View.GONE);
          view.findViewById(R.id.pv_main_mist_div).setVisibility(View.VISIBLE);
      	ivSound.setImageResource(R.drawable.bg_sound);
         }
		
		// 初始化动画效果
		initAnimation();
		// 初始化组件
		initWidget();

		view.findViewById(R.id.pv_main_presets_btn).setOnClickListener(this);
		oilsView = (OilsView) view.findViewById(R.id.pv_main_oils_view);
		lightView = (LightView) view.findViewById(R.id.pv_main_light_view);
		mistView = (MistView) view.findViewById(R.id.pv_main_mist_view);
		soundView = (SoundView) view.findViewById(R.id.pv_main_sound_view);
		timeView = (TimeView) view.findViewById(R.id.pv_main_time_view);

		mistTB = (ToggleButton) view.findViewById(R.id.pv_main_mist_tg);
		soundTB = (ToggleButton) view.findViewById(R.id.pv_main_sound_tg);
		lightTB = (ToggleButton) view.findViewById(R.id.pv_main_light_tg);
		timeTB = (ToggleButton) view.findViewById(R.id.pv_main_time_tg);
		motorTB = (ToggleButton) view.findViewById(R.id.pv_main_motor_tg);
		switchTB = (ToggleButton) view.findViewById(R.id.pv_main_onoff_tg);

		
		
		mistTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mistView.show(bleManager);
					// Light动画
					if (mistAnimView == null) {
						mistAnimView = new MistAnimView(defaultActivity);
					} else {
						mistAnimView.stopDraw();
					}
					int color = Color.rgb(00, 0x9F, 0xE3);
					// mistView.init(animMistSize, color, new PaintState() {
					mistAnimView.init(new int[] { layoutMistAnim.getWidth(), layoutMistAnim.getHeight() }, color, new PaintState() {
						@Override
						public void haveChanged(View v) {
							layoutMistAnim.removeAllViews();
							layoutMistAnim.addView(v);
						}
					});

				} else {
					mistView.hide();
					if (mistAnimView != null) {
						mistAnimView.stopDraw();
						layoutMistAnim.removeAllViews();
					}
				}
				command = isChecked ? AppConstant.HardwareCommands.CMD_MIST_ON : AppConstant.HardwareCommands.CMD_MIST_OFF;
				mh.sendEmptyMessage(1);
			}
		});
		soundTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					soundView.show();
					ivSound.setVisibility(View.VISIBLE);
//					startAnim();
					ivSound.setAnimation(rotateAnim);
				} else {
					soundView.hide();
					if (ivSound.getVisibility() == View.VISIBLE) {
						ivSound.clearAnimation();
//						stopAnim();
						ivSound.setVisibility(View.GONE);
					}
				}
				command = isChecked ? AppConstant.HardwareCommands.CMD_MUSIC_ON : AppConstant.HardwareCommands.CMD_MUSIC_OFF;
				mh.sendEmptyMessage(1);
			}
		});

		lightTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					lightView.show(bleManager);
					if (sunshineView == null) {
						sunshineView = new SunshineView(defaultActivity);
					} else {
						sunshineView.stopDraw();
					}
					int color = Color.rgb(0xFD, 0xC3, 0x00);
					// sunshineView.init(animLightSize, color, new PaintState()
					sunshineView.init(new int[] { layoutLightAnim.getWidth(), layoutLightAnim.getHeight() }, color, new PaintState() {
						@Override
						public void haveChanged(View v) {
							layoutLightAnim.removeAllViews();
							layoutLightAnim.addView(v);
						}
					});

				} else {
					lightView.hide();
					if (sunshineView != null) {
						sunshineView.stopDraw();
						layoutLightAnim.removeAllViews();
					}
				}
				command = isChecked ? AppConstant.HardwareCommands.CMD_LIGHT_ON : AppConstant.HardwareCommands.CMD_LIGHT_OFF;
				mh.sendEmptyMessage(1);
			}
		});
		timeTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					timeView.show(bleManager);
					mh.sendEmptyMessage(2);
				} else {
					timeView.hide();
				}
			}
		});

		motorTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				if (isChecked) {
					oilsView.show(bleManager);
					switchFlowers(true);
				} else {
					oilsView.hide();
					switchFlowers(false);
				}
			}
		});

		switchTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(!isChecked)	{
				mistTB.setChecked(isChecked);
				lightTB.setChecked(isChecked);
				soundTB.setChecked(isChecked);
				timeTB.setChecked(isChecked);
				motorTB.setChecked(isChecked);
			}	
				command = isChecked ? AppConstant.HardwareCommands.CMD_OPEN : AppConstant.HardwareCommands.CMD_CLOSE;
				mh.sendEmptyMessage(1);
			}
		});
		
		mh.sendEmptyMessageDelayed(3, 500);
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

	private Handler mh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (bleManager != null && !TextUtils.isEmpty(command)) {
					VDLog.i(TAG, "发送的命令:" + command);
					bleManager.writeData(CHexConver.hexStr2Bytes(command), false);
				}
			} else if (msg.what == 2) {
				 sendPhoneTimeCom();
			}else if(msg.what==3){
				getStatus();
			}
		};
	};

	private void sendPhoneTimeCom() {
		if (bleManager != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()); // 通过SimpleDateFormat获取24小时制时间
			String date = sdf.format(new Date());
			int[] times = new int[3];
			if (date.indexOf(":") != -1) {
				String s[] = date.split(":");
				times[0] = Integer.valueOf(s[0]);
				times[1] = Integer.valueOf(s[1]);
				times[2] = Integer.valueOf(s[2]);
			}

			// int[] starts = CommUtils.hourMin2Int(date);
			String dateStr = CommUtils.int2HexString(times[0]) + CommUtils.int2HexString(times[1]) + CommUtils.int2HexString(times[2]);
			String phoneTimeCom = AppConstant.HardwareCommands.CMD_PHONE_TIME.replace("hhmmss", dateStr);
			VDLog.i(TAG, "手机时间命令:" + phoneTimeCom);
			bleManager.writeData(CHexConver.hexStr2Bytes(phoneTimeCom), false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_main_presets_btn:
			if(AppAplication.productType.getName().equals(ProductType.PZ02.getName())){
				CommUtils.startActivity(defaultActivity, Pz02VideoListActivity.class);//Pz02VideoListActivity FiveSensesActivity
			}else{
				 CommUtils.startActivity(defaultActivity, FiveSensesActivity.class);
			}
            break;

		default:
			break;
		}
	}

	@Override
	public View intitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_main, container, false);
		return view;
	}

	

	@Override
	public String getSelfTitle() {
		return "";
	}
	
    private int getColor() {
        int color=PreferencesUtils.getInt(defaultActivity, AppConstant.KEY.LAST_COLOR_RGB);
        if(color==0||color==-1)color=-15728385;
        return color;
    }
    
    
    private void getStatus(){
    	bleManager.setResponseOrNotifyListener(responseListener);
    	// 发送LED MIST SOUND.
    	handler.sendEmptyMessageDelayed(7, 20);
    	handler.sendEmptyMessageDelayed(1, 50);
    	handler.sendEmptyMessageDelayed(2, 80);
    	handler.sendEmptyMessageDelayed(3, 110);
//    	handler.sendEmptyMessageDelayed(3, 110);
//    	handler.sendEmptyMessageDelayed(3, 140);
    	
//    	bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("CC", "16")), false);
    }
    private boolean pstatus=false,mstatus=false,lstatus=false,sstatus=false;
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		int what = msg.what;
    		if(what==1){//PERF
    			bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "04")), false);
    			pstatus=true;
    		}else if(what==2){//MUSIC
    			bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "0C")), false);
    			mstatus=true;
    		}else if(what==3){//LED
    			bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "0E")), false);
    			lstatus=true;
    		}else if(what==4){
    			lightTB.setChecked(true);
    		}else if(what==5){
    			soundTB.setChecked(true);
    		}else if(what==6){
    			mistTB.setChecked(true);
    		}else if(what==7){//读取整个开关状态
    			bleManager.writeData(CHexConver.hexStr2Bytes( AppConstant.HardwareCommands.CMD_READ_STATUS.replace("NN", "01")), false);
    			sstatus=true;
    		}else if(what==8){//发送整个设备 开
    			switchTB.setChecked(true);
    		}
    	};
    	
    };
    private ResponseOrNotifyListener  responseListener = new ResponseOrNotifyListener() {
		
		@Override
		public void responseOrNotify(int[] data) {
			int l = data.length;
			if (l > 2) {
				switch (data[1]) {
				case 0x04:
					if(data[4]==1&&pstatus){
						handler.sendEmptyMessageDelayed(6, 10);
					}
					pstatus=false;
					break;
				case 0x0c:
					if(data[4]==1&&mstatus)handler.sendEmptyMessageDelayed(5, 10);
					mstatus=false;
					break;
				case 0x0e:
if(data[4]==1&&lstatus)handler.sendEmptyMessageDelayed(4, 10);
lstatus=false;
					break;
				case 0x01:
					if(data[4]==1&&sstatus)handler.sendEmptyMessageDelayed(8, 10);
					sstatus=false;
										break;
				default:
					break;
				}

			}
			System.out.println(data + "   " + l);
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//连接蓝牙的时候动画
		private void startAnim(){
			ivSound.setImageResource(R.drawable.pv_sound_anim);
			rotateAnimDrawable = (AnimationDrawable) ivSound.getDrawable();
			rotateAnimDrawable.start();
		}
		
		private void stopAnim(){
			ivSound.setImageResource(R.drawable.pv_sound_anim);
			rotateAnimDrawable = (AnimationDrawable) ivSound.getDrawable();
			
			if(rotateAnimDrawable!=null){
				rotateAnimDrawable.selectDrawable(0);
				rotateAnimDrawable.stop();
			}
		}
	
	
	
	// 初始化组件
		private void initWidget() {
			int[] display = new Tools().getDisplayByPx(defaultActivity);
			LinearLayout.LayoutParams linearParams = null;
			RelativeLayout.LayoutParams relativeParams = null;
			int rectSize = (int) (display[0] * 0.3);
			// 动画父布局
			RelativeLayout layoutAnimParent = (RelativeLayout) view.findViewById(R.id.activity_main_layout_anim_parent);
			linearParams = (android.widget.LinearLayout.LayoutParams) layoutAnimParent.getLayoutParams();
			linearParams.width = display[0];
			linearParams.height = display[0];
			linearParams.setMargins(0, -display[0] / 10, 0, 0);
			layoutAnimParent.setLayoutParams(linearParams);
			// 固定布局
			ImageView ivPot = (ImageView) view.findViewById(R.id.activity_main_iv_pot);
			relativeParams = (android.widget.RelativeLayout.LayoutParams) ivPot.getLayoutParams();
			relativeParams.width = rectSize;
			relativeParams.height = rectSize;
			ivPot.setLayoutParams(relativeParams);
			// 动画布局
			layoutLightAnim = (LinearLayout) view.findViewById(R.id.activity_main_layout_light_anim);
			relativeParams = (LayoutParams) layoutLightAnim.getLayoutParams();
			relativeParams.width = (int) (rectSize * 1.2);
			relativeParams.height = (int) (rectSize * 1.2);
			layoutLightAnim.setLayoutParams(relativeParams);
			layoutMistAnim = (LinearLayout) view.findViewById(R.id.activity_main_layout_mist_anim);
			relativeParams = (LayoutParams) layoutMistAnim.getLayoutParams();
			relativeParams.width = rectSize * 2;
			relativeParams.height = rectSize * 2;
			layoutMistAnim.setLayoutParams(relativeParams);
			layoutSoundAnim = (RelativeLayout) view.findViewById(R.id.activity_main_layout_sound_anim);
			relativeParams = (LayoutParams) layoutSoundAnim.getLayoutParams();
			relativeParams.width = (int) (rectSize * 1.8);
			relativeParams.height = (int) (rectSize * 1.8);
			layoutSoundAnim.setLayoutParams(relativeParams);
//			ivSound = (ImageView) view.findViewById(R.id.activity_main_iv_sound_anim);
			
			
			layoutOilAnim = (RelativeLayout) view.findViewById(R.id.activity_main_layout_oil_anim);
			// TODO Oil
			ivFlowersBg = new ImageView[4];
			ivFlowersBg[0] = (ImageView) view.findViewById(R.id.layout_oil_anim_bg1);
			ivFlowersBg[1] = (ImageView) view.findViewById(R.id.layout_oil_anim_bg2);
			linearParams = (android.widget.LinearLayout.LayoutParams) ivFlowersBg[1].getLayoutParams();
			linearParams.setMargins(0, display[0] / 8, 0, 0);
			ivFlowersBg[1].setLayoutParams(linearParams);
			ivFlowersBg[2] = (ImageView) view.findViewById(R.id.layout_oil_anim_bg3);
			linearParams = (android.widget.LinearLayout.LayoutParams) ivFlowersBg[2].getLayoutParams();
			linearParams.setMargins(0, display[0] / 8, 0, 0);
			ivFlowersBg[2].setLayoutParams(linearParams);
			ivFlowersBg[3] = (ImageView) view.findViewById(R.id.layout_oil_anim_bg4);
			// Oil SwitchView

			int sideLength = display[0] / 15;
			ImageView ivOilBg = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_bg);
			relativeParams = (LayoutParams) ivOilBg.getLayoutParams();
			final float flowerScale = 0.7F;
			relativeParams.width = (int) (display[0] / 6 * 5 * flowerScale);
			relativeParams.setMargins(0, 0, 0, (int) (sideLength / 1.5 + sideLength / flowerScale));
			ivOilBg.setLayoutParams(relativeParams);
			// -->Flowers
			ivFlowers = new ImageView[9];
			relativeParams = (LayoutParams) layoutOilAnim.getLayoutParams();
			int flowerWidth = (int) (display[0] * flowerScale);
			relativeParams.width = flowerWidth;
			layoutOilAnim.setLayoutParams(relativeParams);

			// -->Flower1
			ivFlowers[0] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower1);
			relativeParams = (LayoutParams) ivFlowers[0].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position1 = new int[2];
			position1[0] = (int) (flowerWidth / 11.5) - sideLength / 2;
			position1[1] = flowerWidth / 18 - sideLength / 2;
			relativeParams.setMargins(position1[0], position1[1], 0, 0);
			ivFlowers[0].setLayoutParams(relativeParams);
			// -->Flower2
			ivFlowers[1] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower2);
			relativeParams = (LayoutParams) ivFlowers[1].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position2 = new int[2];
			position2[0] = flowerWidth / 5 - sideLength / 2;
			position2[1] = flowerWidth / 8 - sideLength / 2;
			relativeParams.setMargins(position2[0], position2[1], 0, 0);
			ivFlowers[1].setLayoutParams(relativeParams);
			// -->Flower3
			ivFlowers[2] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower3);
			relativeParams = (LayoutParams) ivFlowers[2].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position3 = new int[2];
			position3[0] = flowerWidth / 3 - sideLength / 2;
			position3[1] = (int) (flowerWidth / 3.2) - sideLength / 2;
			relativeParams.setMargins(position3[0], position3[1], 0, 0);
			ivFlowers[2].setLayoutParams(relativeParams);
			// -->Flower4
			ivFlowers[3] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower4);
			relativeParams = (LayoutParams) ivFlowers[3].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position4 = new int[2];
			position4[0] = (int) (flowerWidth / 2.54) - sideLength / 2;
			position4[1] = (int) (flowerWidth / 5) - sideLength / 2;
			relativeParams.setMargins(position4[0], position4[1], 0, 0);
			ivFlowers[3].setLayoutParams(relativeParams);
			// -->Flower5
			ivFlowers[4] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower5);
			relativeParams = (LayoutParams) ivFlowers[4].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position5 = new int[2];
			position5[0] = (int) (flowerWidth / 1.85) - sideLength / 2;
			position5[1] = (int) (flowerWidth / 2.72) - sideLength / 2;
			relativeParams.setMargins(position5[0], position5[1], 0, 0);
			ivFlowers[4].setLayoutParams(relativeParams);
			// -->Flower6
			ivFlowers[5] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower6);
			relativeParams = (LayoutParams) ivFlowers[5].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position6 = new int[2];
			position6[0] = (int) (flowerWidth / 1.645) - sideLength / 2;
			position6[1] = (int) (flowerWidth / 5.2) - sideLength / 2;
			relativeParams.setMargins(position6[0], position6[1], 0, 0);
			ivFlowers[5].setLayoutParams(relativeParams);
			// -->Flower7
			ivFlowers[6] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower7);
			relativeParams = (LayoutParams) ivFlowers[6].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position7 = new int[2];
			position7[0] = (int) (flowerWidth / 1.4) - sideLength / 2;
			position7[1] = (int) (flowerWidth / 3.15) - sideLength / 2;
			relativeParams.setMargins(position7[0], position7[1], 0, 0);
			ivFlowers[6].setLayoutParams(relativeParams);
			// -->Flower8
			ivFlowers[7] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower8);
			relativeParams = (LayoutParams) ivFlowers[7].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position8 = new int[2];
			position8[0] = (int) (flowerWidth / 1.2) - sideLength / 2;
			position8[1] = (int) (flowerWidth / 5.2) - sideLength / 2;
			relativeParams.setMargins(position8[0], position8[1], 0, 0);
			ivFlowers[7].setLayoutParams(relativeParams);
			// -->Flower9
			ivFlowers[8] = (ImageView) view.findViewById(R.id.layout_oil_anim_iv_flower9);
			relativeParams = (LayoutParams) ivFlowers[8].getLayoutParams();
			relativeParams.width = sideLength;
			relativeParams.height = sideLength;
			int[] position9 = new int[2];
			position9[0] = (int) (flowerWidth / 1.095) - sideLength / 2;
			position9[1] = (int) (flowerWidth / 15) - sideLength / 2;
			relativeParams.setMargins(position9[0], position9[1], 0, 0);
			ivFlowers[8].setLayoutParams(relativeParams);
		}

		// 初始化动画效果
		private void initAnimation() {
			LinearInterpolator interpolator = new LinearInterpolator();
			rotateAnim = AnimationUtils.loadAnimation(defaultActivity, R.anim.rotate_center);
			rotateAnim.setInterpolator(interpolator);
		}

		// 花朵的复杂动画
		private void switchFlowers(boolean willRuning) {
			if (!willRuning) {
				layoutOilAnim.setVisibility(View.INVISIBLE);
				for (int i = 0; i < ivFlowers.length; i++) {
					ivFlowers[i].clearAnimation();
					ivFlowers[i].setVisibility(View.GONE);
				}
				for (int i = 0; i < ivFlowersBg.length; i++) {
					ivFlowersBg[i].clearAnimation();
					ivFlowersBg[i].setVisibility(View.VISIBLE);
				}
			} else {
				layoutOilAnim.setVisibility(View.VISIBLE);
				for (int i = 0; i < ivFlowers.length; i++) {
					ivFlowers[i].clearAnimation();
					ivFlowers[i].setVisibility(View.GONE);
				}
				Animation oilAinmParent = AnimationUtils.loadAnimation(appContext, R.anim.go_to_bottom);
				oilAinmParent.setInterpolator(new LinearInterpolator());
				for (int i = 0; i < ivFlowersBg.length; i++) {
					ivFlowersBg[i].startAnimation(oilAinmParent);
					if (i == ivFlowersBg.length - 1) {
						oilAinmParent.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								Animation animationTemp;
								for (int i = 0; i < ivFlowers.length; i++) {
									animationTemp = AnimationUtils.loadAnimation(appContext, R.anim.come_from_small);
									animationTemp.setInterpolator(new LinearInterpolator());
									animationTemp.setStartOffset(i * 500);
									ivFlowers[i].startAnimation(animationTemp);
									ivFlowers[i].setVisibility(View.VISIBLE);
									if (i == 8) {
										// 开启花朵持续旋转动画
										keepFlowerToRotate(animationTemp);
									}
								}
							}
						});
					}
				}
			}
		}

		// 开启花朵持续旋转动画
		private void keepFlowerToRotate(Animation animation) {
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					Animation animationTemp;
					for (int i = 0; i < ivFlowers.length; i++) {
						ivFlowers[i].clearAnimation();
						animationTemp = AnimationUtils.loadAnimation(appContext, R.anim.keep_rotate);
						animationTemp.setInterpolator(new LinearInterpolator());
						animationTemp.setStartOffset(i * 500);
						ivFlowers[i].startAnimation(animationTemp);
					}
				}
			});

		}
}
