package cn.mb.ui.activity;

import com.simple.player.music.data.MusicData;
import com.simple.player.service.MediaPlayerService;
import com.simple.player.util.Constant;
import com.simple.player.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cn.mb.hk.music.R;
import cn.mb.http.VDNotic;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.ui.view.CircleProgressBar;
import cn.mb.ui.view.CircleProgressBar.CircleProgressCallBack;
import cn.mb.util.CommUtils;


public class MusicActivity extends InBaseActivity implements OnSeekBarChangeListener,OnClickListener,OnCheckedChangeListener,CircleProgressCallBack{

	private MyReceiver receiver;
	private SeekBar seekBar;
	private int pro;
	private TextView txCurrTime,txTotal,txTitle,txArtist;
	private ImageView btnPlay,btnPrevious,btnNext,btnList,btnVoiceSub,btnVoiceAdd;//,btnPlayModle;
	private RadioGroup modelRG;
	
	private CircleProgressBar wheel;
	private Intent intent;
	//播放列表
	private void init() {

		seekBar = (SeekBar) findViewById(R.id.sb_playmusic_sb);
		seekBar.setOnSeekBarChangeListener(this);

		txTitle = (TextView) findViewById(R.id.tx_playMusic_title);
	
		txCurrTime = (TextView) findViewById(R.id.tx_playMusic_currTime);
		txTotal = (TextView) findViewById(R.id.tx_playMusic_total);
		btnList = (ImageView) findViewById(R.id.pv_music_list_iv);
		
		


		btnPrevious = (ImageView) findViewById(R.id.pv_music_previous_iv);
		btnPlay = (ImageView) findViewById(R.id.pv_music_play_iv);
		btnNext = (ImageView) findViewById(R.id.pv_music_next_iv);
//		btnPlayModle = (ImageView) findViewById(R.id.btn_playMusic_playModle);
		btnList = (ImageView) findViewById(R.id.pv_music_list_iv);
		btnVoiceSub = (ImageView) findViewById(R.id.pv_voice_sub_iv);
		btnVoiceAdd = (ImageView) findViewById(R.id.pv_voice_add_iv);
		
		modelRG = (RadioGroup) findViewById(R.id.pv_music_play_model);
		modelRG.setOnCheckedChangeListener(this);

		
		wheel = (CircleProgressBar) findViewById(R.id.pv_music_progress);
		wheel.setCircleProgressCallBack(this);
		
		btnPrevious.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		btnNext.setOnClickListener(this);
//		btnPlayModle.setOnClickListener(this);
		btnList.setOnClickListener(this);
		btnVoiceSub.setOnClickListener(this);
		btnVoiceAdd.setOnClickListener(this);
		

		checkPlayModle();
		setPlayModle();
		
//		updataDisplay();
		
		intent = new Intent(this, MediaPlayerService.class);
	}

	private void updataDisplay() {
//		seekBar.setMax(Constant.duration);
//		seekBar.setProgress(Constant.seekBarCurr);
//		wheel.setMax(max);
		
		txTitle.setText(Constant.title);
//		txArtist.setText(Constant.artist);
		txTotal.setText(Constant.total);
		txCurrTime.setText(Util.toTime(Constant.seekBarCurr));
	}

	/*@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser){
			pro = progress;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		intent.putExtra(Constant.POSITION, pro);
		intent.putExtra(Constant.FLAG, Constant.SEEKBAR);
		startService(intent);
	}
*/
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser){
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//			seekBar.setpr
			displayVoice();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	
	}

	
	
	public void onClick(View v) {
		int id = v.getId();
		
		if(id== R.id.pv_music_play_iv){
	if(MusicData.allMusicList.size()>0)		play();
//		}else if(id==  R.id.btn_playMusic_back){
//			//finish();  刷新列表
//			refreshMusic();
	}else if(id==  R.id.pv_music_previous_iv){
		if(MusicData.allMusicList.size()>0)			previous();
}else if(id==   R.id.pv_music_next_iv){
	if(MusicData.allMusicList.size()>0)			next();
}
//else if(id==   R.id.btn_playMusic_playModle){
//			playModle();
//		}
		
else if(id==R.id.pv_music_list_iv)	{
	CommUtils.startActivity(this, MusicListActivity.class);
}	else if(id==R.id.pv_voice_sub_iv)	{
//	CommUtils.startActivity(this, MusicListActivity.class);
	subVoice();
}	else if(id==R.id.pv_voice_add_iv)	{
//	CommUtils.startActivity(this, MusicListActivity.class);
	addVoice();
}	
		
		
	}

	private void playModle() {
		switch(Constant.playModler){
		case Constant.loop:
//			btnPlayModle.setBackgroundResource(R.drawable.cycleall_selector);
			Constant.playModler = Constant.order;
			break;
		case Constant.order:
//			btnPlayModle.setBackgroundResource(R.drawable.random_selector);
			Constant.playModler = Constant.random;
			break;
		case Constant.random:
//			btnPlayModle.setBackgroundResource(R.drawable.cycleone_selector);
			Constant.playModler = Constant.loop;
			break;
		}
	}

	private void next() {
		intent.putExtra(Constant.FLAG, Constant.NEXT);
		startService(intent);
	}


	private void previous() {
		intent.putExtra(Constant.FLAG, Constant.PREVIOUS);
		startService(intent);
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

	private void startService() {
		intent.putExtra(Constant.FLAG, Constant.PLOPA);
		startService(intent);
	}

	private void checkPlayModle() {
		switch(Constant.currentState){
		case Constant.PLAY:
			btnPlay.setImageResource(R.drawable.pv_music_pause);
			break;
		case Constant.PAUSE:
			btnPlay.setImageResource(R.drawable.pv_music_play);
			break;
		}
	}
	
	private void setPlayModle(){
		switch(Constant.playModler){
		case Constant.loop:
//			btnPlayModle.setBackgroundResource(R.drawable.cycleone_selector);
			break;
		case Constant.order:
//			btnPlayModle.setBackgroundResource(R.drawable.cycleall_selector);
			break;
		case Constant.random:
//			btnPlayModle.setBackgroundResource(R.drawable.random_selector);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updataDisplay();
		checkPlayModle();
//		receiver = new MyReceiver();
//		IntentFilter filter = new IntentFilter(Constant.ACTION);
//		this.registerReceiver(receiver, filter);
		
		   int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
		    seekBar.setProgress(currentVolume);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		this.unregisterReceiver(receiver);
//		receiver = null;
	}

	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			int curr = intent.getIntExtra(Constant.CURR, 0);
//			int max = intent.getIntExtra(Constant.MAXCURR, 0);
//			seekBar.setMax(max);
			wheel.setProgress((curr*360)/(Constant.duration==0?1:Constant.duration));
//			seekBar.setProgress(curr);
			txCurrTime.setText(Util.toTime(curr)); 
//			txTotal.setText(Util.toTime(max));
			
			if(intent.getStringExtra(Constant.FLAG).equals(Constant.UPDATADIAPALY)){
				updataDisplay();
			}
		}
	}


	
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_music;
	}
	private  AudioManager mAudioManager ;
	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		init();
		
		
		mAudioManager	= (AudioManager) getSystemService(Context.AUDIO_SERVICE);  
//		    //最大音量  
		    int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
		    seekBar.setMax(maxVolume);
//		    //当前音量  
		    int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
		    seekBar.setProgress(currentVolume);
		
		    
		    receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(Constant.ACTION);
		this.registerReceiver(receiver, filter);	
		
	}
	
	
	private void subVoice(){
		 mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);  
	
	displayVoice();
	}
	private void addVoice(){
		 mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,  
                 AudioManager.FX_FOCUS_NAVIGATION_UP);  
		displayVoice();
		
		
	}

	private void displayVoice() {
		int v = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		seekBar.setProgress(v);
		VDNotic.alert(this, v +"");
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		this.unregisterReceiver(receiver);
		receiver = null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.pv_music_model_random:
			Constant.playModler = Constant.random;
			break;
		case R.id.pv_music_model_circul:
			Constant.playModler = Constant.order;
			break;
		default:
			break;
		}
	}

	@Override
	public void callbackDegress(int data) {
		pro = (data*Constant.duration)/360;
		intent.putExtra(Constant.POSITION, pro);
		intent.putExtra(Constant.FLAG, Constant.SEEKBAR);
		startService(intent);
	}
	
	
	
	@Override
	public boolean onKeyDown (int keyCode, KeyEvent event) {
	// 获取手机当前音量值
	switch (keyCode) {
		// 音量减小
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			displayVoice();
		break;
		// 音量增大
		case KeyEvent.KEYCODE_VOLUME_UP:
			displayVoice();
			break;
		}
	return super.onKeyDown (keyCode, event);
	}

}
