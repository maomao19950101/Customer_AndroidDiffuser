package cn.mb.ui.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.app.MySingleton;
import cn.mb.hk.music.R;
import cn.mb.model.entity.OilRecipeBean;

public class RecipeAdapter extends BaseAdapter {

	private final class ViewHolder {
		TextView categoryName;
		TextView oil_recipe_name;
		TextView oil_recipe_oilOne;
		TextView oil_recipe_oilTwo;
		TextView oil_recipe_oilThree;

		ImageView oil_recipe_img_center;
		ImageView oil_recipe_img_left;
		ImageView oil_recipe_img_right;
		ImageView oil_recipe_store;
		ImageView oil_recipe_del;
	}

	private List<OilRecipeBean> list;
	private ImageLoader imageLoader;
	private Context contenxt;

	public RecipeAdapter(List<OilRecipeBean> list, Context contenxt) {
		this.list = list;
		this.contenxt = contenxt;
		imageLoader = MySingleton.getInstance(this.contenxt).getImageLoader();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(this.contenxt).inflate(R.layout.oil_recipe_essential, null);
			holder = new ViewHolder();
			holder.categoryName = (TextView) convertView.findViewById(R.id.categoryName);
			holder.oil_recipe_name = (TextView) convertView.findViewById(R.id.oil_recipe_name);
			holder.oil_recipe_oilOne = (TextView) convertView.findViewById(R.id.oil_recipe_oilOne);
			holder.oil_recipe_oilTwo = (TextView) convertView.findViewById(R.id.oil_recipe_oilTwo);
			holder.oil_recipe_oilThree = (TextView) convertView.findViewById(R.id.oil_recipe_oilThree);
			holder.oil_recipe_img_center = (ImageView) convertView.findViewById(R.id.oil_recipe_img_center);
			holder.oil_recipe_img_left = (ImageView) convertView.findViewById(R.id.oil_recipe_img_left);
			holder.oil_recipe_img_right = (ImageView) convertView.findViewById(R.id.oil_recipe_img_right);
			holder.oil_recipe_store = (ImageView) convertView.findViewById(R.id.oil_recipe_store);
			holder.oil_recipe_del = (ImageView) convertView.findViewById(R.id.oil_recipe_del);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		OilRecipeBean bean = list.get(position);

//		holder.categoryName.setVisibility(View.GONE);
		holder.oil_recipe_name.setText(bean.getName());
		holder.oil_recipe_oilOne.setText(bean.getOilOneName());
		holder.oil_recipe_oilTwo.setText(bean.getOilTwoName());
		holder.oil_recipe_oilThree.setText(bean.getOilThreeName());
//		String imgUrls[] = bean.getImg().split(";");

		// Get the ImageLoader through your singleton class.

	/*	imageLoader.get(imgUrls[0],
				ImageLoader.getImageListener(holder.oil_recipe_img_left, R.drawable.pv_img_defult, R.drawable.ic_error));
		imageLoader.get(imgUrls[1],
				ImageLoader.getImageListener(holder.oil_recipe_img_center, R.drawable.pv_img_defult, R.drawable.ic_error));
		imageLoader.get(imgUrls[2],
				ImageLoader.getImageListener(holder.oil_recipe_img_right, R.drawable.pv_img_defult, R.drawable.ic_error));
*/
		holder.oil_recipe_store.setTag(bean);
		holder.oil_recipe_store.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// store((OilRecipeBean) v.getTag());
			}
		});

		holder.oil_recipe_del.setTag(bean);
		holder.oil_recipe_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// del((OilRecipeBean) v.getTag());
			}
		});

		return convertView;
	}

	public void myNotify(List<OilRecipeBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}
}
