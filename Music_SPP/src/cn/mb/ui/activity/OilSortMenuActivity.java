package cn.mb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import cn.mb.hk.music.R;
import cn.mb.ui.base.InBaseActivity;

public class OilSortMenuActivity extends InBaseActivity implements View.OnClickListener {

	private NumberPicker essential_number;
	private EditText essential_input;

	private ImageView essential_search;

	private int type = 1;

	protected void setAfter(Bundle savedInstanceState) {
		super.setAfter(savedInstanceState);
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.essential_search:
			toOilRepeListActivity();
			break;
		default:
			break;
		}
	}

	private void initView() {
		essential_input = (EditText) this.findViewById(R.id.essential_input);
		essential_search = (ImageView) this.findViewById(R.id.essential_search);
		essential_search.setOnClickListener(this);
		essential_number = (NumberPicker) this.findViewById(R.id.essential_number);

		essential_number.setMinValue(0);
		essential_number.setMaxValue(2);
		essential_number.setDisplayedValues(new String[] { "Name", "Oil", "Category" });

		essential_number.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				type = 1 + newVal;
			}
		});
	}

	private void toOilRepeListActivity() {
		String text = essential_input.getText().toString();
		if (TextUtils.isEmpty(text)) {
			return;
		}
		Intent intent = new Intent(this, OilRecipeListActivity.class);
		intent.putExtra(OilRecipeListActivity.PARAM_TYPE, type + "");
		intent.putExtra(OilRecipeListActivity.PRRAM_VALUE, text);
		startActivity(intent);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_essential_oil_sort_menu;
	}
}
