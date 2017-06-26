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
public class TwoPickerView extends BasePopupWindow implements OnClickListener {

	private String valueSelected1;//
	private String valueSelected2;

	private PickerView pvView1;
	private PickerView pvView2;

	protected Button cancleBtn;//
	protected Button submitBtn;//

	protected View view;
	protected onSubmitListener onSubmit;
	private Context context;

	private List<String> datas1;
	private List<String> datas2;

	private TwoPickerView(Context context) {
		super(context);
	}

	public TwoPickerView(Context context, List<String> datas1, List<String> datas2, String defaultValue1, String defaultValue2,
			onSubmitListener onSubmit) {
		super(context);
		this.context = context;
		this.datas1 = datas1;
		this.datas2 = datas2;
		this.valueSelected1 = defaultValue1;
		this.valueSelected2 = defaultValue2;
		this.onSubmit = onSubmit;
		initViewData();
	}

	@SuppressLint("InflateParams")
	private void initViewData() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.pv_popup_two, null);

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

		pvView2 = (PickerView) view.findViewById(R.id.pv_View_normal2);
		if (datas2 == null)
			datas2 = getDefaultData();
		pvView2.setData(datas2);

		if (valueSelected2 != null && !"".equals(valueSelected2))
			pvView2.setDefault(valueSelected2);
		else
			valueSelected2 = pvView2.getDefaultText();

		pvView2.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				valueSelected2 = text;
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
			onSubmit.onSubmit(valueSelected1 + "." + valueSelected2);
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
