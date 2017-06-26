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

public class WelcomeSymptomListActivity extends InBaseActivity implements OnItemClickListener{

	private ListView listView;
	private int resId=R.array.brainsystem;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		listView = (ListView) findViewById(R.id.welcome_symptom_list);
		resId = getIntent().getIntExtra("category", R.array.brainsystem);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  R.layout.pv_item,R.id.pv_item_tx,getResources().getStringArray(resId));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.welcome_symptom_list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent i = new Intent();
		i.setClass(this, WelcomeSymptomDetailActivity.class);
		
		if(resId==R.array.brainsystem){
			if(position==4)i.putExtra("detailBg", R.drawable.ailment002);
			else if(position==5)i.putExtra("detailBg", R.drawable.ailment001);
			else return;
//			i.putExtra("detailBg", detailBg[position]);
		}else if(resId==R.array.respiratorysystem){
			if(position==1)i.putExtra("detailBg", R.drawable.ailment003);
			else if(position==2)i.putExtra("detailBg", R.drawable.ailment004);
			else return;
		}else if(resId==R.array.skin){
			if(position==0)i.putExtra("detailBg", R.drawable.ailment005);
			else return;
		}else return;
		
		CommUtils.startActivity(this, i);
	}
	
	private int[] detailBg={R.drawable.ailment001,R.drawable.ailment002,R.drawable.ailment003,R.drawable.ailment004,R.drawable.ailment005};
}
