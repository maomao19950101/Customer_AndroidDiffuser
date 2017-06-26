package cn.mb.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.mb.app.AppConstant;
import cn.mb.hk.music.R;
import cn.mb.model.ShareInfo;
import cn.mb.ui.view.SharePopupWindow;

/**
 * JET 公共方法
 *
 * @author Administrator
 */
public class CommUtils {

    // 验证邮箱
    public static boolean isEamilNo(String str) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    //验证手机号
    public static boolean isCellPhoneNum(String str) {
        Pattern pattern = Pattern
                .compile("^1[345789]\\d{9}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isInt(String str) {
        Pattern pattern = Pattern
                .compile("^[1-9]\\d*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isNum(String str) {
        Pattern pattern = Pattern
                .compile("^\\d+(\\.\\d+)?$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 返回 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getDate10(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }

    /**
     * 返回 yyyy-MM-dd 12:00:00
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return df.format(date);
    }

    /**
     * 12:25
     *
     * @param longStrTime
     * @return
     */
    public static String getDate5(String longStrTime) {
        long time = Long.parseLong(longStrTime);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(c.getTime());
    }

    /**
     * 格式化字符串 0.00
     *
     * @return
     */
    public static String formatFloat(double f) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(f);
    }

    /**
     * 格式化字符串 0.00
     *
     * @return
     */
    public static String formatFloat(Float f) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(f);
    }

    /**
     * 格式化字符串 00.0
     *
     * @return
     */
    public static String formatFloat1(String str) {
        if (str == null || str.equals("")) str = "0.00";
        DecimalFormat df = new DecimalFormat("#0.0");

        return df.format(Double.parseDouble(str));
    }


    /**
     * 格式化字符串 0.0
     *
     * @return
     */
    public static String formatFloat1(double f) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(f);
    }

    /**
     * 格式化字符串 0.0
     *
     * @return
     */
    public static String formatFloat1(Float f) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(f);
    }

    /**
     * 格式化字符串 00.00
     *
     * @return
     */
    public static String formatFloat(String str) {
        if (str == null || str.equals("")) str = "0.00";
        DecimalFormat df = new DecimalFormat("#0.00");

        return df.format(Double.parseDouble(str));
    }

    /**
     * 格式化字符串 00.00
     *
     * @return
     */
    public static String formatFloat3(String str) {
        if (str == null || str.equals("")) str = "0.00";
        DecimalFormat df = new DecimalFormat("#0.00");

        return df.format(str);
    }

    public static double second2Hour(int second) {

        return second / 3600.0;
    }

    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    // 获取睡觉时间
    // //////

    // 整数到字节数组的转换
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    // 字节数组到整数的转换
    public static int byteToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[i] >= 0)
                s = s + b[i];
            else

                s = s + 256 + b[i];
            s = s * 256;
        }
        if (b[3] >= 0) // 最后一个之所以不乘，是因为可能会溢出
            s = s + b[3];
        else
            s = s + 256 + b[3];
        return s;
    }

    // 字符到字节转换
    public static byte[] charToByte(char ch) {
        int temp = (int) ch;
        byte[] b = new byte[2];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    // 字节到字符转换

    public static char byteToChar(byte[] b) {
        int s = 0;
        if (b[0] > 0)
            s += b[0];
        else
            s += 256 + b[0];
        s *= 256;
        if (b[1] > 0)
            s += b[1];
        else
            s += 256 + b[1];
        char ch = (char) s;
        return ch;
    }

    // 浮点到字节转换
    public static byte[] doubleToByte(double d) {
        byte[] b = new byte[8];
        long l = Double.doubleToLongBits(d);
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    // 字节到浮点转换
    public static double byteToDouble(byte[] b) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;

        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);

        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    // 十六进制转二进制
    public static String HToB(String a) {
        String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
        return b;
    }

    // 二进制转十六进制
    public String BToH(String a) {
        // 将二进制转为十进制再从十进制转为十六进制
        String b = Integer.toHexString(Integer.valueOf(toD(a, 2)));
        return b;
    }

    // 任意进制数转为十进制数
    public static String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1)) * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    public static String int2HexString(int rgb) {
        String s = Integer.toHexString(rgb);
        if (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }


    // 将十六进制中的字母转为对应的数字
    public static int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
    }


    // 将十进制中的数字转为十六进制对应的字母
    public String formattingH(int a) {
        String i = String.valueOf(a);
        switch (a) {
            case 10:
                i = "a";
                break;
            case 11:
                i = "b";
                break;
            case 12:
                i = "c";
                break;
            case 13:
                i = "d";
                break;
            case 14:
                i = "e";
                break;
            case 15:
                i = "f";
                break;
        }
        return i;
    }


    public static int[] hourMin2Int(String hourMin) {
        int[] times = new int[2];
        if (hourMin.indexOf(":") != -1) {
            String s[] = hourMin.split(":");
            times[0] = Integer.valueOf(s[0]);
            times[1] = Integer.valueOf(s[1]);
        }
        return times;
    }


    public static String addDay(String s, int n) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);

            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }

    }

    public static String getPreDay(String currentDate) {
        return CommUtils.addDay(currentDate, -1);
    }

    public static String getNextDay(String currentDate) {
        return CommUtils.addDay(currentDate, 1);
    }

    public static String getString(Context context, int resId) {
        String s = context.getResources().getString(resId);
        return s;
    }

    /**
     * @param context
     * @param resId      包含变量的字符串
     * @param replaceStr 需要替换的值
     * @return
     */
    public static String getStringFormat(Context context, int resId, String replaceStr) {
        String s = getString(context, resId);

        return String.format(s, replaceStr);
    }

    /**
     * 返回xxhxxm
     */
    public static String getTimeFormat(int min) {
        int h = min / 60;
        int m = min % 60;
        return h + "h" + m + "m";
    }


    public static double min2Hour(int min) {

        return min / 60.0;
    }

    public static String min2HourStr(int min) {
        return formatFloat1(min2Hour(min));
    }


    public static List<String> getHour() {
        List<String> hour = new ArrayList<String>();
		for (int j = 0; j <= 23; j++) {
         //   if (j % 3 == 0) {
                hour.add(j < 10 ? "0" + j : "" + j);
          //  }
        }
        return hour;
    }
    
    public static List<String> getHour5() {
        List<String> hour = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
         //   if (j % 3 == 0) {
                hour.add(j < 10 ? "0" + j : "" + j);
          //  }
        }
        return hour;
    }

    public static List<String> getMinute2() {
        List<String> m = new ArrayList<String>();
        for (int j = 0; j <= 59; j++) {
          //  if (j%5==0) {
                m.add(j < 10 ? "0" + j : "" + j);
          //  }
        }
        return m;
    }
    public static List<String> getMinute30() {
        List<String> m = new ArrayList<String>();
        for (int j = 0; j <= 30; j++) {
          //  if (j%5==0) {
                m.add(j < 10 ? "0" + j : "" + j);
          //  }
        }
        return m;
    }
    /*public static boolean checkPhoneOrEmail(int type, String userName) {
		boolean flag = true;
		if (type == QzConstant.LoginType.LOGIN_BY_PHONE) {
			flag = isCellPhoneNum(userName);
		} else if (type == QzConstant.LoginType.LOGIN_BY_EMAIL) {
			flag = isEamilNo(userName);
		}
		return flag;
	}*/

    public static void startActivity(Context context, Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        startActivity(context, intent);
    }

    public static void startActivity(Context context, Intent intent) {
        Activity a = (Activity) context;
        a.startActivity(intent);
        a.overridePendingTransition(R.anim.slide_in_left, R.anim.alpa_null);
    }


    public static void startActivity(Context context, Class<?> c, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        startActivity(context, intent, requestCode);
    }

    public static void startActivity(Context context, Intent intent, int requestCode) {
        Activity a = (Activity) context;
        a.startActivityForResult(intent, requestCode);
        a.overridePendingTransition(R.anim.slide_in_left, R.anim.alpa_null);
    }

    public static void startActivity(Context context, Intent intent, int requestCode, Fragment fragment) {
        Activity a = (Activity) context;
        if (fragment != null) fragment.startActivityForResult(intent, requestCode);
        else a.startActivityForResult(intent, requestCode);
        a.overridePendingTransition(R.anim.slide_in_left, R.anim.alpa_null);
    }

    public static void startActivity(Context context, Class<?> c, int requestCode, Fragment fragment) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        startActivity(context, intent, requestCode, fragment);
    }

    ////////////////////////

    public static void startActivityDown2Top(Context context, Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        startActivityDown2Top(context, intent);
    }

    public static void startActivityDown2Top(Context context, Intent intent) {
        Activity a = (Activity) context;
        a.overridePendingTransition(R.anim.in_from_down, 0);//R.anim.alpa_null
        a.startActivity(intent);
    }

    //////////////////////////////
    public static void startActivityTop2Down(Context context, Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(context, c);
        startActivityTop2Down(context, intent);
    }

    public static void startActivityTop2Down(Context context, Intent intent) {
        Activity a = (Activity) context;
        a.overridePendingTransition(R.anim.out_to_up, 0);//R.anim.alpa_null
        a.startActivity(intent);
    }

    public static <T> List<T> listKeyMaps(String result, String key, Class<T> c) {
        List<T> list = null;
        try {
            list = new ArrayList<T>();
            Gson gson = new Gson();
            String listStr = (new JSONObject(result)).get(key).toString();
            JsonArray array = new JsonParser().parse(listStr).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, c));
            }
//			list = gson.fromJson(,new  TypeToken<List<T>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static <T> List<T> listKeyMaps2(String result, String key,String subKey, Class<T> c) {
        List<T> list = null;
        try {
            list = new ArrayList<T>();
            Gson gson = new Gson();
            String listStr = (new JSONObject(result)).get(key).toString();
            JsonArray array = new JsonParser().parse(listStr).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, c));
            }
//			list = gson.fromJson(,new  TypeToken<List<T>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String toTime(long time) {
        time /= 1000;
        long minute = time / 60;
        long second = time % 60;
//		minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
    public static boolean isOnWifi(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isOnMobile(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0);
        return networkInfo != null && networkInfo.isConnected();
    }
   private static SharePopupWindow sharePopup;
	public static void showShareWindow2(Context context, ShareInfo shareInfo) {
		if (sharePopup != null && sharePopup.isShowing()) {
			sharePopup.dismiss();
			sharePopup = null;
		}
		sharePopup = new SharePopupWindow((Activity) context, shareInfo);

		sharePopup.showAtLocation(((Activity) context).findViewById(R.id.drawer_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	public static void saveColor(Context context,int color) {
        PreferencesUtils.putInt(context,AppConstant.KEY.LAST_COLOR_RGB, color);
    }
	
    public static ImageListener  imageListener(ImageView iv){
    	return ImageLoader.getImageListener(iv, R.drawable.ic_error, R.drawable.ic_error);
    }
    
	public static void setListViewHeghtBasedOnchildren(ListView photolist2) {
		if (photolist2 == null)
			return;
		ListAdapter adapter = photolist2.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, photolist2);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = photolist2.getLayoutParams();
		params.height = totalHeight + (photolist2.getDividerHeight() * (adapter.getCount() - 1));
		photolist2.setLayoutParams(params);
	}

}
