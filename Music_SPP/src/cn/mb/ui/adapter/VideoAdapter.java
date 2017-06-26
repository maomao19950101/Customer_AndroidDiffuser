package cn.mb.ui.adapter;

import java.util.ArrayList;
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
import cn.mb.model.VideoInfo;
import cn.mb.util.CommUtils;

public class VideoAdapter extends BaseAdapter {

    private List<VideoInfo> listData = new ArrayList<VideoInfo>();
    private Context context;
    private VideoItemButtonClickListener playClickListener, yunClickListener;
    private ImageLoader imageLoader;
    public VideoAdapter(Context context, VideoItemButtonClickListener ... listeners) {
        this.context = context;
        if (listeners.length >= 1) this.playClickListener = listeners[0];
//        if (listeners.length >= 2) this.yunClickListener = listeners[1];
        
    	imageLoader = MySingleton.getInstance(  this.context ).getImageLoader();
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
            convertView = inflater.inflate(R.layout.pv_video_list_item, null);

            viewHolder.videoPlay = (ImageView) convertView.findViewById(R.id.pv_video_play_iv);
            viewHolder.videoTitle = (TextView) convertView.findViewById(R.id.pv_video_name_tv);
            viewHolder.videoTime = (TextView) convertView.findViewById(R.id.pv_video_time_tv);
            viewHolder.videoYunIV = (ImageView) convertView.findViewById(R.id.pv_video_yun_iv);
           
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VideoViewHolder) convertView.getTag();
        }

        VideoInfo video = listData.get(position);
   

        if(video.isPlayed()){
        	 viewHolder.videoPlay.setImageResource(R.drawable.pv_video_played);
        	   viewHolder.videoPlay.setTag(video);
        	  viewHolder.videoTitle.setTextColor(context.getResources().getColor(R.color.pv_green_btn_bg));
        	  viewHolder.videoTime.setTextColor(context.getResources().getColor(R.color.pv_green_btn_bg));
        }else{
        	if(viewHolder.videoPlay.getTag()!=video)viewHolder.videoPlay.setImageResource(R.drawable.pv_video_oils_icon);
      	  viewHolder.videoTitle.setTextColor(context.getResources().getColor(R.color.pv_white));
      	  viewHolder.videoTime.setTextColor(context.getResources().getColor(R.color.pv_white));
        viewHolder.videoPlay.setTag(video);
        viewHolder.videoPlay.setOnClickListener(playClickListener);
//        viewHolder.videoYun.setTag(video);
//        viewHolder.videoYun.setOnClickListener(yunClickListener);
        }
        viewHolder.videoTitle.setText(video.getName());
        viewHolder.videoTime.setText(video.getTime());
        imageLoader.get(video.getImgUrl(),CommUtils.imageListener( viewHolder.videoYunIV));
        
        return convertView;
    }





    public void mynotifyData(List<VideoInfo> listData) {
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    public static abstract class VideoItemButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            click((VideoInfo) v.getTag());
        }

        public abstract void click(VideoInfo videoInfo);
    }
    
	private class VideoViewHolder {
		ImageView videoPlay;
		TextView videoTitle;
		TextView videoTime;
		ImageView videoYunIV;
	}
}
