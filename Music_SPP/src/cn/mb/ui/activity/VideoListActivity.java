package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.mb.app.AppAplication;
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
import cn.mb.model.entity.PlayVideoBakInfo;
import cn.mb.ui.adapter.VideoAdapter;
import cn.mb.ui.adapter.VideoAdapter.VideoItemButtonClickListener;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class VideoListActivity extends InBaseActivity
		implements SimpleHttpCallback, OnRefreshListener,OnClickListener {

	private ImageLoader imageLoader;
	private SimpleHttpUtil httpUtil;
	private RequestResultPage reqPage;
	private List<VideoInfo> listData = new ArrayList<VideoInfo>();
	private VideoAdapter adapter;
	private ListView listView;
	private SwipeRefreshLayout swipeRefresh;
	private int pageNo = 1;
	private String videoType = "1";
	
	private List<PlayVideoBakInfo> playVideoList=new ArrayList<PlayVideoBakInfo>();

	private Button resetBtn,resumeBtn;
	
	private TextView typeName;
	private ImageView typeImg,typeImg2;
	// private LoadMoreView loadMoreView;

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		httpUtil = new SimpleHttpUtil();
		httpUtil.addCallBack(this);

		listView = (ListView) findViewById(R.id.pv_video_list);
		swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.pv_video_list_refresh);

		resetBtn = (Button) findViewById(R.id.pv_video_play_reset_btn);resetBtn.setOnClickListener(this);
		resumeBtn = (Button) findViewById(R.id.pv_video_play_resume_btn);resumeBtn.setOnClickListener(this);
		
		// loadMoreView = (LoadMoreView) findViewById(R.id.pv_load_more_div);
		// loadMoreView.setLoadMoreListener(this);
		// loadMoreView.setLoaded();

		

		swipeRefresh.setOnRefreshListener(this);

		listView.setOnScrollListener(new PauseOnScrollListener(AppAplication.getBitmapUtils(), false, true));

		typeName = (TextView) findViewById(R.id.pv_video_type_name_tv);//android:id="@+id/pv_video_type_name_tv"
		typeImg = (ImageView) findViewById(R.id.pv_video_type_iv);
		typeImg2 = (ImageView) findViewById(R.id.pv_video_type_iv2);
		// listView.setOnItemClickListener(this);
		imageLoader = MySingleton.getInstance(this).getImageLoader();
		Intent i = getIntent();
		videoType = i.getStringExtra("videType");
		String title = i.getStringExtra("videTypeTitle");
		 String imgUrl= i.getStringExtra("videotypeImg");
		 String imgUrl2= i.getStringExtra("videotypeImg2");
		 
		if (titleTV != null) {
			titleTV.setText(title);
		}
		typeName.setText(title);
		adapter = new VideoAdapter(this, playClickListener);

		listView.setAdapter(adapter);
		loadMoreData();
		
		
		
		 if(rightImgBtn!=null){
	    	   rightImgBtn.setImageResource(R.drawable.pv_main_search);
	    	   rightImgBtn.setVisibility(View.VISIBLE);
	       }
		 
			if(imgUrl!=null&&!"".equals(imgUrl)){ 
				
				imageLoader.get(imgUrl,CommUtils.imageListener(typeImg));
				imageLoader.get(imgUrl2,CommUtils.imageListener(typeImg2));
				
			}
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_list;
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUI();
	}

	private void updateUI() {
		if(adapter!=null)	{
			try {
				playVideoList = AppAplication.db.findAll(Selector.from(PlayVideoBakInfo.class).where("video_type", "=", videoType));
			} catch (DbException e) {
				e.printStackTrace();
			}
			if(playVideoList!=null&&playVideoList.size()>0){
				for (PlayVideoBakInfo playVideo : playVideoList) {
					for (VideoInfo videoInfo : listData) {
						if(videoInfo.getVideoId().equals(playVideo.getVideoId()))videoInfo.setPlayed(true);
					}
				}
			}
			
			adapter.mynotifyData(listData);
		}
	}
	
	private void loadMoreData() {

		Map<String, String> paramsMap = new HashMap<String, String>();
		// paramsMap.put("action", "VideoList");
		paramsMap.put("videoType", videoType);
		paramsMap.put("pageSize", "10");
		paramsMap.put("pageNo", pageNo + "");

		httpUtil.postRequest(RequestUrls.GET_VIDEO_LIST, paramsMap, RequestCommand.GET_VIDEO_LIST, "Loading...", this);
	}

	@Override
	public void onResponseResult(int command, String resultStr) {
		if (command == RequestCommand.GET_VIDEO_LIST) {
			Gson gson = new Gson();
			reqPage = gson.fromJson(resultStr, RequestResultPage.class);
			if (reqPage != null && reqPage.getStatus() == RequestResultStatus.REQUEST_SUCCESS) {
				// List<VideoInfo> list = jsonParse(resultStr);
				List<VideoInfo> list = CommUtils.listKeyMaps(resultStr, "List", VideoInfo.class);
				
				try {
					playVideoList = AppAplication.db.findAll(Selector.from(PlayVideoBakInfo.class).where("video_type", "=", videoType));
				} catch (DbException e) {
					e.printStackTrace();
				}
				if(playVideoList!=null&&playVideoList.size()>0){
					for (PlayVideoBakInfo playVideo : playVideoList) {
						for (VideoInfo videoInfo : list) {
							if(videoInfo.getVideoId().equals(playVideo.getVideoId()))videoInfo.setPlayed(true);
						}
					}
				}
				listData.addAll(list);
				adapter.mynotifyData(listData);

				if (reqPage.getTotalPage() >= pageNo)
					pageNo++;

			} else {

				VDNotic.alert(this, reqPage.getMessage());
			}
		}
		dissRefush();
	}

	private void dissRefush() {
		listView.postDelayed(new Runnable() {

			@Override
			public void run() {
				swipeRefresh.setRefreshing(false);
				// loadMoreView.setLoaded();
			}
		}, 1000);
	}

	@Override
	public void onRefresh() {
		pageNo = 1;
		listData.clear();
		loadMoreData();
	}


	private VideoItemButtonClickListener playClickListener = new VideoItemButtonClickListener() {

		@Override
		public void click(VideoInfo videoInfo) {
			Intent intent = new Intent();
			// intent.setClass(VideoListActivity.this,
			// BBVideoPlayerActivity.class);
			
				intent.setClass(VideoListActivity.this, VideoOilsActivity.class);	
			Bundle b = new Bundle();
			// video.setCmdConfig(getConfig());
			b.putSerializable("video", videoInfo);
			b.putSerializable("videoType", videoType);
			intent.putExtras(b);
			CommUtils.startActivity(VideoListActivity.this, intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_video_play_reset_btn:
			try {
				AppAplication.db.delete(PlayVideoBakInfo.class, WhereBuilder.b("video_type", "=", videoType));
				
				updateUI();
			} catch (DbException e) {
				e.printStackTrace();
			}
			break;
		case R.id.pv_video_play_resume_btn:
			
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void rightImgBtnClick() {
		// TODO Auto-generated method stub
		super.rightImgBtnClick();
		
		CommUtils.startActivity(VideoListActivity.this, OilTreatmentsActivity.class);
	}
	
}
