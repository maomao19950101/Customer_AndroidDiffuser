package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.mb.app.MySingleton;
import cn.mb.app.RequestUrls;
import cn.mb.hk.music.R;
import cn.mb.http.RequestCommand;
import cn.mb.http.RequestResult.RequestResultStatus;
import cn.mb.http.RequestResultPage;
import cn.mb.http.SimpleHttpUtil;
import cn.mb.http.SimpleHttpUtil.SimpleHttpCallback;
import cn.mb.http.VDNotic;
import cn.mb.model.VideoInfo;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class Pz02VideoListActivity extends InBaseActivity implements SimpleHttpCallback,OnItemClickListener{

	private ListView listView;
	private ImageLoader imageLoader;
	private List<VideoInfo> listData = new  ArrayList<VideoInfo>();
	private PZ02ListAdapter adapter;
	private SimpleHttpUtil httpUtil;
	private RequestResultPage reqPage;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		listView = (ListView) view.findViewById(R.id.pv_pz02_video_list);
		adapter = new PZ02ListAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		imageLoader = MySingleton.getInstance(this).getImageLoader();
		
		if (rightImgBtn != null) {
			rightImgBtn.setImageResource(R.drawable.pv_main_search);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
		httpUtil = new SimpleHttpUtil();httpUtil.addCallBack(this);
		loadMoreData();
		
	}

	private void loadMoreData() {

		Map<String, String> paramsMap = new HashMap<String, String>();
		// paramsMap.put("action", "VideoList");
		paramsMap.put("videoType", "1");
		paramsMap.put("pageSize", "100");
		paramsMap.put("pageNo", "1");

		httpUtil.postRequest(RequestUrls.GET_PZ02_VIDEO_LIST, paramsMap, RequestCommand.GET_PZ02_VIDEO_LIST, "Loading...", this);
	}
	
	@Override
	public void onResponseResult(int command, String resultStr) {
		if (command == RequestCommand.GET_PZ02_VIDEO_LIST) {
			Gson gson = new Gson();
			reqPage = gson.fromJson(resultStr, RequestResultPage.class);
			if (reqPage != null && reqPage.getStatus() == RequestResultStatus.REQUEST_SUCCESS) {
				listData = CommUtils.listKeyMaps(resultStr, "List", VideoInfo.class);
				
				adapter.notifyDataSetChanged();

//				if (reqPage.getTotalPage() >= pageNo)
//					pageNo++;

			} else {

				VDNotic.alert(this, reqPage.getMessage());
			}
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		VideoInfo videoInfo = listData.get(position);
		Intent i =new Intent();
		i.setClass(Pz02VideoListActivity.this, VideoRecipeListPz02Activity.class);
		Bundle b = new Bundle();
		b.putSerializable("pz02VideoInfo", videoInfo);
		i.putExtras(b);
		CommUtils.startActivity(Pz02VideoListActivity.this, i);
	}
	
	
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_pz2_video_list;
	}

	@Override
	protected void rightImgBtnClick() {
		super.rightImgBtnClick();
		CommUtils.startActivity(Pz02VideoListActivity.this, OilTreatmentsActivity.class);
	}
	
	private class PZ02ListAdapter extends BaseAdapter {

		private Context context;
		public PZ02ListAdapter(Context context){
			this.context = context;
		}
		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
		final	ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(this.context).inflate(R.layout.pv_pz2_video_list_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.pv_pz02_video_img);
				holder.name = (TextView) convertView.findViewById(R.id.pv_pz02_video_name);
				holder.time = (TextView) convertView.findViewById(R.id.pv_pz02_video_time);
				
				
				/*RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(width, picHeight);
				holder.iv.setAdjustViewBounds(true);
				holder.iv.setMaxWidth(width);
				holder.iv.setMaxHeight(picHeight);
				holder.iv.setScaleType(ScaleType.FIT_XY);
				holder.iv.setLayoutParams(mParams);*/
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			VideoInfo  video = listData.get(position);
			String imgUrl = video.getImgUrl();
		
			imageLoader.get(imgUrl,CommUtils.imageListener(holder.iv));
			
			holder.name.setText(video.getName());
			holder.time.setText(video.getTime());
			return convertView;
		}
		private final class ViewHolder {
			ImageView iv;
			TextView name;
			TextView time;
		}
	}

}
