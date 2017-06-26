package cn.mb.http;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import cn.mb.alertview.CustomDialog;
import cn.mb.alertview.CustomProgressDialog;
import cn.mb.hk.music.R;

/**
 * 提示信息的管理
 */
public class PromptManager {
	private static ProgressDialog dialog;

	public static void showProgressDialog(Context context) {
		showProgressDialog(context, "请等候，数据加载中……");
	}

	public static void showProgressDialog(Context context, String message) {
		if (dialog != null && dialog.isShowing())
			return;
		dialog = new CustomProgressDialog(context, message);
		dialog.setCancelable(true);

		dialog.show();
	}

	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * 当判断当前手机没有网络时使用
	 * 
	 * @param context
	 */
	public static void showNoNetWork(final Context context) {

		CustomDialog.Builder ibuilder;
		ibuilder = new CustomDialog.Builder(context);
		ibuilder.setTitle(R.string.prompt);
		ibuilder.setMessage("当前无网络,去设置");
		ibuilder.setPositiveButton("设置", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				if (android.os.Build.VERSION.SDK_INT > 10) {
					// intent = new
					// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
			}
		});
		ibuilder.setNegativeButton(R.string.cancel, null);
		ibuilder.create().show();

	}

	public static Dialog alertDialog(Context context, String msg, android.content.DialogInterface.OnClickListener listener) {
		CustomDialog c;
		CustomDialog.Builder ibuilder;
		ibuilder = new CustomDialog.Builder(context);
		ibuilder.setTitle(R.string.prompt);
		ibuilder.setMessage(msg);
		ibuilder.setPositiveButton(R.string.confirm, listener);
		ibuilder.setNegativeButton(R.string.cancel, null);
		c = ibuilder.create();
		c.show();

		return c;
	}

	public static Dialog alertDialog(Context context, String msg, android.content.DialogInterface.OnClickListener okListener,android.content.DialogInterface.OnClickListener canlistener) {
		CustomDialog c;
		CustomDialog.Builder ibuilder;
		ibuilder = new CustomDialog.Builder(context);
		ibuilder.setTitle(R.string.prompt);
		ibuilder.setMessage(msg);
		ibuilder.setPositiveButton(R.string.confirm, okListener);
		ibuilder.setNegativeButton(R.string.cancel, canlistener);
		c = ibuilder.create();
		c.show();

		return c;
	}
	public static void showErrorDialog(Context context, String msg) {
		CustomDialog c;
		CustomDialog.Builder ibuilder;
		ibuilder = new CustomDialog.Builder(context);
		ibuilder.setTitle(R.string.prompt);
		ibuilder.setMessage(msg);
		ibuilder.setPositiveButton(R.string.confirm, null);

		c = ibuilder.create();
		c.show();
	}

	public static ProgressDialog getProgressDialog(Context context, String title, int resId) {
		String msg = context.getResources().getString(resId);

		return getProgressDialog(context, title, msg);
	}

	public static ProgressDialog getProgressDialog(Context context, String title, String msg) {
		ProgressDialog alertDialog = new CustomProgressDialog(context, msg);
		alertDialog.show();
		alertDialog.setCancelable(true);
		return alertDialog;
	}

	public static void showExitSystem(Context context) {
		CustomDialog.Builder ibuilder;
		ibuilder = new CustomDialog.Builder(context);
		ibuilder.setTitle(R.string.prompt);
		ibuilder.setMessage(R.string.exit_app);
		ibuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				dialog.dismiss();
			}
		});
		ibuilder.setNegativeButton(R.string.cancel, null);
		ibuilder.create().show();
	}

	
}
