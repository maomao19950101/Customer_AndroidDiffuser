package cn.mb.ui.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import cn.mb.app.AppAplication;
import cn.mb.hk.music.R;



public class OilListPopupWindow extends PopupWindow implements OnItemClickListener {

	private View layout;
	private Context context;
	private ListView  listView;
	private OnSelectItemListener listener;

	private OilListPopupWindow(Context context) {
		super(context);
	}
	public OilListPopupWindow(Context context,OnSelectItemListener listener) {
//		super(context);
//		super(context,R.style.CustomDialogStyle);
		this.context = context;
		this.listener=listener;
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.pv_oil_local_list, null);
		layout.setBackgroundResource(AppAplication.bgResId);
		listView = (ListView) layout.findViewById(R.id.pv_oil_local_list);
		
		listView.setOnItemClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,  R.layout.pv_item,R.id.pv_item_tx,context.getResources().getStringArray(R.array.oils_local_list));
				listView.setAdapter(adapter);
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
	
	

	public interface OnSelectItemListener{
		void oItemSelect(String text);
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
		if(listener!=null){
			listener.oItemSelect(context.getResources().getStringArray(R.array.oils_local_list)[position]);
			dismiss();
		}
	}
}
