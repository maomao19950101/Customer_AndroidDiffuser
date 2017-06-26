package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.fragment.VideoPlayFragment;
import cn.mb.hk.music.R;
import cn.mb.model.VideoInfoConfig;
import cn.mb.ui.base.BaseActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class WelcomeVideoActivity extends BaseActivity {

	private ViewPager videoViewPager;
	private SlidePagerAdapter mAdapter;
	private boolean isVideo=true;
	private ImageButton pBtn,cBtn,fBtn;
	private int videoListLength=AppConstant.videoNameDataList.length;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//保持全屏，不显示系统状态栏  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕不变黑  
		
		
		setContentView(R.layout.welcome_video);

		
		
		IntentFilter filter = new IntentFilter(WELCOME_PLAY_ACTION);
		registerReceiver(playReceive, filter);
		
		videoViewPager = (ViewPager) findViewById(R.id.pv_content_layout_vp);

		this.videoViewPager.setOffscreenPageLimit(1);
		FragmentManager fm = getSupportFragmentManager();
		mAdapter = new SlidePagerAdapter(fm);

		videoViewPager.setAdapter(mAdapter);
		videoViewPager.setOnPageChangeListener(mAdapter);
	
		
		pBtn=(ImageButton)	findViewById(R.id.pv_welcome_video_list);
		cBtn=(ImageButton)findViewById(R.id.pv_welcome_diff_btn);
		fBtn=(ImageButton)findViewById(R.id.pv_welcome_desc_btn);
		
		Intent i = getIntent();
		isVideo= i.getBooleanExtra("isVideo", true);
		
		if(!isVideo){
			listConfig = AppConstant.getConfigList();
			pBtn.setVisibility(View.GONE);
			cBtn.setVisibility(View.GONE);
			fBtn.setVisibility(View.GONE);
			
			if(!isVideo){	
				config = listConfig.get(0);
				if(config!=null)	sendLightCmd();
			}
		}	
		
		
		
		pBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = getIntent();
				i.setClass(WelcomeVideoActivity.this, WelcomeVideoBannerListActivity.class);
				CommUtils.startActivity(WelcomeVideoActivity.this, i);//WelcomeVideoBannerListActivity.class
			}
		});
		
		
		cBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommUtils.startActivity(WelcomeVideoActivity.this, DiffUserActivity.class);
			}
		});
		
		
		fBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommUtils.startActivity(WelcomeVideoActivity.this, WelcomeSelectTypeActivity.class);
			}
		});
	}

	private class SlidePagerAdapter extends FragmentStatePagerAdapter implements OnPageChangeListener {

		private int mCurrentIndex;
		private VideoPlayFragment mCurrentFragment;

		public SlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public void setPrimaryItem(View container, int position, Object obj) {
			if (this.mCurrentFragment != obj) {
				Object obj2 = this.mCurrentFragment == null ? 1 : null;
				this.mCurrentFragment = (VideoPlayFragment) obj;
				this.mCurrentIndex = position % videoListLength;
			}

			super.setPrimaryItem(container, position, obj);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public int getCount() {
			return videoListLength;
		}

		@Override
		public Fragment getItem(int position) {

			mCurrentIndex = position % videoListLength;
			return VideoPlayFragment.newInstance(mCurrentIndex);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int i) {
			this.mCurrentIndex = i % videoListLength;
			AppAplication.bgResId=AppConstant.bgDataList[mCurrentIndex];
			if(!isVideo){	
				config = listConfig.get(mCurrentIndex);
				if(config!=null)	sendLightCmd();
			}
		}

		@Override
		public int getItemPosition(Object object) {
			if (object instanceof VideoPlayFragment) {
				((VideoPlayFragment) object).play();
			}
			return super.getItemPosition(object);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
	@Override
	protected void onDestroy() {
		if(playReceive!=null)unregisterReceiver(playReceive);
		super.onDestroy();
	}
	
	public static final String WELCOME_PLAY_ACTION="welcome.play.action";
	public static final String WELCOME_BANNER_INDEX="welcome.banner.index";
	
	private BroadcastReceiver playReceive = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(WELCOME_PLAY_ACTION)){
				
				Message msg = Message.obtain();
				msg.arg1=intent.getIntExtra(WELCOME_BANNER_INDEX, 0);
				msg.what=1;
				h.sendMessageDelayed(msg, 10);
			}
		}
		
	};
	private Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				int index = msg.arg1;
				videoViewPager.setCurrentItem(index%videoListLength);
		}
		
	};
	};
	
	private void sendLightCmd() {
		if (helper == null)
			helper = new SppConnectHelper("", null);
		if (bleManager == null)
			bleManager = helper.getInstance();

		ledHandler.sendEmptyMessageDelayed(1, 50);
		ledHandler.sendEmptyMessageDelayed(2, 150);
		ledHandler.sendEmptyMessageDelayed(3, 250);
		ledHandler.sendEmptyMessageDelayed(4, 350);
	}

	private VideoInfoConfig config;
	protected SppManager bleManager;
	protected SppConnectHelper helper;
	private Handler ledHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				bleManager.writeData(CHexConver
						.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB1.replace("FFFFFF", config.getRgb1().replace("#", ""))), false);
			} else if (what == 2) {
				bleManager.writeData(CHexConver
						.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB2.replace("FFFFFF", config.getRgb2().replace("#", ""))), false);
			} else if (what == 3) {
				bleManager.writeData(CHexConver
						.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB3.replace("FFFFFF", config.getRgb3().replace("#", ""))), false);
			} else if (what == 4) {
				bleManager.writeData(CHexConver.hexStr2Bytes(
						AppConstant.HardwareCommands.CMD_RGB_LIGHT_MODEL.replace("NN", CommUtils.int2HexString(config.getRgbModel()))),
						false);
			}
		}
	};
	private List<VideoInfoConfig> listConfig = new ArrayList<VideoInfoConfig>();
	
}
