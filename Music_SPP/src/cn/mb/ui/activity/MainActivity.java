package cn.mb.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import cn.mb.app.AppAplication;
import cn.mb.app.ProductType;
import cn.mb.hk.music.R;
import cn.mb.http.VDNotic;
import cn.mb.model.ShareInfo;
import cn.mb.ui.base.InBaseActivity;
import cn.mb.ui.view.SubmenuWindow;
import cn.mb.util.CommUtils;

/**
 * Created by lihongshi on 16/1/26.
 * 
 * modfiy 
 */
public class MainActivity extends InBaseActivity implements View.OnClickListener {

    // 抽屉菜单对象
    public DrawerLayout mDrawerLayout;
    private LinearLayout left_menu_layout;

    
    @Override
    protected int getLayoutResId() {
    	return R.layout.main_frame_activity;
    }

@Override
protected void setAfter(Bundle savedInstanceState) {
	super.setAfter(savedInstanceState);
	initView();
    initLeftLayout();
    left_menu_layout.setBackgroundResource(AppAplication.bgResId);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_left_menu:
                openLeftLayout();
                break;
            case R.id.left_menu_item_01:
            	CommUtils.startActivity(this, DiffUserActivity.class);
                break;
            case R.id.left_menu_item_02:
            	if(AppAplication.productType.getName().equals(ProductType.PZ02.getName())){
    				CommUtils.startActivity(this, Pz02VideoListActivity.class);//Pz02VideoListActivity FiveSensesActivity
    			}else{
    				CommUtils.startActivity(this, FiveSensesActivity.class);
    			}
                break;
            case R.id.left_menu_item_03:
            	CommUtils.startActivity(this,ProfileActivity.class);
                break;
            case R.id.left_menu_item_04:
            	CommUtils.startActivity(this,SettingActivity.class);
                break;
            case R.id.left_menu_item_05:
            	CommUtils.startActivity(this,InfoActivity.class);
                break;
            case R.id.pv_main_submenu:
            	showSubMenuWindow(this);
                break;
            case R.id.pv_main_fav:
            	CommUtils.startActivity(this, FavoriteListActivity.class);
                break;
            case R.id.pv_main_share:
            	ShareInfo shareInfo =new ShareInfo("ShareText","https://www.baidu.com/img/baidu_jgylogo3.gif","https://www.baidu.com/");
        		CommUtils.showShareWindow2(this, shareInfo);
            	break;
            default:
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (firstTime == 0) {
                firstTime = System.currentTimeMillis();
                VDNotic.alert(this, R.string.src_exit_tip);
            } else {
                if (System.currentTimeMillis() - firstTime < 2000) {
//                    System.exit(0);
                	appContext.onTerminate();
                    return super.onKeyDown(keyCode, event);
                } else
                    firstTime = 0;
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private View topView;
    private void initView() {
        this.findViewById(R.id.actionbar_left_menu).setOnClickListener(this);
        this.findViewById(R.id.left_menu_item_01).setOnClickListener(this);
        this.findViewById(R.id.left_menu_item_02).setOnClickListener(this);
        this.findViewById(R.id.left_menu_item_03).setOnClickListener(this);
        this.findViewById(R.id.left_menu_item_04).setOnClickListener(this);
        this.findViewById(R.id.left_menu_item_05).setOnClickListener(this);
        this.findViewById(R.id.pv_main_share).setOnClickListener(this);
        this.findViewById(R.id.pv_main_fav).setOnClickListener(this);
        this.findViewById(R.id.pv_main_submenu).setOnClickListener(this);
        topView=   findViewById(R.id.main_action_op);
    }


    public void initLeftLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置透明
        mDrawerLayout.setScrimColor(0x00000000);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // set a custom shadow that overlays the main content when the drawer opens
        //   mDrawerLayout.setDrawerShadow(R.drawable.icon, GravityCompat.START);
        //   mDrawerLayout.setDrawerShadow(R.drawable.icon, GravityCompat.START);
        //左边菜单
        left_menu_layout = (LinearLayout) findViewById(R.id.main_left_drawer_layout);
    }


    //左边菜单开关事件
    public void openLeftLayout() {
    	CommUtils.startActivity(this, DiffUserActivity.class);
      /*  if (mDrawerLayout.isDrawerOpen(left_menu_layout)) {
            mDrawerLayout.closeDrawer(left_menu_layout);
        } else {
            mDrawerLayout.openDrawer(left_menu_layout);

        }*/
    }
    private  SubmenuWindow subMenuPopup;
    private  void showSubMenuWindow(Context context) {
		if (subMenuPopup != null && subMenuPopup.isShowing()) {
			subMenuPopup.dismiss();
			subMenuPopup = null;
		}
		 int[] location = new int[2];  
		  topView.getLocationOnScreen(location);  
		  subMenuPopup = new SubmenuWindow((Activity) context,(int)(topView.getWidth()/2.0f));
		  subMenuPopup.showAtLocation(topView, Gravity.TOP|Gravity.RIGHT,0, location[1]+topView.getHeight());
	}

}
