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
import cn.mb.model.ShareInfo;



public class SharePopupWindow extends PopupWindow implements OnClickListener{

	private View layout;
	private Context context;
	private ShareInfo shareInfo;
	public SharePopupWindow(Activity context,ShareInfo shareInfo) {
		super(context);
		this.context = context;
		this.shareInfo = shareInfo;
		
		initView(context);
		
	}

	private void initView(Activity context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.share_dialog, null);

		layout.findViewById(R.id.sys_photo_share_qq).setOnClickListener(this);
		layout.findViewById(R.id.sys_photo_share_qzone).setOnClickListener(this);
		layout.findViewById(R.id.sys_photo_share_wx).setOnClickListener(this);
//		layout.findViewById(R.id.sys_photo_share_wxpy).setOnClickListener(this);
//		layout.findViewById(R.id.sys_photo_share_wxfav).setOnClickListener(this);
		layout.findViewById(R.id.sys_photo_share_weibo).setOnClickListener(this);
		layout.findViewById(R.id.sys_photo_share_cancle).setOnClickListener(this);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
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

	private void showShare(boolean silent, String platform) {
//		ShareSDK.initSDK(context);
//		OnekeyShare oks = new OnekeyShare();
//		// String title = this.getString(R.string.share_title);
//		// String text=this.getString(R.string.share_desc);
//		// String url =this.getString(R.string.share_url);
//		if (platform != null) {
//			oks.setPlatform(platform);
//		}
////		oks.setImagePath(filePathName);
////		oks.setNotification(R.drawable.pv_notic, context.getResources().getString(R.string.app_name));
//		oks.setSiteUrl(shareInfo.getSiteUrl());
//		oks.setText(context.getResources().getString(R.string.app_name));
//	if(SinaWeibo.NAME.equals(platform))	oks.setImageUrl(shareInfo.getImgUrl());
//		oks.setShareContentCustomizeCallback(new SysShareContentCustomizeCallback(shareInfo));// 去自定义不同平台的字段内容
//		oks.show(context);
//	
////		dismiss();
	}
}
