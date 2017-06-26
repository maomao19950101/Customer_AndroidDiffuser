package cn.mb.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.hk.music.R;
import cn.mb.model.entity.OilRecipeBean;

public class VideoPz02Adapter extends BaseAdapter {

    private List<OilRecipeBean> listData = new ArrayList<OilRecipeBean>();
    private Context context;
    private VideoItemButtonClickListener playClickListener;
    public VideoPz02Adapter(Context context, VideoItemButtonClickListener ... listeners) {
        this.context = context;
        if (listeners.length >= 1) this.playClickListener = listeners[0];
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final VideoViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new VideoViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pv_video_list_item2, null);

            viewHolder.videoPlay = (ImageView) convertView.findViewById(R.id.pv_pz02_video_play_iv);
            viewHolder.recipseTitle = (TextView) convertView.findViewById(R.id.pv_pz02_video_name_tv);
            viewHolder.oil1Name = (TextView) convertView.findViewById(R.id.pv_pz02_oil1_tv);
            viewHolder.oil2Name = (TextView) convertView.findViewById(R.id.pv_pz02_oil2_tv);
            viewHolder.oil3Name = (TextView) convertView.findViewById(R.id.pv_pz02_oil3_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VideoViewHolder) convertView.getTag();
        }

        OilRecipeBean recipe = listData.get(position);
   

        if(recipe.isPlayed()){//oils id&recipseid
        	 viewHolder.videoPlay.setImageResource(R.drawable.pv_video_played);
        	 viewHolder.videoPlay.setTag(recipe);
        	 viewHolder.recipseTitle.setTextColor(context.getResources().getColor(R.color.pv_green_btn_bg));
        }else{
        	Object o = viewHolder.videoPlay.getTag();
        	if(o!=null&&o!=recipe)viewHolder.videoPlay.setImageResource(R.drawable.pv_video_oils_icon);
      	  	viewHolder.recipseTitle.setTextColor(context.getResources().getColor(R.color.pv_white));
      	  	viewHolder.videoPlay.setTag(recipe);
      	  	viewHolder.videoPlay.setOnClickListener(playClickListener);
        }
        viewHolder.recipseTitle.setText(recipe.getCategoryName());
        viewHolder.oil1Name.setText(recipe.getOilOneName());
        viewHolder.oil2Name.setText(recipe.getOilTwoName());
        viewHolder.oil3Name.setText(recipe.getOilThreeName());
        return convertView;
    }





    public void mynotifyData(List<OilRecipeBean> listData) {
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    public static abstract class VideoItemButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            click();
        }

        public abstract void click();
    }
    
	private class VideoViewHolder {
		ImageView videoPlay;
		TextView recipseTitle;
		TextView oil1Name;
		TextView oil2Name;
		TextView oil3Name;
//		ImageView videoYun;
	}
}
