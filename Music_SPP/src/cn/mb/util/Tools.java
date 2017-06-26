package cn.mb.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import cn.mb.http.VDLog;

public class Tools {
	private final String TAG;

	public Tools() {
		this.TAG = getClass().getSimpleName();
	}

	/**
	 * @param context
	 *            ����������
	 * @return ����int[��,��],����ϵͳ״̬��
	 */
	public int[] getDisplayByPx(Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) (context)).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int[] result = new int[2];
		result[0] = displayMetrics.widthPixels;
		result[1] = displayMetrics.heightPixels;
		VDLog.v(TAG, "getDisplayByPxWithoutSystem px:" + result[0] + " x " + result[1]);
		return result;
	}
}
