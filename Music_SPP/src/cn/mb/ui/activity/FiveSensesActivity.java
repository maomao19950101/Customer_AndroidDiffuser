package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.mb.app.RequestUrls;
import cn.mb.hk.music.R;
import cn.mb.http.RequestCommand;
import cn.mb.http.RequestResult.RequestResultStatus;
import cn.mb.http.RequestResultPage;
import cn.mb.http.SimpleHttpUtil;
import cn.mb.http.SimpleHttpUtil.SimpleHttpCallback;
import cn.mb.http.VDNotic;
import cn.mb.model.VideoClassifyModel;
import cn.mb.ui.adapter.VideoClassifyAdapter;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class FiveSensesActivity extends InBaseActivity implements SimpleHttpCallback,OnItemClickListener {
private VideoClassifyAdapter adapter;
	private SimpleHttpUtil httpUtil;
	private RequestResultPage reqPage;
	private ListView listView;
	  private List<VideoClassifyModel> listData = new ArrayList<VideoClassifyModel>();
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		listView = (ListView) findViewById(R.id.pv_video_type_list);
		adapter = new VideoClassifyAdapter(this);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		 if(rightImgBtn!=null){
	    	   rightImgBtn.setImageResource(R.drawable.pv_main_search);
	    	   rightImgBtn.setVisibility(View.VISIBLE);
	       }
		 httpUtil = new SimpleHttpUtil();httpUtil.addCallBack(this);
			loadMoreData();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_five_senses;
	}

	
	
	
	
	private void loadMoreData() {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("status", "0");
		paramsMap.put("pageSize", "100");
		paramsMap.put("pageNo", "1");

		httpUtil.postRequest(RequestUrls.GET_VIDEO_CLASSIFY_LIST, paramsMap, RequestCommand.GET_VIDEO_CLASSIFY_LIST, "Loading...", this);
	}
	
	@Override
	public void onResponseResult(int command, String resultStr) {
		if (command == RequestCommand.GET_VIDEO_CLASSIFY_LIST) {
			Gson gson = new Gson();
			reqPage = gson.fromJson(resultStr, RequestResultPage.class);
			if (reqPage != null && reqPage.getStatus() == RequestResultStatus.REQUEST_SUCCESS) {
				listData = CommUtils.listKeyMaps(resultStr, "List", VideoClassifyModel.class);
				adapter.mynotifyData(listData);
			} else {
				VDNotic.alert(this, reqPage.getMessage());
			}
		}
	}
	
	
	
	@Override
	protected void rightImgBtnClick() {
		super.rightImgBtnClick();
		CommUtils.startActivity(FiveSensesActivity.this, OilTreatmentsActivity.class);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		VideoClassifyModel classify = listData.get(position);
		if(classify!=null){
			Intent i = new Intent();
			i.setClass(this, VideoListActivity.class);
			i.putExtra("videType", classify.getClassifyId());
			i.putExtra("videTypeTitle", classify.getClassifyName());
			i.putExtra("videotypeImg", classify.getImgUrl());
			i.putExtra("videotypeImg2", classify.getImgUrl2());
			CommUtils.startActivity(this, i);
		}
	}
	
}
