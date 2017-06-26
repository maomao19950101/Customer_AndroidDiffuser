package cn.mb.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;



public class BasePopupWindow extends PopupWindow {
	protected Button winCancleBtn;// 
	protected Button winSubmitBtn;
	
	protected View view;
	protected onSubmitListener onSubmit;

	public BasePopupWindow(Context context){
		super(context);
		
	}
	
	public interface onSubmitListener{
		void onSubmit(String text);
	}
}
