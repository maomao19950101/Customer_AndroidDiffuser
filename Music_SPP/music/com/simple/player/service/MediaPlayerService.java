package com.simple.player.service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.simple.player.music.data.MusicData;
import com.simple.player.util.Constant;
import com.simple.player.util.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;
import cn.mb.hk.music.R;
import cn.mb.ui.activity.MusicActivity;

public class MediaPlayerService extends Service implements OnCompletionListener,OnErrorListener,Runnable{
	private MediaPlayer mp;
	private ExecutorService es;
	private boolean fl = true;
	private Intent intent;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mp = new MediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setOnCompletionListener(this);
		intent = new Intent(Constant.ACTION);
		
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(RECEIVE_CALL);// 来电通知 发送指令
		intentFilter.addAction(RECEIVE_SMS);// 来信息通知 发送指令
		
		registerReceiver(noticReceive, intentFilter);
		super.onCreate(); 
	} 

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		es = Executors.newSingleThreadExecutor();
		if(intent != null){
			String flag = intent.getStringExtra(Constant.FLAG);
			int position = intent.getIntExtra(Constant.POSITION, 0); 
			play(intent, position, flag);
		}
	}

	private void play(Intent intent, int position, String flag) {
		try {
			if(flag.equals(Constant.NEXT)){
				next();
			}else if(flag.equals(Constant.PREVIOUS)){
				previous();
			}else if(flag.equals(Constant.ALL)){
				String _data = MusicData.allMusicList.get(position).get_data();
				startPlay(_data);  
			
			}else if(flag.equals(Constant.SEEKBAR)){
				mp.seekTo(position);
			}else if(flag.equals(Constant.PLOPA)){
				switch(Constant.currentState){
				case Constant.PAUSE:
					mp.pause();
					break;
				case Constant.PLAY:
					mp.start();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startPlay(String _data) throws IOException {
	if(_data!=null&&!"".equals(_data)){
		reset(); 
		mp.setDataSource(_data);
		init();
		
		//2016-03-11 jet 添加
		sendBRDisplay();
	}
	}

	private void reset() {
		fl = false;
		mp.stop();
		mp.reset();
	}

	private void init() throws IOException {
//		if(es==null)return;
		mp.prepare();
		mp.start();
		//start 2015-11-03
		Constant.duration = mp.getDuration();
		Constant.total = Util.toTime(Constant.duration);
		//end
		fl = true;
		Constant.currentState = Constant.PLAY;
		es.execute(this);
		
		notic(getApplicationContext(), Constant.title);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mp.stop();
		
	if(notificationManager!=null)	notificationManager.cancelAll();
	if (noticReceive != null)
		unregisterReceiver(noticReceive);
	}

	public void run() {
		while(fl){
			if((mp.getCurrentPosition() < mp.getDuration())){
				intent.putExtra(Constant.FLAG, Constant.CURR);
				intent.putExtra(Constant.CURR, mp.getCurrentPosition());
//				intent.putExtra(Constant.MAXCURR, mp.getDuration());
				Constant.seekBarCurr = mp.getCurrentPosition();
				sendBroadcast(intent);
			}else{
				fl = false; 
			}
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {  
				e.printStackTrace();
			}
		}
	}

	public void onCompletion(MediaPlayer mp) {
		switch(Constant.playModler){
		case Constant.loop:
			loop();
			break;
		case Constant.order:
			next();
			break;
		case Constant.random:
			random();
			break;
		}
		Intent intent = new Intent(Constant.UPDATACTION);
		sendBroadcast(intent);
		sendBRDisplay();
	}

	private void next() {
		
		if(Constant.currentArr.equals(Constant.ALL)&&MusicData.allMusicList.size()>0){
			if(Constant.playModler==Constant.random){
				random();
			}else{
			if(Constant.currentIndex + 1 > MusicData.allMusicList.size() - 1){
				Constant.currentIndex = 0;
			}else{
				Constant.currentIndex++;
			}
			setAllMusic();
			play();
			}
		}
		sendBRDisplay();
	}

	private void previous() {
		if(Constant.currentArr.equals(Constant.ALL)&&MusicData.allMusicList.size()>0){
			if(Constant.currentIndex - 1 < 0){
				Constant.currentIndex = MusicData.allMusicList.size() - 1;
			}else{
				Constant.currentIndex--;
			}
			setAllMusic();
			play();
		}
		sendBRDisplay();
	}
	
	private void sendBRDisplay() {
		intent.putExtra(Constant.FLAG, Constant.UPDATADIAPALY);
		sendBroadcast(intent);
	}

	private void loop(){
		if(Constant.currentArr.equals(Constant.ALL)){
			setAllMusic();
			play();
		}
	}

	private void random() {
		if(Constant.currentArr.equals(Constant.ALL)){
			int size = MusicData.allMusicList.size();
			randomFactory(size);
			setAllMusic();
			play();
		}
	}

	private void randomFactory(int size) {
		Random random = new Random();
		Constant.currentIndex = random.nextInt(size);
	}

	private void setAllMusic() {
		Constant.title = MusicData.allMusicList.get(Constant.currentIndex).getTitle();
		Constant.artist = MusicData.allMusicList.get(Constant.currentIndex).getArtist();
		Constant.total = Util.toTime(MusicData.allMusicList.get(Constant.currentIndex).getDuration());
		Constant.duration = MusicData.allMusicList.get(Constant.currentIndex).getDuration();
		Constant._data = MusicData.allMusicList.get(Constant.currentIndex).get_data();
	}



	private void play(){
		try {
			if(Constant.currentArr.equals(Constant.ALL)){
				reset(); 
				mp.setDataSource(MusicData.allMusicList.get(Constant.currentIndex).get_data());
				init();  
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		return false;
	}
	NotificationManager notificationManager;
	private void notic(Context context, String text) {
		notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.pv_notic;
		notification.tickerText = text;
//		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_INSISTENT;		
		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.pv_notic);

		contentView.setImageViewResource(R.id.notic_img, R.drawable.pv_notic);

		contentView.setTextViewText(R.id.notic_txt, text);
		notification.contentView = contentView;

		Intent intent2 = new Intent();
		intent2.setClass(context, MusicActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		notificationManager.notify(0, notification);
	}
	
	
	//来电
			public static final String RECEIVE_CALL="android.intent.action.PHONE_STATE";//"TelephonyManager.CALL_STATE_RINGING";
			//短信
			public static final String RECEIVE_SMS="android.provider.Telephony.SMS_RECEIVED";
	private BroadcastReceiver noticReceive = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(RECEIVE_CALL)){
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
				switch (tm.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING://振铃
					Constant.currentState = Constant.PAUSE;
					mp.pause();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK://摘机
					Constant.currentState = Constant.PAUSE;
					mp.pause();
//					AiduApplication.isCalling=true;  //
					break;
				case TelephonyManager.CALL_STATE_IDLE://空闲
					Constant.currentState = Constant.PLAY;
					mp.start();//非通话时间
					break;
				}
			}
		};
	};
}
