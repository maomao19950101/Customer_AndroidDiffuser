package cn.mb.ui.main.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import cn.mb.hk.music.R;
import cn.sixpower.spp.SppManager;

public abstract class MainBaseView extends RelativeLayout implements OnClickListener{

	protected View view;
	protected Animation animComeFromRight;
	protected Animation animGoToRight;
	protected Animation animComeFromTop;
	protected Animation animGoToTop;
	protected Context context;
	protected SppManager bleManager;
	public MainBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		view = LayoutInflater.from(context).inflate(getResourceLayoutId(), this, true);
//		view.setBackground(new BitmapDrawable(getResources(), ImageUtils.readBitMap(context, AppAplication.bgResId)));
		
//		blur(ImageUtils.readBitMap(context, AppAplication.bgResId), view, 0.5f);
		initAnimation(context);
		initView();
	}


	private void initAnimation(Context context) {
		LinearInterpolator interpolator = new LinearInterpolator();
		animComeFromRight = AnimationUtils.loadAnimation(context, R.anim.come_from_right);
		animComeFromRight.setInterpolator(interpolator);
		animGoToRight = AnimationUtils.loadAnimation(context, R.anim.go_to_right);
		animGoToRight.setInterpolator(interpolator);
		animComeFromTop = AnimationUtils.loadAnimation(context, R.anim.come_from_top);
		animComeFromTop.setInterpolator(interpolator);
		animGoToTop = AnimationUtils.loadAnimation(context, R.anim.go_to_top);
 		animGoToTop.setInterpolator(interpolator);
	}
	protected abstract int getResourceLayoutId();
	protected abstract void initView();
	public abstract void show();
	public abstract void hide();
	
	public void show(SppManager bleManager){
		this.bleManager = bleManager;
		show();
	}
	
	@Override
	public void onClick(View v) {
		//menu
		//点击menu图标，菜单消失 下部呈现
//		colorDiv.startAnimation(animComeFromTop);
//		colorDiv.setVisibility(View.VISIBLE);
//		menuDiv.startAnimation(animGoToRight);
//		menuDiv.setVisibility(View.INVISIBLE);
//		break;
		
		//show
//		if(menuDiv==null||downDiv==null)return;
//		menuDiv.startAnimation(animComeFromRight);
//		menuDiv.setVisibility(View.VISIBLE);
//		downDiv.setVisibility(View.GONE);
//		setVisibility(View.VISIBLE);
		
		//hide
//		if(menuDiv==null||downDiv==null)return;
//		menuDiv.setVisibility(View.INVISIBLE);
//		downDiv.setVisibility(View.GONE);
//		setVisibility(View.GONE);
		
		
		//close
		
//		menuDiv.startAnimation(animComeFromRight);
//		menuDiv.setVisibility(View.VISIBLE);
//		cameraDiv.setVisibility(View.GONE);
//		colorDiv.setVisibility(View.GONE);
		
	}
	
	@SuppressLint("NewApi")
	private void blur(Bitmap bkg, float radius) {  
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);  
        Canvas canvas = new Canvas(overlay);  
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);  
        RenderScript rs = RenderScript.create(this.context);  
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);  
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());  
        blur.setInput(overlayAlloc);  
        blur.setRadius(radius);  
        blur.forEach(overlayAlloc);  
        overlayAlloc.copyTo(overlay);  
        view.setBackground(new BitmapDrawable(getResources(), overlay));  
        rs.destroy();  
    }  
}
