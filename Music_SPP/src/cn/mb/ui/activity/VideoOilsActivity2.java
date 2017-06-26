package cn.mb.ui.activity;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.app.AppConstant;
import cn.mb.app.MySingleton;
import cn.mb.hk.music.R;
import cn.mb.model.VideoInfo;
import cn.mb.model.entity.OilRecipeBean;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class VideoOilsActivity2 extends InBaseActivity {
	private TextView oil1Name,oil2Name,oil3Name;
	private VideoInfo	currentVideo;
	OilRecipeBean bean;
	private ImageLoader imageLoader;
	private String imgUrl="";
	private ImageView recipe3IV;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
		oil1Name = (TextView) findViewById(R.id.pv_oil1name_iv);
		oil2Name = (TextView) findViewById(R.id.pv_oil2name_iv);
		oil3Name = (TextView) findViewById(R.id.pv_oil3name_iv);
		
		recipe3IV = (ImageView) findViewById(R.id.pv_oils_rec_iv);
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
			currentVideo = (VideoInfo) b.get("video");
		if(currentVideo!=null){
			
		List<OilRecipeBean> list=	currentVideo.getRecipeConfigs();
			if(list!=null&&list.size()>0){
				bean = list.get(0);
				
				oil1Name.setText(bean.getOilOneName());
				oil2Name.setText(bean.getOilTwoName());
				oil3Name.setText(bean.getOilThreeName());
				
				imgUrl = bean.getImg();
			}
		}
		
		findViewById(R.id.pv_video_oils_play).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.setClass(VideoOilsActivity2.this, VideoPlayerActivity.class);
				CommUtils.startActivity(VideoOilsActivity2.this, intent);
				finish();
			}
		});
		
		imageLoader = MySingleton.getInstance(this).getImageLoader();
		imageLoader.get(imgUrl,CommUtils.imageListener(recipe3IV));
	
		
		handler.sendEmptyMessageDelayed(1, 500);
		handler.sendEmptyMessageDelayed(2, 530);
		handler.sendEmptyMessageDelayed(3, 560);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_oils_desc2;
	}

	protected SppManager bleManager;
	protected SppConnectHelper helper;

	@Override
	public void onResume() {
		super.onResume();
		if (helper == null)
			helper = new SppConnectHelper("", null);
		if (bleManager == null)
			bleManager = helper.getInstance();
		
		
	}
	
	
//	Message msg = Message.obtain();
//	msg.obj = progress;
//	handler.sendMessage(msg);
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			
			if (what == 1) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_MOTOR1.replace("NN", parseStr(bean.getOilOnePower()))), false);
			} else if (what == 2) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_MOTOR2.replace("NN", parseStr(bean.getOilTwoPower()))), false);
			} else if (what == 3) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_MOTOR3.replace("NN", parseStr(bean.getOilTwoPower()))), false);
			}
		};
	};
	
	
	private String parseStr(int value){
		String processStr = "10";
			String endC = Integer.toHexString(value);
			processStr = endC.length() <= 1 ? ("0" + endC) : endC;
			
			return processStr;
	}
}