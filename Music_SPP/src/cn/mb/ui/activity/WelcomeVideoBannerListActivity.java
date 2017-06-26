package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.model.VideoInfoConfig;
import cn.mb.ui.base.BaseActivity;
import cn.mb.ui.common.BitmapCache;
import cn.mb.util.CommUtils;
import cn.mb.util.ImageUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class WelcomeVideoBannerListActivity extends BaseActivity implements OnItemClickListener{

	private int width, picHeight;
	private ListView listView;

	private boolean isVideo=true;
	BannerAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.welcome_video_banner);

		listView = (ListView) findViewById(R.id.pv_welcome_banner_list);

		width = getWidth(this);
		picHeight = (int) ((750 * width * 1.0f) / 1334);
		
		picHeight = (picHeight*2)/3;
		
		adapter =new BannerAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		
		Intent i = getIntent();
		isVideo= i.getBooleanExtra("isVideo", true);
		
		if(!isVideo){
			listConfig = AppConstant.getConfigList();
		}
	}

	private BitmapCache bcache = new BitmapCache();
	private class BannerAdapter extends BaseAdapter {

		private Context context;
		public BannerAdapter(Context context){
			this.context = context;
		}
		@Override
		public int getCount() {
			return AppConstant.bannerDarkDataList.length;
		}

		@Override
		public Object getItem(int position) {
			return AppConstant.bannerDarkDataList[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/*int resId = bannerDataList[position];
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(width, picHeight);
			ImageView iv = new ImageView(VideoWelcomeBannerListActivity.this);
			iv.setTag(resId);
			iv.setAdjustViewBounds(true);
			iv.setMaxWidth(width);
			iv.setMaxHeight(picHeight);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(mParams);
			iv.setImageDrawable(getResources().getDrawable(resId));*/
			
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(this.context).inflate(R.layout.welcome_video_banner_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.pv_bannber_img);
				RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(width, picHeight);
				holder.iv.setAdjustViewBounds(true);
				holder.iv.setMaxWidth(width);
				holder.iv.setMaxHeight(picHeight);
				holder.iv.setScaleType(ScaleType.FIT_XY);
				holder.iv.setLayoutParams(mParams);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			int resId = AppConstant.bannerDarkDataList[position];
			if(resId==AppAplication.currentBannerResId)resId=AppConstant.bannerDataList[position%AppConstant.bannerDataList.length];
//			holder.iv.setImageDrawable(getResources().getDrawable(resId));
		
			Bitmap bm = bcache.getBitmap(resId+"_upng");
			if(bm==null){
				bm= ImageUtils.readBitMap(appContext, resId);//new BitmapDrawable(appContext.getResources(), )
				bcache.putBitmap(resId+"_upng", bm);//
			}
			
			holder.iv.setImageBitmap(bm);
			return convertView;
		}
		private final class ViewHolder {
			ImageView iv;
		}
	}
	

	public static int getWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	public static int getHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		return height;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position>AppConstant.videoNameDataList.length-1)return;
		int p=position;
		if(isVideo){
//			VDNotic.alert(this, position+"");
			Intent intent = new Intent(WelcomeVideoActivity.WELCOME_PLAY_ACTION);
			intent.putExtra(WelcomeVideoActivity.WELCOME_BANNER_INDEX, position);
			sendBroadcast(intent);
		}else{
			//发送指令  灯光的
			if(p>(listConfig.size()))p=listConfig.size()-1;
			config = listConfig.get(p);
			if(config!=null)	sendLightCmd();
			
		}
		p=position;
		if(p>AppConstant.bgDataList.length-1)p=AppConstant.bgDataList.length-1;
		AppAplication.bgResId=AppConstant.bgDataList[p];
		
		p=position;
		if(p>AppConstant.bannerDarkDataList.length-1)p=AppConstant.bannerDarkDataList.length-1;
		AppAplication.currentBannerResId=AppConstant.bannerDarkDataList[p];
		
		adapter.notifyDataSetChanged();
	}
	private void sendLightCmd() {
		if (helper == null) helper = new SppConnectHelper("", null);
        if (bleManager == null) bleManager = helper.getInstance();
        
        
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
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB1.replace("FFFFFF", config.getRgb1().replace("#", ""))), false);
			} else if (what == 2) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB2.replace("FFFFFF", config.getRgb2().replace("#", ""))), false);
			} else if (what == 3) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB3.replace("FFFFFF", config.getRgb3().replace("#", ""))), false);
			}else if (what == 4) {
				bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB_LIGHT_MODEL.replace("NN", CommUtils.int2HexString(config.getRgbModel()))), false);
			}
		}
	};
	private List<VideoInfoConfig> listConfig = new ArrayList<VideoInfoConfig>();
    
	
}
