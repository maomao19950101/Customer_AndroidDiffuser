package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

public class OilRecipeListNameActivity extends InBaseActivity implements SimpleHttpCallback, OnClickListener {
	private ImageView oil_recipe_control_arrow;
	private ListView oil_recipe_listView;
	private List<OilRecipeBean> mList = new ArrayList<OilRecipeBean>();
	private SimpleHttpUtil httpUtil;
	private RecipeAdapter adapter;

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		httpUtil = new SimpleHttpUtil();
		httpUtil.addCallBack(this);
		initView();
		initData("1", "");

		if (rightImgBtn != null) {
			rightImgBtn.setImageResource(R.drawable.pv_main_search);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.oil_recipe_control_arrow:
			showList();
			break;

		default:
			break;
		}
	}

	private void initView() {
		oil_recipe_control_arrow = (ImageView) this.findViewById(R.id.oil_recipe_control_arrow);
		oil_recipe_control_arrow.setOnClickListener(this);
		oil_recipe_listView = (ListView) this.findViewById(R.id.oil_recipe_listView);
		adapter = new RecipeAdapter(mList, this);
		oil_recipe_listView.setAdapter(adapter);
	}

	@Override
	protected void rightImgBtnClick() {
		super.rightImgBtnClick();

		CommUtils.startActivity(this, OilSortMenuActivity.class);
	}

	private void showList() {
		String tag = (String) oil_recipe_control_arrow.getTag();
		oil_recipe_control_arrow.setTag("false".equalsIgnoreCase(tag) ? "true" : "false");
		oil_recipe_control_arrow.setImageDrawable(
				getResources().getDrawable("false".equalsIgnoreCase(tag) ? R.drawable.table_arrow_down : R.drawable.table_arrow));
		oil_recipe_listView.setVisibility("false".equalsIgnoreCase(tag) ? View.VISIBLE : View.GONE);
	}

	private void initData(String type, String value) {

		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sortby", type);// 传1名称,2材料,3类别
		hashMap.put("value", value);

		httpUtil.postRequest(RequestUrls.GET_RECIPE_LIST, hashMap, RequestCommand.GET_RECIPE_LIST0, "Loading...", this);

	}

	private void showData() {
		adapter.myNotify(mList);
		oil_recipe_listView.setVisibility(View.VISIBLE);
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
		return R.layout.activity_oil_recipe_list_sorted;
	}

	private RequestResultPage reqPage;

	@Override
	public void onResponseResult(int command, String resultStr) {
		if (command == RequestCommand.GET_RECIPE_LIST0) {

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
