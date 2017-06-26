package cn.mb.ui.activity;

import java.util.Date;

import com.lidroid.xutils.exception.DbException;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import cn.mb.app.AppAplication;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.model.entity.FavoritesInfo;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.ui.view.CameraColorPickerPreview;
import cn.mb.ui.view.CameraColorPickerPreview.OnColorSelectedListener;
import cn.mb.util.Cameras;
import cn.mb.util.CommUtils;
import cn.sixpower.spp.CHexConver;
import cn.sixpower.spp.SppConnectHelper;
import cn.sixpower.spp.SppManager;

@SuppressWarnings("deprecation")
public class CameraColorPickerActivity extends InBaseActivity implements OnColorSelectedListener, OnClickListener,OnSeekBarChangeListener {
	protected static final long DELAY_HIDE_CONFIRM_SAVE_MESSAGE = 1400;
	protected static final long DURATION_CONFIRM_SAVE_MESSAGE = 400;
	protected static final String PICKED_COLOR_PROGRESS_PROPERTY_NAME = "pickedColorProgress";
	protected static final String SAVE_COMPLETED_PROGRESS_PROPERTY_NAME = "saveCompletedProgress";
	protected static final String TAG;

	protected Camera mCamera;
	protected CameraAsyncTask mCameraAsyncTask;
	protected CameraColorPickerPreview mCameraPreview;
	// protected TextView mColorPreviewText;
	protected Interpolator mConfirmSaveMessageInterpolator;
	protected boolean mIsFlashOn;
	protected boolean mIsPortrait;
	protected int mLastPickedColor;
	protected View mPickedColorPreview;
	protected View mPointerRing;
	protected FrameLayout mPreviewContainer;
	// protected View mSaveButton;
	// protected View mSaveCompletedIcon;
	protected float mSaveCompletedProgress;
	protected ObjectAnimator mSaveCompletedProgressAnimator;
	protected int mSelectedColor;
	protected float mTranslationDeltaX;
	protected float mTranslationDeltaY;
	protected ImageButton mFavButton;
	private String currentColorHex;
	private float hsv[]=new float[3];
	
	private SeekBar sb1,sb2;
	static {
		TAG = CameraColorPickerActivity.class.getSimpleName();
	}

	private class CameraAsyncTask extends AsyncTask<Void, Void, Camera> {
		protected LayoutParams mPreviewParams;

		private CameraAsyncTask() {
		}

		protected Camera doInBackground(Void... params) {
			Camera camera = CameraColorPickerActivity.getCameraInstance();
			if (camera == null) {
				finish();
			} else {
				Parameters cameraParameters = camera.getParameters();
				Size bestSize = Cameras.getBestPreviewSize(cameraParameters.getSupportedPreviewSizes(), mPreviewContainer.getWidth(),
						mPreviewContainer.getHeight(), mIsPortrait);
				cameraParameters.setPreviewSize(bestSize.width, bestSize.height);
				camera.setParameters(cameraParameters);
				Cameras.setCameraDisplayOrientation(CameraColorPickerActivity.this, camera);
				int[] adaptedDimension = Cameras.getProportionalDimension(bestSize, mPreviewContainer.getWidth(),
						mPreviewContainer.getHeight(), mIsPortrait);
				this.mPreviewParams = new LayoutParams(adaptedDimension[0], adaptedDimension[1]);
				this.mPreviewParams.gravity = 17;
			}
			return camera;
		}

		protected void onPostExecute(Camera camera) {
			super.onPostExecute(camera);
			if (!isCancelled()) {
				mCamera = camera;
				if (mCamera == null) {
					finish();
					return;
				}
				mCameraPreview = new CameraColorPickerPreview(CameraColorPickerActivity.this, mCamera);
				mCameraPreview.setOnColorSelectedListener(CameraColorPickerActivity.this);
				mCameraPreview.setOnClickListener(CameraColorPickerActivity.this);
				mPreviewContainer.addView(mCameraPreview, 0, this.mPreviewParams);
			}
		}

		protected void onCancelled(Camera camera) {
			super.onCancelled(camera);
			if (camera != null) {
				camera.release();
			}
		}
	}

	private static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return c;
	}

	@Override
	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		initSaveCompletedProgressAnimator();
		initViews();
		initTranslationDeltas();

		if (rightImgBtn != null) {
			rightImgBtn.setImageResource(R.drawable.pv_camera_flash_on);
			rightImgBtn.setVisibility(View.VISIBLE);
		}
		currentColorHex = Integer.toHexString(AppAplication.colorRGB).substring(2);
	
	sb1=	((SeekBar)findViewById(R.id.pv_color_brightness_sb));
	sb1.setOnSeekBarChangeListener(this);;
	sb2=	((SeekBar)findViewById(R.id.pv_color_saturation_sb));
	sb2.setOnSeekBarChangeListener(this);;
		
	}

	protected void onResume() {
		super.onResume();
		this.mCameraAsyncTask = new CameraAsyncTask();
		this.mCameraAsyncTask.execute(new Void[0]);

		if (sppHelper == null)
			sppHelper = new SppConnectHelper("", null);
		if (bleManager == null)
			bleManager = sppHelper.getInstance();
	}

	protected void onPause() {
		super.onPause();
		this.mCameraAsyncTask.cancel(true);
		if (this.mCamera != null) {
			this.mCamera.stopPreview();
			this.mCamera.setPreviewCallback(null);
			this.mCamera.release();
			this.mCamera = null;
		}
		if (this.mCameraPreview != null) {
			this.mPreviewContainer.removeView(this.mCameraPreview);
		}
	}

	public void onColorSelected(int color) {
		this.mSelectedColor = color;
		this.mPointerRing.getBackground().setColorFilter(color, Mode.SRC_ATOP);
	}

	public void onClick(View v) {
		if (v == this.mCameraPreview) {
			animatePickedColor(this.mSelectedColor);
		} else if (v.getId() == R.id.sys_header_rightbtn_ib2) {
			saveFavColor();
		}
	}

	protected void initViews() {
		this.mIsPortrait = getResources().getBoolean(R.bool.is_portrait);
		this.mPreviewContainer = (FrameLayout) findViewById(R.id.pv_camera_picker_preview_container);
		this.mPickedColorPreview = findViewById(R.id.pv_camera_color_picker_preview);
		this.mPointerRing = findViewById(R.id.pv_camera_color_picker_ring);
		this.mFavButton = (ImageButton) findViewById(R.id.sys_header_rightbtn_ib2);
		this.mFavButton.setOnClickListener(this);

		this.mConfirmSaveMessageInterpolator = new DecelerateInterpolator();
		// this.mLastPickedColor = ColorItems.getLastPickedColor(this);
		this.mLastPickedColor = AppAplication.colorRGB;
		this.mPickedColorPreview.getBackground().setColorFilter(AppAplication.colorRGB, Mode.SRC_ATOP);
	}

	@SuppressLint({ "NewApi" })
	protected void initTranslationDeltas() {
		ViewTreeObserver vto = this.mPointerRing.getViewTreeObserver();
		if (vto.isAlive()) {
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				public void onGlobalLayout() {
					ViewTreeObserver vto = mPointerRing.getViewTreeObserver();
					if (VERSION.SDK_INT <= 16) {
						vto.removeGlobalOnLayoutListener(this);
					} else {
						vto.removeOnGlobalLayoutListener(this);
					}
					Rect pointerRingRect = new Rect();
					Rect colorPreviewAnimatedRect = new Rect();
					mPointerRing.getGlobalVisibleRect(pointerRingRect);
					mTranslationDeltaX = (float) (pointerRingRect.left - colorPreviewAnimatedRect.left);
					mTranslationDeltaY = (float) (pointerRingRect.top - colorPreviewAnimatedRect.top);
				}
			});
		}
	}

	protected boolean isFlashSupported() {
		return getPackageManager().hasSystemFeature("android.hardware.camera.flash");
	}

	protected void toggleFlash() {
		if (this.mCamera != null) {
			Parameters parameters = this.mCamera.getParameters();
			parameters.setFlashMode(this.mIsFlashOn ? "off" : "torch");
			this.mCamera.stopPreview();
			this.mCamera.setParameters(parameters);
			this.mCamera.startPreview();
			this.mIsFlashOn = !this.mIsFlashOn;
		}
	}

	protected void initSaveCompletedProgressAnimator() {
		this.mSaveCompletedProgressAnimator = ObjectAnimator.ofFloat(this, SAVE_COMPLETED_PROGRESS_PROPERTY_NAME,
				new float[] { 1.0f, 0.0f });
	}

	protected void applyPreviewColor(int previewColor) {

		this.mPickedColorPreview.getBackground().setColorFilter(previewColor, Mode.SRC_ATOP);
		Message msg = Message.obtain();
		msg.what = 1;
		msg.arg1 = previewColor;
		mh.sendMessageDelayed(msg, 10);
	}

	protected void animatePickedColor(int pickedColor) {
		this.mLastPickedColor = pickedColor;
		applyPreviewColor(mLastPickedColor);
	}

	protected void saveFavColor() {
		FavoritesInfo entity = new FavoritesInfo();

		entity.setColorHex(currentColorHex);
		entity.setDateTime(CommUtils.getDateString(new Date()));

		try {
			AppAplication.db.save(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.pv_camera_color_picker;
	}

	private SppManager bleManager;
	private SppConnectHelper sppHelper;
	private Handler mh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (bleManager != null) {
					currentColorHex = Integer.toHexString(msg.arg1).substring(2);

					String	command = AppConstant.HardwareCommands.CMD_RGB_SAVE.replace("EEFFFF", currentColorHex);
					bleManager.writeData(CHexConver.hexStr2Bytes(command), false);// CHexConver.hexStr2Bytes(
					AppAplication.colorRGB = msg.arg1;
					AppAplication.colorRGBProcess = 10;
					CommUtils.saveColor(CameraColorPickerActivity.this, AppAplication.colorRGB);
				
					sb1.setProgress(sb1.getMax());
					sb2.setProgress(sb2.getMax());
				}
			}else if(msg.what==2){//亮度
				if (bleManager != null) {
					int process = (Integer) msg.obj;
					hsv(process,2);
				}
				
			}else if(msg.what==3){//饱和度
				if (bleManager != null) {
					int process = (Integer) msg.obj;
					hsv(process,3);
				}
			}
		};
	};

	protected void rightImgBtnClick() {
		toggleFlash();
		if (this.mIsFlashOn) {
			rightImgBtn.setImageResource(R.drawable.pv_camera_flash_off);
		} else {
			rightImgBtn.setImageResource(R.drawable.pv_camera_flash_on);
		}
	};
	private void hsv(int process,int type){
		  int  c = AppAplication.colorRGB;
        if (c == 0) {
            c = -1;
        }
        currentColorHex = Integer.toHexString(c).substring(2);
        String Rstr = currentColorHex.substring(0, 2);//ff0000
        String Gstr = currentColorHex.substring(2, 4);
        String Bstr = currentColorHex.substring(4, 6);

        int R = Integer.valueOf(Rstr, 16);//十六进制转十进制
        int G = Integer.valueOf(Gstr, 16);
        int B = Integer.valueOf(Bstr, 16);
        
        Color.RGBToHSV(R, G, B, hsv);
       if(process==0)process=1; 
        //亮度 v
        if(type==2){
      	  hsv[2] = hsv[2]*(((process)*1.0f/100));
        }else if(type==3){
      	  hsv[1] = hsv[1]*((process)*1.0f/100);
        }
        
        //饱和度 s
        
   c  =     Color.HSVToColor(hsv);
  String  bufColorHex = Integer.toHexString(c).substring(2);
  mPickedColorPreview.getBackground().setColorFilter(c, Mode.SRC_ATOP);
   if (bleManager != null) {
       bleManager.writeData(CHexConver.hexStr2Bytes(AppConstant.HardwareCommands.CMD_RGB_SAVE.replace("EEFFFF", bufColorHex)), false);//CHexConver.hexStr2Bytes(
   }
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(fromUser){
			Message msg = mh.obtainMessage();
			msg.obj = progress;
			if(seekBar.getId()==R.id.pv_color_brightness_sb){
				msg.what=2;
			}else if(seekBar.getId()==R.id.pv_color_saturation_sb){
				msg.what=3;
			}
			mh.sendMessage(msg);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	};
	
}