package cn.mb.http;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class VDNotic {
	
	private static Toast toast;
	public static void alert(Context context,CharSequence text,int duration){
		if(toast!=null)toast.cancel();
			toast=	Toast.makeText(context,text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
//		Toast.makeText(context, text, duration).show();
	}
	public static void alert(Context context,CharSequence text){
		alert(context, text, Toast.LENGTH_SHORT);
	}
	
	public static void alert(Context context,int ResId,int duration){
		Toast.makeText(context, ResId, duration).show();
	}
	
	public static void alert(Context context,int ResId){
		alert(context, ResId, Toast.LENGTH_SHORT);
	}
}
