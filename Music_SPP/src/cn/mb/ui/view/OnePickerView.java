package cn.mb.ui.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import cn.mb.hk.music.R;
import cn.mb.ui.view.PickerView.onSelectListener;

/**
 * 
 * @author JET
 * 
 */
public class OnePickerView extends BasePopupWindow implements OnClickListener {

	private String valueSelected1;//

	private PickerView pvView1;

	protected Button cancleBtn;//
	protected Button submitBtn;//

	protected View view;
	protected onSubmitListener onSubmit;
	private Context context;

	private List<String> datas1;

	private OnePickerView(Context context) {
		super(context);
	}

	public OnePickerView(Context context, List<String> datas1, String defaultValue1,
			onSubmitListener onSubmit) {
		super(context);
		this.context = context;
		this.datas1 = datas1;
		this.valueSelected1 = defaultValue1;
		this.onSubmit = onSubmit;
		initViewData();
	}

	@SuppressLint("InflateParams")
	private void initViewData() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.pv_popup_one, null);

		pvView1 = (PickerView) view.findViewById(R.id.pv_View_normal1);
		if (datas1 == null)
			datas1 = getDefaultData();
		pvView1.setData(datas1);

		if (valueSelected1 != null && !"".equals(valueSelected1))
			pvView1.setDefault(valueSelected1);
		else
			valueSelected1 = pvView1.getDefaultText();

		pvView1.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				valueSelected1 = text;
			}
		});


		cancleBtn = (Button) view.findViewById(R.id.pickerViewCancel);
		cancleBtn.setOnClickListener(this);

		submitBtn = (Button) view.findViewById(R.id.pickerViewSubmit);
		submitBtn.setOnClickListener(this);

		this.setContentView(view);// ����SelectPicPopupWindow��View

		this.setWidth(LayoutParams.MATCH_PARENT);// ����SelectPicPopupWindow��������Ŀ�

		this.setHeight(LayoutParams.WRAP_CONTENT);// ����SelectPicPopupWindow��������ĸ�

		this.setFocusable(true);// ����SelectPicPopupWindow��������ɵ��

		this.setAnimationStyle(R.style.AnimBottom);// ����SelectPicPopupWindow�������嶯��Ч��

		ColorDrawable dw = new ColorDrawable(0xb0000000);// ʵ����һ��ColorDrawable��ɫΪ��͸��

		this.setBackgroundDrawable(dw);// ����SelectPicPopupWindow��������ı���

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.pickerViewCancel) {
			dismiss();
		} else if (id == R.id.pickerViewSubmit) {
			onSubmit.onSubmit(valueSelected1);
			dismiss();
		} else {
		}
	}

	private List<String> getDefaultData() {
		List<String> height = new ArrayList<String>();

		for (int i = 100; i < 120; i++) {
			height.add(i + "");
		}
		return height;
	}
}
