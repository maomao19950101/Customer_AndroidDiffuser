package cn.mb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class WelcomeOilListActivity extends InBaseActivity implements OnItemClickListener{

	private ListView listView;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		
		listView = (ListView) findViewById(R.id.welcome_oils_list);
		listView.setOnItemClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  R.layout.pv_item,R.id.pv_item_tx,getResources().getStringArray(R.array.oils_local_list));
		listView.setAdapter(adapter);
	}
	
	
	
	@Override
	protected int getLayoutResId() {
		return R.layout.welcome_oil_list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position>=5)return;
		Intent i = new Intent();
		i.setClass(this, WelcomeOilDetailActivity.class);
		i.putExtra("detailBg", detailBg[position%5]);
		
		
		CommUtils.startActivity(this, i);
	}
	
	private int[] detailBg={R.drawable.oil001,R.drawable.oil002,R.drawable.oil003,R.drawable.oil004,R.drawable.oil005};
	
	
}
