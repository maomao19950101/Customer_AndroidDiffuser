package cn.mb.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.mb.app.MySingleton;
import cn.mb.hk.music.R;
import cn.mb.model.VideoClassifyModel;
import cn.mb.util.CommUtils;

public class VideoClassifyAdapter extends BaseAdapter {

    private List<VideoClassifyModel> listData = new ArrayList<VideoClassifyModel>();
    private Context context;
    private ImageLoader imageLoader;
    public VideoClassifyAdapter(Context context) {
        this.context = context;
    	imageLoader = MySingleton.getInstance(this.context).getImageLoader();
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
    	final	ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(R.layout.pv_pz2_video_list_item, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.pv_pz02_video_img);//NetworkImageView
			holder.name = (TextView) convertView.findViewById(R.id.pv_pz02_video_name);
			holder.time = (TextView) convertView.findViewById(R.id.pv_pz02_video_time);
			holder.sessions = (TextView) convertView.findViewById(R.id.pv_pz02_video_sessioncount);
			
			
			/*RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(width, picHeight);
			holder.iv.setAdjustViewBounds(true);
			holder.iv.setMaxWidth(width);
			holder.iv.setMaxHeight(picHeight);
			holder.iv.setScaleType(ScaleType.FIT_XY);
			holder.iv.setLayoutParams(mParams);*/
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		VideoClassifyModel  classify = listData.get(position);
		String imgUrl = classify.getImgUrl();

		if(imgUrl!=null&&!imgUrl.equals(""))imageLoader.get(imgUrl,CommUtils.imageListener(holder.iv));
		
		holder.name.setText(classify.getClassifyName());
		holder.time.setText(classify.getTotalTime());
		holder.sessions.setText(classify.getSessionCount()+" Sessions");
		return convertView;
	}
	private final class ViewHolder {
		ImageView iv;
		TextView name;
		TextView time;
		TextView sessions;
	}





    public void mynotifyData(List<VideoClassifyModel> listData) {
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    
}
