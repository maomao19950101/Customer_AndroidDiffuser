package cn.mb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.app.MySingleton;
import cn.mb.hk.music.R;
import cn.mb.model.VideoInfo;
import cn.mb.model.entity.OilRecipeBean;
import cn.mb.model.entity.PlayedRecipeBean;
import cn.mb.ui.adapter.VideoPz02Adapter;
import cn.mb.ui.adapter.VideoPz02Adapter.VideoItemButtonClickListener;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;

/**
 * for pz02
 * 
 * @author JET
 *
 */
public class VideoRecipeListPz02Activity extends InBaseActivity implements OnClickListener {
	private ImageLoader imageLoader;
	private List<OilRecipeBean> listData = new ArrayList<OilRecipeBean>();
	private VideoPz02Adapter adapter;
	private ListView listView;
	private List<PlayedRecipeBean> playRecipeList = new ArrayList<PlayedRecipeBean>();
	private Button resetBtn, resumeBtn;
	private VideoInfo video;
private TextView videoName;
private ImageView videoImg;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		listView = (ListView) findViewById(R.id.pv_video_list);

		resetBtn = (Button) findViewById(R.id.pv_video_play_reset_btn);
		resetBtn.setOnClickListener(this);
		resumeBtn = (Button) findViewById(R.id.pv_video_play_resume_btn);
		resumeBtn.setOnClickListener(this);

		videoName = (TextView) findViewById(R.id.pv_pz02_video_tv);
		videoImg = (ImageView) findViewById(R.id.pv_pz02_video_iv);
		imageLoader = MySingleton.getInstance(this).getImageLoader();	
		// listView.setOnScrollListener(new
		// PauseOnScrollListener(AppAplication.getBitmapUtils(), false, true));

		Intent i = getIntent();
		Bundle b = i.getExtras();
		video = (VideoInfo) b.get("pz02VideoInfo");
	if(video!=null){
		listData = video.getRecipeConfigs();
		videoName.setText(video.getName());
		String imgUrl=video.getImgUrl();
		if(imgUrl!=null&&!"".equals(imgUrl)){ 
			imageLoader.get(imgUrl,CommUtils.imageListener(videoImg));
		}
	}

		// if (titleTV != null) {
		// titleTV.setText(title);
		// }
		adapter = new VideoPz02Adapter(this, playClickListener);

		listView.setAdapter(adapter);

		if (rightImgBtn != null) {
			rightImgBtn.setImageResource(R.drawable.pv_main_search);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_list2;
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUI();
	}

	private void updateUI() {
		if (adapter != null) {
			try {
				playRecipeList = AppAplication.db
						.findAll(Selector.from(PlayedRecipeBean.class).where("video_id", "=", video.getVideoId()));
			} catch (DbException e) {
				e.printStackTrace();
			}
			if (playRecipeList != null && playRecipeList.size() > 0) {
				for (OilRecipeBean recipe : listData) {
					for (PlayedRecipeBean recipePlayed : playRecipeList) {
						if (recipe.getId().equals(recipePlayed.getRecipeId()) && recipePlayed.getVideoId().equals(""))
							recipe.setPlayed(true);
					}
				}
			}
			adapter.mynotifyData(listData);
		}
	}

	private VideoItemButtonClickListener playClickListener = new VideoItemButtonClickListener() {

		@Override
		public void click() {
			Intent intent = new Intent();
			intent.setClass(VideoRecipeListPz02Activity.this, VideoOilsActivity2.class);
			Bundle b = new Bundle();
			b.putSerializable("video", video);
			b.putSerializable("videoType", "5");
			intent.putExtras(b);
			CommUtils.startActivity(VideoRecipeListPz02Activity.this, intent);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_video_play_reset_btn:
			try {
				AppAplication.db.delete(PlayedRecipeBean.class, WhereBuilder.b("video_id", "=", video.getVideoId()));
				updateUI();
			} catch (DbException e) {
				e.printStackTrace();
			}
			break;
		case R.id.pv_video_play_resume_btn:

			break;
		default:
			break;
		}
	}

	@Override
	protected void rightImgBtnClick() {
		super.rightImgBtnClick();
		CommUtils.startActivity(VideoRecipeListPz02Activity.this, OilTreatmentsActivity.class);
	}

}
