package cn.mb.alertview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.mb.hk.music.R;

public class LoadMoreView extends RelativeLayout {

	private View view;
	private TextView txt;
	private ImageView img;
	private LoadMoreListener loadMoreListener;
	private boolean isLoading = false;

	public LoadMoreView(Context context, AttributeSet attrs) {
		super(context, attrs);

		view = LayoutInflater.from(context).inflate(R.layout.load_more, this, true);
		txt = (TextView) view.findViewById(R.id.load_more_txt);
		img = (ImageView) view.findViewById(R.id.load_more_img);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLoading)
					return;

				if (loadMoreListener != null) {
					setLoading();
					loadMoreListener.onLoadMoreData();
				}
			}
		});
	}

	private void setLoading() {
		isLoading = true;
		if (img != null)
			img.setVisibility(View.VISIBLE);
		if (txt != null) {
			txt.setVisibility(View.GONE);
		}
	}

	public void setLoaded() {
		isLoading = false;
		if (img != null)
			img.setVisibility(View.GONE);
		if (txt != null) {
			txt.setText("加载更多");
			txt.setVisibility(View.VISIBLE);
		}
	}

	public void setNoMore() {
		// view.setVisibility(View.GONE);
		isLoading = false;
		if (img != null)
			img.setVisibility(View.GONE);
		if (txt != null) {
			txt.setText("没有更多数据");
			txt.setVisibility(View.VISIBLE);
		}
	}

	public interface LoadMoreListener {
		void onLoadMoreData();
	}

	public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

}
