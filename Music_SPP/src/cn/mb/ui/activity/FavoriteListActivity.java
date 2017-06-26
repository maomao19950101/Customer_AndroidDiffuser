package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.exception.DbException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;
import cn.mb.model.entity.FavoritesInfo;
import cn.mb.ui.adapter.FavoriteAdapter;
import cn.mb.ui.adapter.FavoriteAdapter.DelClickListener;
import cn.mb.ui.base.InBaseActivity;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class FavoriteListActivity extends InBaseActivity{

	private List<FavoritesInfo> listData = new ArrayList<FavoritesInfo>();
	
	private ListView listView;
	
	private FavoriteAdapter adapter;
	
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_favorite_list;
	}

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		listView = (ListView) findViewById(R.id.pv_favorite_color_list);
		adapter = new FavoriteAdapter(this, delListener);
		listView.setAdapter(adapter);
		
		loadData();
		
		listView.setOnItemClickListener(onItemClick);
	}
	private AdapterView.OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			FavoritesInfo fav = listData.get(position);
//			command="BA03"+fav.getColorHex()+"CCEEDA";
//			command="BA03"+fav.getColorHex()+"01FFEE0D0A";
//			command="BA0f"+fav.getColorHexChanged()+"01FFEE0D0A";
			command="BA0f"+fav.getColorHex()+"01FFEE0D0A";
			mh.sendEmptyMessageDelayed(1, 10);
		}
		
	};
	private DelClickListener delListener = new DelClickListener() {
		@Override
		public void click(FavoritesInfo fav) {
			try {
				listData.remove(fav);
				AppAplication.db.delete(fav);
				adapter.mynotifyData(listData);
			} catch (DbException e) {
				e.printStackTrace();
			}
			
			
		}
	};
	
	private void loadData(){
		new FavoriteListAsync().execute();
	}
	
	private class FavoriteListAsync extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			try {
				List<FavoritesInfo> list =	AppAplication.db.findAll(FavoritesInfo.class);
				if(list!=null)listData=list;
			} catch (DbException e) {
				e.printStackTrace();
			}
			return "";
		}
		@Override
		protected void onPostExecute(String result) {
			adapter.mynotifyData(listData);
		}
	}	
	
	
//	private String command="BA0300FF00CCEEDA";
	private String command="BA0300FF0001FFEE0D0A";
	private SppManager bleManager;
	private SppConnectHelper sppHelper;
	private Handler mh=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
			if(bleManager!=null){
				bleManager.writeData(CHexConver.hexStr2Bytes(command),false);//CHexConver.hexStr2Bytes(
			}
			}
		};
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		if(sppHelper==null)sppHelper = new SppConnectHelper("", null);
		if(bleManager==null)bleManager = sppHelper.getInstance();
	}
}

