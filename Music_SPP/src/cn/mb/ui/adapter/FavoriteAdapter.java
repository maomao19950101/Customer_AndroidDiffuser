package cn.mb.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.mb.hk.music.R;
import cn.mb.model.entity.FavoritesInfo;

public class FavoriteAdapter  extends BaseAdapter {

	private List<FavoritesInfo> listData=new ArrayList<FavoritesInfo>();
	private Context context;
	private DelClickListener delListener;
	public FavoriteAdapter(Context context,DelClickListener delListener){
		this.context = context;
		this.delListener = delListener;
		
	}
	@Override
	public int getCount() {
		return  listData.size();
	}

	@Override
	public Object getItem(int position) {
		return  listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FavoriteViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new FavoriteViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.pv_favorite_list_item, null);
		
			viewHolder.colorView = convertView.findViewById(R.id.pv_fav_color_view);
			viewHolder.delIV = (ImageView) convertView.findViewById(R.id.pv_fav_color_del);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(FavoriteViewHolder) convertView.getTag();
		}
		
		FavoritesInfo fav = listData.get(position);
		viewHolder.colorView.setBackgroundColor(Color.parseColor(fav.colorHex()));

		viewHolder.delIV.setTag(fav);
		viewHolder.delIV.setOnClickListener(delListener);
		return convertView;
	}
	
	

	public void mynotifyData(List<FavoritesInfo> listData){
		this.listData=listData;
		this.notifyDataSetChanged();
	}
	
	public static abstract class DelClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			click((FavoritesInfo)v.getTag());
		}
		public abstract void click(FavoritesInfo fav);
	}
	
	private class FavoriteViewHolder{
		View colorView;
		ImageView delIV;
	}
}
