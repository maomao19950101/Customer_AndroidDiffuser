package cn.mb.ui.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.ui.base.BaseActivity;
import cn.mb.util.ImageUtils;
import cn.mb.util.PreferencesUtils;

/**
 * slide
 *
 * @author JET
 */
public class GuideActivity extends BaseActivity {

    private ViewPager slidePager;
    private LinearLayout points;
    private int currentSelect = 0;
    private boolean flagSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flagSet = getIntent().getBooleanExtra("flagSet", false);
        if (flagSet == false && PreferencesUtils.getBoolean(this, AppConstant.KEY.IS_FIRST_GUIDE, true) == false) {
            openActivity();
            return;
        }
        setContentView(R.layout.pv_guide);
        initView();
    }

    private void prepareData() {
        ImageView iv;
        View v;
        int[] all = getImageResIds();
        for (int i : all) {
            View view = LayoutInflater.from(this).inflate(R.layout.pv_guide_item, null);

            iv = (ImageView) view.findViewById(R.id.sys_guide_img);

            // iv.setBackgroundDrawable(new
            // BitmapDrawable(ImageUtils.readBitMap(this, i)));
            iv.setScaleType(ScaleType.CENTER_CROP);
            iv.setImageDrawable(new BitmapDrawable(appContext.getResources(), ImageUtils.readBitMap(this, i)));
            v = new View(this);
            v.setBackground(getResources().getDrawable(R.drawable.point_bg));
            v.setEnabled(false);
            LayoutParams params = new LayoutParams(20, 20);
            params.rightMargin = 35;
            v.setLayoutParams(params);
            points.addView(v);
            points.setVisibility(View.VISIBLE);
            if (i == R.drawable.guide05) {
                Button btn = (Button) view.findViewById(R.id.sys_guide_btn);
                btn.setVisibility(View.GONE);
             /*   btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferencesUtils.putBoolean(getApplicationContext(), AppConstant.KEY.IS_FIRST_GUIDE, false);
                        openActivity();
                    }
                });*/
                iv.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						  PreferencesUtils.putBoolean(getApplicationContext(), AppConstant.KEY.IS_FIRST_GUIDE, false);
	                        openActivity();
						return false;
					}
				});
            }
            views.add(view);
        }
    }

    private void openActivity() {
        if (!flagSet)
            startActivity(new Intent(GuideActivity.this, WelcomeActivity.class));
        finish();
    }

    private int[] getImageResIds() {

        return new int[]{R.drawable.guide01, R.drawable.guide02, R.drawable.guide03, R.drawable.guide04, R.drawable.guide05};
    }

    private void initView() {
        points = (LinearLayout) findViewById(R.id.pc_points);
        slidePager = (ViewPager) findViewById(R.id.ii_viewpager);

        prepareData();

        slidePager.setAdapter(new MyAd());
        slidePager.setOnPageChangeListener(new MyPcL());

        currentSelect = 0;
        slidePager.setCurrentItem(currentSelect);
        points.getChildAt(currentSelect).setEnabled(true);

    }

    private class MyPcL implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            points.getChildAt(currentSelect).setEnabled(false);
            points.getChildAt(arg0).setEnabled(true);
            currentSelect = arg0;
        }
    }

    private List<View> views = new ArrayList<View>();

    private class MyAd extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View imgV = views.get(position);
            container.addView(imgV);
            return imgV;
        }
    }
}
