package cn.mb.ui.activity;

import com.simple.player.adapter.AllMusicAdapter;
import com.simple.player.music.data.MusicData;
import com.simple.player.service.MediaPlayerService;
import com.simple.player.util.Constant;
import com.simple.player.util.Util;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;

public class MusicListActivity extends InBaseActivity implements OnItemClickListener,OnRefreshListener {
	private AllMusicAdapter listAdapter;
	private MusicData musicData;
	private ListView musicListView;
	
	private RefreshMusicList musicListA;
	private boolean isRefreshing=false;
	
	private SwipeRefreshLayout swipeRefresh;
	
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_music_list;
	}

	private Intent intent;
	
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
		
		swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.pv_music_list_refresh);
		
		musicListView = (ListView) view.findViewById(R.id.pv_music_list);
		
		swipeRefresh.setOnRefreshListener(this);
		
		
		listAdapter = new AllMusicAdapter(this);
		
		musicListView.setAdapter(listAdapter);
		musicListView.setCacheColorHint(00000000);
//		musicListView.setSelector(R.drawable.list_item_natural);
		musicListView.setOnItemClickListener(this);
		 intent = new Intent(this,MediaPlayerService.class);
//		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		musicData = new MusicData();
		
		if(MusicData.allMusicList.size()==0)	handler.sendEmptyMessageDelayed(1, 200);
		
		if(rightImgBtn!=null){
			rightImgBtn.setImageResource(R.drawable.pv_refush);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
	}
	
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			refreshMusic();
		};
		
	};
//	private ServiceConnection  mServiceConnection = new ServiceConnection() {
//		
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			
//		}
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//		
//		}
//	};
	
	
	private class RefreshMusicList extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isRefreshing=true;
			swipeRefresh.setRefreshing(true);
		}
		@Override
		protected String doInBackground(String... arg0) {
			MusicData.allMusicList.clear();
//			scan();
			musicData.initAllMusicList(MusicListActivity.this);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			listAdapter.notifyDataSetChanged();
			isRefreshing=false;
			swipeRefresh.setRefreshing(false);
		}
	}
	
	
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Constant.currentArr = Constant.ALL;
		Constant.currentIndex = position;
		
		Constant.title = MusicData.allMusicList.get(position).getTitle();
		Constant.artist = MusicData.allMusicList.get(position).getArtist();
		Constant.duration = MusicData.allMusicList.get(position).getDuration();
		Constant.total = Util.toTime(MusicData.allMusicList.get(position).getDuration());
		Constant._data = MusicData.allMusicList.get(position).get_data();
		
		intent.putExtra(Constant.FLAG, Constant.ALL);  
		intent.putExtra(Constant.POSITION, position);
		
		startService(intent);
		Constant.currentState = Constant.PLAY;
//		checkPlayModle();
//		updataDisplay();
	}
	@Override
	public void onRefresh() {
		if(!isRefreshing)refreshMusic();
	}
	
	
	private void refreshMusic(){
		
		if(musicListA!=null&&!musicListA.isCancelled())musicListA.cancel(true);
		musicListA=new RefreshMusicList();
		musicListA.execute("");
		isRefreshing=true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		unbindService(mServiceConnection);
	}
	
	@Override
	protected void rightImgBtnClick() {
		onRefresh();
	}
}
