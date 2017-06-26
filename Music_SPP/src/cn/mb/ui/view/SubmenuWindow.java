package cn.mb.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import cn.mb.hk.music.R;



public class SubmenuWindow extends PopupWindow implements OnClickListener{

	private View layout;
	private Context context;
	private int width=500;
	public SubmenuWindow(Activity context,int width) {
		super(context);
		this.context = context;
		this.width = width;
		initView(context);
		
	}

	private void initView(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.submenu_dialog, null);
//		layout.findViewById(R.id.sys_photo_share_qq).setOnClickListener(this);
		
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(width);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.subAnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		layout.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = layout.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
//		case R.id.sys_photo_share_qq:
//			showShare(true, QQ.NAME);
//			break;
//		case R.id.sys_photo_share_qzone:
//			showShare(true, QZone.NAME);
//			break;
//		case R.id.sys_photo_share_wx:
//			showShare(true, Wechat.NAME);
//			break;
//		case R.id.sys_photo_share_wxpy:
////			showShare(true, WechatMoments.NAME);
//			break;
//		case R.id.sys_photo_share_wxfav:
////			showShare(true, WechatFavorite.NAME);
//			break;
//		case R.id.sys_photo_share_weibo:
//			showShare(true, SinaWeibo.NAME);
//			break;
		case R.id.sys_photo_share_cancle:
			dismiss();
			break;
		default:
			break;
		}
	}

}
