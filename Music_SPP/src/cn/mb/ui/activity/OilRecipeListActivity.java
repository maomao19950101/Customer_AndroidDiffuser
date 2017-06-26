package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import cn.mb.app.RequestUrls;
import cn.mb.hk.music.R;
import cn.mb.http.RequestCommand;
import cn.mb.http.RequestResult.RequestResultStatus;
import cn.mb.http.RequestResultPage;
import cn.mb.http.SimpleHttpUtil;
import cn.mb.http.SimpleHttpUtil.SimpleHttpCallback;
import cn.mb.http.VDNotic;
import cn.mb.model.entity.OilRecipeBean;
import cn.mb.ui.adapter.RecipeAdapter;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

public class OilRecipeListActivity extends InBaseActivity implements SimpleHttpCallback, View.OnClickListener {
	private ImageView oil_recipe_control_arrow;
	private ListView listView;
	public final static String PARAM_TYPE = "type";
	public final static String PRRAM_VALUE = "value";
	private List<OilRecipeBean> mList = new ArrayList<OilRecipeBean>();
	private SimpleHttpUtil httpUtil;
	private RecipeAdapter adapter;

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		httpUtil = new SimpleHttpUtil();
		httpUtil.addCallBack(this);
		initView();

		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.oil_recipe_control_arrow:
			isListVisible();
			break;
		default:
			break;
		}
	}

	private void initView() {
		oil_recipe_control_arrow = (ImageView) this.findViewById(R.id.oil_recipe_control_arrow);
		listView = (ListView) this.findViewById(R.id.essential_list);
		oil_recipe_control_arrow.setOnClickListener(this);

		adapter = new RecipeAdapter(mList, this);
		listView.setAdapter(adapter);
	}

	private void isListVisible() {
		String tag = (String) oil_recipe_control_arrow.getTag();
		oil_recipe_control_arrow.setTag("false".equalsIgnoreCase(tag) ? "true" : "false");
		oil_recipe_control_arrow.setImageDrawable(
				getResources().getDrawable("false".equalsIgnoreCase(tag) ? R.drawable.ic_choice : R.drawable.ic_choice));
		listView.setVisibility("false".equalsIgnoreCase(tag) ? View.VISIBLE : View.GONE);
	}

	private void initData() {
		String type = this.getIntent().getStringExtra(PARAM_TYPE);
		String value = this.getIntent().getStringExtra(PRRAM_VALUE);

		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sortby", type);// 传1名称,2材料,3类别
		hashMap.put("value", value);

		httpUtil.postRequest(RequestUrls.GET_RECIPE_LIST, hashMap, RequestCommand.GET_RECIPE_LIST, "Loading...", this);
	}

	private void showData() {
		adapter.myNotify(mList);
		listView.setVisibility(View.VISIBLE);
	}

	private void store(OilRecipeBean oilRecipeBean) {

	}

	private void del(OilRecipeBean bean) {
		if (mList.contains(bean)) {
			mList.remove(bean);
			showData();
		}
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_essential_oil_recipe_list;
	}

	private RequestResultPage reqPage;

	@Override
	public void onResponseResult(int command, String resultStr) {
		if (command == RequestCommand.GET_RECIPE_LIST) {

			Gson gson = new Gson();
			reqPage = gson.fromJson(resultStr, RequestResultPage.class);
			if (reqPage != null && reqPage.getStatus() == RequestResultStatus.REQUEST_SUCCESS) {
				mList = CommUtils.listKeyMaps(resultStr, "List", OilRecipeBean.class);
				showData();

			} else {

				VDNotic.alert(this, reqPage.getMessage());
			}
		}
	}

}
