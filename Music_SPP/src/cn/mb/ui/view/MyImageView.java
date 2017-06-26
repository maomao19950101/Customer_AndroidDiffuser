package cn.mb.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("ClickableViewAccessibility")
public class MyImageView extends ImageView {


	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyImageView(Context context) {
		super(context);
	}
	Drawable drawable;
	

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 if(gestureDetector.onTouchEvent(event))  
			             return true;  
			         else 
			              return false;  
		
		/*if (event.getAction() == MotionEvent.ACTION_DOWN) {
			drawable =getDrawable();
			drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
			setImageDrawable(drawable);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP||event.getAction() == MotionEvent.ACTION_CANCEL||event.getAction() == MotionEvent.ACTION_OUTSIDE||event.getAction() == MotionEvent.ACTION_MOVE) {
			drawable.clearColorFilter();
			setImageDrawable(drawable);
		}*/
//		return super.onTouchEvent(event);
	}

	
	private GestureDetector gestureDetector= new GestureDetector(new OnGestureListener(){

		@Override
		public boolean onDown(MotionEvent e) {
			drawable =getDrawable();
			drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
			setImageDrawable(drawable);
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			drawable.clearColorFilter();
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			drawable.clearColorFilter();
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			drawable.clearColorFilter();
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			drawable.clearColorFilter();
			return false;
		}

	});
}
