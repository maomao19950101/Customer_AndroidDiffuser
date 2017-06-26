package cn.mb.ui.activity;

import java.util.List;

import com.lidroid.xutils.exception.DbException;
import com.simple.player.service.MediaPlayerService;
import com.simple.player.util.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.http.VDLog;
import cn.mb.model.VideoInfo;
import cn.mb.model.VideoInfoConfig;
import cn.mb.model.entity.PlayVideoBakInfo;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

public class VideoPlayerActivity extends InBaseActivity
		implements OnClickListener, OnBufferingUpdateListener, OnCompletionListener, OnErrorListener {
	private MediaPlayer mediaPlayer;
	private SeekBar seekbar;
	private ImageButton playBtn, setBtn;
	private TextView videoName;

	private TextureView mTextureView;
	private SurfaceTextureListener mSurfaceTextureListener;
	private Surface mSurface;

	private VideoInfo currentVideo;
	private Uri mUri;
	private String path = "";
	private UpdateSeekBar updateSb;
	private ImageView loadingIV;

	private String videoType;

	private View setDiv;
	
	   protected SppManager bleManager;
	    protected SppConnectHelper helper;
	@Override
	protected int getLayoutResId() {
		return R.layout.pv_video_player;
	}

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);

		mTextureView = (TextureView) findViewById(R.id.video);
		mediaPlayer = new MediaPlayer();

		playBtn = (ImageButton) findViewById(R.id.pv_video_pay_iv);
		setBtn = (ImageButton) findViewById(R.id.pv_video_set_iv);
		videoName = (TextView) findViewById(R.id.pv_video_name_tv);
		seekbar = (SeekBar) findViewById(R.id.pv_voice_sb);
		loadingIV = (ImageView) findViewById(R.id.surface_loading_img);
		setDiv = findViewById(R.id.pv_set_div);
		
		updateSb = new UpdateSeekBar(); // 创建更新进度条对象
		
		
		
		initData();

		// seekbar.setOnSeekBarChangeListener(new SurfaceSeekBar());

		playBtn.setOnClickListener(this);
		setBtn.setOnClickListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		startAnim();

		setupTextureView();
		// setupVideo();
		
		intent = new Intent(AppConstant.Action.PLAY_VIDEO_TIME_ACTION);
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstant.Action.PLAY_VIDEO_PLAY_ACTION);
		registerReceiver(receiver, filter);
		
		
		
		mTextureView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					return true;
				case MotionEvent.ACTION_UP:
					v.performClick();
					break;
				default:
					break;
				}
				return false;
			}
		});
		mTextureView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.removeCallbacks(runnableLayout);
				if(setDiv.getVisibility()==View.VISIBLE){
					setDiv.setVisibility(View.GONE);
				}else{
					setDiv.setVisibility(View.VISIBLE);
					handler.postDelayed(runnableLayout, 5000);
				}
				
			}
		});
		
		
		 Intent	intent = new Intent(this, MediaPlayerService.class);
		Constant.currentState = Constant.PAUSE;
		intent.putExtra(Constant.FLAG, Constant.PLOPA);
		startService(intent);
		
	}
	// Runnable延时关闭
		private Runnable runnableLayout = new Runnable() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
	private void initData() {
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		currentVideo = (VideoInfo) b.get("video");

//		currentVideo.setVideoUrl(
//				"http://14.152.72.72/youku/6571AAF4575388220B2E0C3F2D/030008070056B1C9035F542BEEFCF9C19992DE-D856-4DA6-680C-B846A996663C.mp4?&start=0");
		mUri = Uri.parse(currentVideo.getVideoUrl());
		path = currentVideo.getVideoUrl();
		videoType = (String) b.get("videoType");
		handler.sendEmptyMessageDelayed(1, 3000);
		 mh.sendEmptyMessageDelayed(1, 2500);
		 
		 videoName.setText(currentVideo.getvName());
	}

	private void setupTextureView() {
		this.mTextureView = (TextureView) findViewById(R.id.video);
		this.mSurfaceTextureListener = new SurfaceTextureListener() {

			public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
				mSurface = new Surface(surfaceTexture);
				if (mediaPlayer != null) {
					mediaPlayer.setSurface(mSurface);
				}
				setupVideo();
			}

			public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
				mSurface = new Surface(surfaceTexture);
				if (mediaPlayer != null) {
					mediaPlayer.setSurface(mSurface);
					setSurfaceLayout(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
				}
			}

			public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
				mSurface = null;
				return false;
			}

			public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
			}
		};
		this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
		if (this.mTextureView.isAvailable()) {
			this.mSurfaceTextureListener.onSurfaceTextureAvailable(this.mTextureView.getSurfaceTexture(), this.mTextureView.getWidth(),
					this.mTextureView.getHeight());
		}
	}

	private void setupVideo() {
		try {
			if (this.mediaPlayer == null || !this.mediaPlayer.isPlaying()) {
				if (this.mSurface != null && mUri != null) {
					// this.mediaPlayer = new MediaPlayer();
					this.mediaPlayer.setDataSource(this, mUri);
					this.mediaPlayer.setSurface(this.mSurface);
					this.mediaPlayer.setLooping(true);
					loadingIV.setVisibility(View.VISIBLE);
					this.mediaPlayer.prepareAsync();
					this.mediaPlayer.setOnPreparedListener(new PreparedListener());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
private Intent intent;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				setDiv.setVisibility(View.GONE);
				break;
			case 1:
				break;

			case 2:
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					int position = mediaPlayer.getCurrentPosition();
					int mMax = mediaPlayer.getDuration();
					int sMax = seekbar.getMax();
					seekbar.setProgress((int) ((position * sMax * 1.0f) / mMax));
					intent.putExtra("currentInt", position);
					intent.putExtra("duration", mMax);
					sendBroadcast(intent);
					// currentTV.setText(StringUtils.toTime(position));
					// totalTV.setText(StringUtils.toTime(mediaPlayer.getDuration()));
				}
				break;
			}
		}
	};

	private final class PreparedListener implements OnPreparedListener {
		@Override
		public void onPrepared(MediaPlayer mp) {
			loadingIV.setVisibility(View.GONE);
			mediaPlayer.start(); // 播放视频
			playBtn.setImageResource(R.drawable.v_pause);
			setSurfaceLayout(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
			handler.post(updateSb);
			// writeMedia();
		}
	}

	private class UpdateSeekBar implements Runnable {
		@Override
		public void run() {
			handler.sendEmptyMessage(2);
			handler.postDelayed(this, 1000);
		}
	}

	private AnimationDrawable loadingAnimationDrawable;

	private void startAnim() {
		loadingIV.setImageResource(R.anim.progress_7png);
		loadingAnimationDrawable = (AnimationDrawable) loadingIV.getDrawable();
		loadingAnimationDrawable.start();
	}

	// 修改 全屏展示
	private void setSurfaceLayout(int videoWidth, int videoHeight) {
		LayoutParams lp = (LayoutParams) mTextureView.getLayoutParams();
		int windowWidth = getScreenWidth(this);
		int windowHeight = getScreenHeight(this);

		lp.width = (int) ((windowHeight * 1.0d / mediaPlayer.getVideoHeight()) * mediaPlayer.getVideoWidth());
		lp.height = windowHeight;
		lp.leftMargin = -((lp.width - windowWidth) / 2);
		// mTextureView.setLayoutParams(lp);
		// mTextureView.getHolder().setFixedSize(lp.width, lp.height);
		this.mTextureView.setScaleX(1.00001f);
		this.mTextureView.requestLayout();
		this.mTextureView.invalidate();
	}

	@SuppressWarnings("deprecation")
	public int getScreenHeight(Context context) {
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display.getHeight();
	}

	/** 获取屏幕宽度 */
	@SuppressWarnings("deprecation")
	public int getScreenWidth(Context context) {
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display.getWidth();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		VDLog.i(TAG, "视频格式错误");
		mp.reset();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		playBtn.setImageResource(R.drawable.v_play);
		mediaPlayer.pause();
		saveplayVideo();
		 mh.sendEmptyMessageDelayed(5, 10);
	}

	private void saveplayVideo() {
		// 存储
		PlayVideoBakInfo entity = new PlayVideoBakInfo();
		entity.setVideoId(currentVideo.getVideoId());
		entity.setVideoUrl(currentVideo.getVideoUrl());
		entity.setVideoType(videoType);
		try {
			AppAplication.db.saveOrUpdate(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		VDLog.i(TAG, "百分比：" + percent);
		seekbar.setSecondaryProgress(percent);
		
//		if()
	}

	@Override
	protected void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (handler != null)
			handler.removeCallbacks(updateSb);
		
		if(receiver!=null)unregisterReceiver(receiver);
		 mh.sendEmptyMessageDelayed(5, 10);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pv_video_pay_iv:
			if (mediaPlayer == null)
				break;
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				playBtn.setImageResource(R.drawable.v_play);
			} else {
				playBtn.setImageResource(R.drawable.v_pause);
				mediaPlayer.start();
			}
			break;
		case R.id.pv_video_set_iv:
			Intent intent = new Intent();
			intent.putExtra("isPlay", mediaPlayer.isPlaying());
			intent.putExtra("videoName", currentVideo.getName());
			intent.putExtra("currentInt", mediaPlayer.getCurrentPosition());
			intent.putExtra("duration",mediaPlayer.getDuration());
			intent.setClass(this, VideoSetActivity.class);
			CommUtils.startActivity(this,intent);
			break;
		default:
			break;
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String s =intent.getAction();
			
			if(s.equals(AppConstant.Action.PLAY_VIDEO_PLAY_ACTION)){
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					playBtn.setImageResource(R.drawable.v_play);
				} else {
					playBtn.setImageResource(R.drawable.v_pause);
					mediaPlayer.start();
				}
			}
		}
		
	};

    @Override
    public void onResume() {
        super.onResume();


        if (helper == null) helper = new SppConnectHelper("", null);
        if (bleManager == null) bleManager = helper.getInstance();

    }
    //雾，时间，rgb1，2，3，model
    private Handler mh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                sendLightCmd();
            } else if (msg.what == 5) {
                sendComplete();
            }
        }
    };
    private void sendLightCmd() {
        if (currentVideo != null) {
        	List<VideoInfoConfig> configList = currentVideo.getCmdConfigs();
            VideoInfoConfig config =null;
            
            if(configList!=null&&configList.size()>0)config=configList.get(0);
            else 	config=getConfig();
            	
            int mistTime = config.getMistTime();
            //雾开,雾停顿时间，
            mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_MIST_ON), 5);
//            if (mistTime > 0) {
//            	String m=CommUtils.int2HexString(mistTime);
//            	 mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_INTERMITTENT_WORK_TIME.replace("mmmm", m+m)), 15);
//            }
            //工作时间
//			config.getLightWorkTime();
//			mh.sendMessageDelayed(1, 50);
            mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_RGB1.replace("FFFFFF", config.getRgb1().replace("#", ""))), 100);
            mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_RGB2.replace("FFFFFF", config.getRgb2().replace("#", ""))), 200);
            mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_RGB3.replace("FFFFFF", config.getRgb3().replace("#", ""))), 300);
            mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_RGB_LIGHT_MODEL.replace("NN", CommUtils.int2HexString(config.getRgbModel()))), 400);
            }

    }

    private void sendComplete() {
    	  mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_MIST_OFF), 5);//雾
    	  mh.postDelayed(new SendCmdThread(AppConstant.HardwareCommands.CMD_LIGHT_OFF), 10);//灯
    }
    
    private class SendCmdThread extends Thread {
        private String command;//	pool.execute(new SendCmdThread(progress));

        public SendCmdThread(String command) {
            this.command = command;
        }

        @Override
        public void run() {
            if (bleManager != null) {
                bleManager.writeData(CHexConver.hexStr2Bytes(this.command), false);//CHexConver.hexStr2Bytes(
            }
        }
    }
    private VideoInfoConfig getConfig() {
        VideoInfoConfig config = new VideoInfoConfig();
        config.setMistTime(1);
        config.setRgb1("f6a951");
        config.setRgb2("c81f19");
        config.setRgb3("19c7c8");
        config.setRgbModel(1);
        return config;
    }
    

}
