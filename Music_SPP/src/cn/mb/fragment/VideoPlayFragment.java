package cn.mb.fragment;


import com.android.volley.DefaultRetryPolicy;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.ui.base.BaseFragment;
import cn.mb.util.DeviceUtils;

public class VideoPlayFragment extends BaseFragment{
	private TextureView mTextureView;
	private SurfaceTextureListener mSurfaceTextureListener;
	private Surface mSurface;
	private MediaPlayer mediaPlayer;
	private ImageView bgIV;
	private AssetManager am ;
	private AssetFileDescriptor afd;

	private int currentIndex=0;
	
    private boolean mVideoEnabled;
    private boolean mIsVisible;
	private boolean mIsFragmentPaused =true;
	private static String CURRENT_INDEX="currentIndex"; 
	public VideoPlayFragment() {
		  this.mVideoEnabled = true;
	      this.mIsVisible = false;
	}

	public static VideoPlayFragment newInstance(int currentIndex) {
		VideoPlayFragment ambianceFragment = new VideoPlayFragment();
		 Bundle bundle = new Bundle();
	        bundle.putInt(CURRENT_INDEX, currentIndex);
	        ambianceFragment.setArguments(bundle);

		return ambianceFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		currentIndex = getArguments().getInt(CURRENT_INDEX);
//		setRetainInstance(true);
	}
	
	@Override
	public View intitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.welcome_video_fragment, container, false);
		return view;
	}
	
	@Override
	public void setAfter() {
		super.setAfter();
		mTextureView = (TextureView) view.findViewById(R.id.video);
		mediaPlayer = new MediaPlayer();
		bgIV = (ImageView) view.findViewById(R.id.surface_img);
	}
	
    public void onResume() {
        super.onResume();
        this.mIsFragmentPaused = false;
        am = defaultActivity.getAssets();
        showBackground();
        play();
    }
    @Override
    public void onPause() {
    	super.onPause();
    	 this.mIsFragmentPaused = true;
    }
	 public void play() {
		 this.mVideoEnabled = getPreferences().isVideosEnabled();
	        if (!this.mVideoEnabled || VERSION.SDK_INT < 15) {
	            pause(true);
	            return;
	        }
	        setupTextureView();
	        setupVideo();
	    }
	
	private void setupTextureView() {
		this.mTextureView = (TextureView) view.findViewById(R.id.video);
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
				pause(true);
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
	                pause(true);
	                if (this.mSurface != null &&  this.mIsVisible) {
					 this.mediaPlayer = new MediaPlayer();
					this.mediaPlayer.reset();
					 afd = am.openFd(AppConstant.videoNameDataList[currentIndex]); 
					this.mediaPlayer.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(),
							afd.getLength());
//					this.mediaPlayer.setDataSource(am.openNonAssetFd("output_cover.m4v").getFileDescriptor());
					this.mediaPlayer.setSurface(this.mSurface);
					this.mediaPlayer.setLooping(true);
					bgIV.setVisibility(View.VISIBLE);
					this.mediaPlayer.prepareAsync();
					  this.mediaPlayer.setOnErrorListener(new OnErrorListener(){
						  @Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						return false;
					}}); 
					this.mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
						
						@Override
						public void onPrepared(MediaPlayer mp) {
							setSurfaceLayout(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
							mediaPlayer.start(); // 播放视频
							hideBackground(false);
						}
					});
				
	                
	                }
			}else{
				  pause(true);
	                showBackground();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private void pause(boolean z) {
        if (this.mediaPlayer == null) {
            return;
        }
        if (z) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
            return;
        }
        this.mediaPlayer.pause();
    }

	private void setSurfaceLayout(int videoWidth, int videoHeight) {
		LayoutParams lp = (LayoutParams) mTextureView.getLayoutParams();
		int windowWidth = DeviceUtils.getScreenWidth(defaultActivity);
		int windowHeight = DeviceUtils.getScreenHeight(defaultActivity);

		lp.width = (int) ((windowHeight * 1.0d / mediaPlayer.getVideoHeight()) * mediaPlayer.getVideoWidth());
		lp.height = windowHeight;
		lp.leftMargin = -((lp.width - windowWidth) / 2);
		// mTextureView.setLayoutParams(lp);
		// mTextureView.getHolder().setFixedSize(lp.width, lp.height);
		this.mTextureView.setScaleX(1.00001f);
		this.mTextureView.requestLayout();
		this.mTextureView.invalidate();
	}
	
	@Override
	public String getSelfTitle() {
		return "";
	}
	private void showBackground() {
        try {
            this.bgIV.setImageResource(AppConstant.bgDataList[currentIndex]);
        } catch (OutOfMemoryError e) {
            System.gc();
            this.bgIV.setImageResource(AppConstant.bgDataList[currentIndex]);
        }
        this.bgIV.setVisibility(View.VISIBLE);
    }

    private void hideBackground(boolean z) {
        if (z) {
            this.bgIV.setVisibility(View.GONE);
            this.bgIV.setImageBitmap(null);
            return;
        }
        this.bgIV.postDelayed(new Runnable() {
            public void run() {
                Animation alphaAnimation = new AlphaAnimation(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, 0.0f);
                alphaAnimation.setDuration(1000);
                alphaAnimation.setAnimationListener(new AnimationListener() {

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                    	bgIV.setVisibility(View.GONE);
                    	bgIV.setImageBitmap(null);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                bgIV.startAnimation(alphaAnimation);
            }
        }, 500);
    }
    
    @Override
    public void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	  if (this.mVideoEnabled) {
              pause(true);
          }
    }
    
    public Preferences getPreferences() {
        return Preferences.getInstance(defaultActivity.getApplicationContext());
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
    	super.setMenuVisibility(menuVisible);
    	 this.mIsVisible = menuVisible;
         if (VERSION.SDK_INT >= 15) {
             setupVideo();
         }
    }
}
