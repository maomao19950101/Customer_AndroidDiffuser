package cn.mb.alertview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import cn.mb.hk.music.R;

public class CustomProgressDialog extends ProgressDialog{
	 private String msg; 
	 private TextView msgTV;
	public CustomProgressDialog(Context context) {
		super(context);//super(context,R.style.CustomDialogStyle);
		msg="loading...";
	}
	public CustomProgressDialog(Context context,String msg) {
		super(context);
		this.msg=msg;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.custom_progress);  
		 msgTV  = (TextView) findViewById(R.id.define_progress_msg);  
		 msgTV.setText(msg);  
	}
}
