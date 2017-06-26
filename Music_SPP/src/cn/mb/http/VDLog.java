package cn.mb.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class VDLog {
	private static final boolean DEBUG=true;
	
	public static void d(String tag, String msg) {
		if (DEBUG) {//3
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {//4
			if(msg==null)msg="";
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG)  {//6
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, Exception e) {
		if (DEBUG)  {//6
			e(tag, e.getMessage(),e);
		}
	}
	public static void e(String tag, String msg,Exception e) {
		if (DEBUG)  {//6
			Log.e(tag, msg,e);
		}
	}
	public static void v(String tag, String msg) {
		if (DEBUG)  {//2
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG)  {//5
			Log.w(tag, msg);
		}
	}
	
	public static void msg(Context context,CharSequence text,int duration){
		Toast.makeText(context, text, duration).show();
	}
	public static void msg(Context context,CharSequence text){
		msg(context,text,Toast.LENGTH_SHORT);
	}
	/*public static void alert(Context context,CharSequence text,int duration){
		Toast.makeText(context, text, duration).show();
	}*/
}
