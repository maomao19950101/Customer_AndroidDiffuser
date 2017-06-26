package cn.mb.ui.activity;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.app.MySingleton;
import cn.mb.hk.music.R;
import cn.mb.model.VideoInfo;
import cn.mb.model.entity.OilRecipeBean;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class VideoOilsActivity extends InBaseActivity {
	private TextView oil1Name,oil2Name,oil3Name,oil1Power,oil2Power,oil3Power;
	private ImageLoader imageLoader;
	private String imgUrl="";
	private ImageView recipe3IV;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
		oil1Name = (TextView) findViewById(R.id.pv_oil1name_iv);
		oil2Name = (TextView) findViewById(R.id.pv_oil2name_iv);
		oil3Name = (TextView) findViewById(R.id.pv_oil3name_iv);
		
		oil1Power = (TextView) findViewById(R.id.pv_oil1power_iv);
		oil2Power = (TextView) findViewById(R.id.pv_oil2power_iv);
		oil3Power = (TextView) findViewById(R.id.pv_oil3power_iv);
		
		recipe3IV = (ImageView) findViewById(R.id.pv_oils_rec_iv);
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		VideoInfo	currentVideo = (VideoInfo) b.get("video");
		if(currentVideo!=null){
			
		List<OilRecipeBean> list=	currentVideo.getRecipeConfigs();
			if(list!=null&&list.size()>0){
				OilRecipeBean bean = list.get(0);
				
				oil1Name.setText(bean.getOilOneName());
				oil2Name.setText(bean.getOilTwoName());
				oil3Name.setText(bean.getOilThreeName());
				oil1Power.setText(bean.getOilOnePower()+" drops");
				oil2Power.setText(bean.getOilTwoPower()+" drops");
				oil3Power.setText(bean.getOilThreePower()+" drops");
				imgUrl = bean.getImg();
			}
		}
		
		
		findViewById(R.id.pv_video_oils_play).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.setClass(VideoOilsActivity.this, VideoPlayerActivity.class);
				CommUtils.startActivity(VideoOilsActivity.this, intent);
				finish();
			}
		});
		
		imageLoader = MySingleton.getInstance(this).getImageLoader();
		imageLoader.get(imgUrl,CommUtils.imageListener(recipe3IV));
	}

	
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_oils_desc;
	}
}