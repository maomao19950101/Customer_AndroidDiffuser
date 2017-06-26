package cn.mb.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class UserViewPager extends ViewPager {

	public UserViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean scrollble = true;

	public UserViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!scrollble) {
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (scrollble) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;
		}
	}
}
