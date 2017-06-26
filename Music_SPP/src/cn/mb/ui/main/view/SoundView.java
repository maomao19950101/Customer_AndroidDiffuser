package cn.mb.ui.main.view;

import com.simple.player.music.data.MusicData;
import com.simple.player.service.MediaPlayerService;
import com.simple.player.util.Constant;
import com.simple.player.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.mb.hk.music.R;
import cn.mb.ui.activity.MusicListActivity;
import cn.mb.util.CommUtils;

public class SoundView extends MainBaseView implements OnSeekBarChangeListener{

	private View menuDiv,controlDiv;
	private ImageButton musicBtn,favBtn,closeBtn;
	private  AudioManager mAudioManager;
	private ImageButton playBtn,nextBtn,preBtn;
	private TextView musicNameTV,timeTV;
	private SeekBar voiceBar;
	private Intent intentMusic;
	private MusicStatusReceiver musicReceive;
	public SoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected int getResourceLayoutId() {
		return R.layout.pv_main_sound;
	}

	@Override
	protected void initView() {
		menuDiv = view.findViewById(R.id.pv_main_sound_menu_div);
		controlDiv = view.findViewById(R.id.pv_main_sound_music_div);
		
		musicBtn = (ImageButton) view.findViewById(R.id.pv_main_sound_music_btn);
		favBtn = (ImageButton) view.findViewById(R.id.pv_main_sound_fav_btn);
		closeBtn = (ImageButton) view.findViewById(R.id.pv_main_sound_music_close_btn);
		
		musicBtn.setOnClickListener(this);
		favBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);
		
		
		preBtn = (ImageButton) view.findViewById(R.id.pv_main_sound_music_pre_btn);
		playBtn = (ImageButton) view.findViewById(R.id.pv_main_sound_music_play_btn);
		nextBtn = (ImageButton) view.findViewById(R.id.pv_main_sound_music_next_btn);
		
		musicNameTV = (TextView) view.findViewById(R.id.pv_main_sound_music_name_tv);
		timeTV = (TextView) view.findViewById(R.id.pv_main_sound_music_time_tv);
		
		preBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		musicNameTV.setOnClickListener(this);
		
		voiceBar = (SeekBar) view.findViewById(R.id.pv_main_sound_music_voice_sb);
		
		voiceBar.setOnSeekBarChangeListener(this);
		
		if(intentMusic==null)intentMusic = new Intent(context, MediaPlayerService.class);
		musicReceive = new MusicStatusReceiver();
		
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);  
	}

	@Override
	public void show() {
//		menuDiv.startAnimation(animComeFromRight);
//		menuDiv.startAnimation(animComeFromTop);
		menuDiv.setVisibility(View.GONE);
//		controlDiv.setVisibility(View.GONE);
		controlDiv.startAnimation(animComeFromTop);
		controlDiv.setVisibility(View.VISIBLE);
		setVisibility(View.VISIBLE);
		if(musicReceive!=null){
			IntentFilter filter = new IntentFilter(Constant.ACTION);
			context.registerReceiver(musicReceive,filter);
		}
		voiceBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		voiceBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
	}

	@Override
	public void hide() {
		menuDiv.setVisibility(View.INVISIBLE);
		controlDiv.setVisibility(View.GONE);
		setVisibility(View.GONE);
		if(musicReceive!=null)context.unregisterReceiver(musicReceive);
		stop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_main_sound_music_btn:
			controlDiv.startAnimation(animComeFromTop);
			controlDiv.setVisibility(View.VISIBLE);
//			menuDiv.startAnimation(animGoToRight);
//			menuDiv.setVisibility(View.INVISIBLE);
			break;
		case R.id.pv_main_sound_fav_btn:
			
			break;
			
		case R.id.pv_main_sound_music_close_btn:
//			menuDiv.startAnimation(animComeFromRight);
//			menuDiv.setVisibility(View.VISIBLE);
			controlDiv.setVisibility(View.GONE);
			break;

		case R.id.pv_main_sound_music_pre_btn:
			if(MusicData.allMusicList.size()>0)	previous();
			break;
		
		case R.id.pv_main_sound_music_play_btn:
			play();
			break;
			
		case R.id.pv_main_sound_music_next_btn:
			if(MusicData.allMusicList.size()>0)	next();
			break;
			
		case R.id.pv_main_sound_music_name_tv:
			Intent i = new Intent();
			i.setClass(context, MusicListActivity.class);
			CommUtils.startActivity(context,i);
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser){
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//			displayVoice();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
	private void previous() {
		intentMusic.putExtra(Constant.FLAG, Constant.PREVIOUS);
		context.startService(intentMusic);
	}
	private void next() {
		intentMusic.putExtra(Constant.FLAG, Constant.NEXT);
		context.startService(intentMusic);
	}
	private void play() {
		switch(Constant.currentState){
		case Constant.PLAY:
			Constant.currentState = Constant.PAUSE;
			break;
		case Constant.PAUSE:
			Constant.currentState = Constant.PLAY;
			break;
		}
		checkPlayModle();
		startService();
	}
	private void stop() {
		Constant.currentState = Constant.PAUSE;
		checkPlayModle();
		startService();
	}
	private void startService() {
		intentMusic.putExtra(Constant.FLAG, Constant.PLOPA);
		context.startService(intentMusic);
	}

	private void checkPlayModle() {
		switch(Constant.currentState){
		case Constant.PLAY:
			playBtn.setImageResource(R.drawable.ic_sound_music_pause);
			break;
		case Constant.PAUSE:
			playBtn.setImageResource(R.drawable.ic_sound_music_play);
			break;
		}
	}
	private void updataDisplay() {
		musicNameTV.setText(Constant.title);
		checkPlayModle();
	}
	private class MusicStatusReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
//			System.out.println("-------------------------------  "+intent.getStringExtra(Constant.FLAG));
			int curr = intent.getIntExtra(Constant.CURR, 0);
//			int max = intent.getIntExtra(Constant.MAXCURR, 0);
			timeTV.setText(Util.toTime(curr)+"/"+Constant.total); 
			if(intent.getStringExtra(Constant.FLAG).equals(Constant.UPDATADIAPALY)||intent.getStringExtra(Constant.FLAG).equals(Constant.ALL)){
				updataDisplay();
			}
		}
	}
	
}
