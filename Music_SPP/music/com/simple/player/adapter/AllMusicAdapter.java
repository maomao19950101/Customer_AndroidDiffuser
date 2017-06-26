package com.simple.player.adapter;

import com.simple.player.music.data.MusicData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.mb.hk.music.R;

public class AllMusicAdapter extends BaseAdapter {
	private Context context;
	
	public AllMusicAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return MusicData.allMusicList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Box box = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.pv_music_list_item, null);
			box = new Box();
			box.tvTitle = (TextView) convertView.findViewById(R.id.tvSongName);
			box.tvCount = (TextView) convertView.findViewById(R.id.tvArtistAlbum);
			box.tvTotal = (TextView) convertView.findViewById(R.id.tvTotal);
			convertView.setTag(box);
		}else{
			box = (Box) convertView.getTag();
		}
		
//			int duration = MusicData.allMusicList.get(position).getDuration();
//			String total = Util.toTime(duration);
			box.tvTitle.setText(MusicData.allMusicList.get(position).getTitle());
			box.tvTotal.setText(MusicData.allMusicList.get(position).getArtist());
			box.tvCount.setText(MusicData.allMusicList.get(position).getAlbum());
		return convertView;
	}

	private class Box{
		TextView tvTitle;
		TextView tvCount;
		TextView tvTotal;
	}
}
