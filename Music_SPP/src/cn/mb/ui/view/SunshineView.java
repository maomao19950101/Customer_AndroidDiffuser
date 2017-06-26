package cn.mb.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import cn.mb.http.VDLog;

public class SunshineView {
	private final String TAG;
	private final Context context;
	// ����
	private DrawShine drawShine;
	private final int maxTimer;
	private int timer;
	private int[] size;
	private int color;
	// �ӿ�
	private PaintState paintState;

	public SunshineView(Context context) {
		this.TAG = getClass().getSimpleName();
		this.context = context;
		this.maxTimer = 3;
	}

	// ��ʼ������
	public void init(int[] size, int color, PaintState paintState) {
		VDLog.v(TAG, "init drawing isn't nice?");
		this.size = size;
		this.color = color;
		this.paintState = paintState;
		handler.post(runnable);
	}

	// ֹͣ����
	public void stopDraw() {
		handler.removeCallbacks(runnable);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (paintState != null) {
				drawShine = new DrawShine(context);
				timer = timer > maxTimer ? 1 : ++timer;
				paintState.haveChanged(drawShine);
			}
			super.handleMessage(msg);
		}
	};
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			handler.postDelayed(this, 500);
			handler.sendEmptyMessage(0);
		}
	};

	// ���ƹ���
	private class DrawShine extends View {
		public DrawShine(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// ���ƹ���
			drawSunshine(canvas);
		}

		// ���ƹ���
		private void drawSunshine(Canvas canvas) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setColor(color);
			int length = (size[0] / 2) / 11;
			for (int i = 0; i < 18; i++) {
				float[] lastPostion = null;
				float radius = 185F + i * 10F;
				for (int j = 0; j < 6; j++) {
					if (lastPostion == null) {
						float addX = (float) (Math.cos(Math.toRadians(radius)) * length / maxTimer * timer);
						float addY = (float) (Math.sin(Math.toRadians(radius)) * length / maxTimer * timer);
						lastPostion = new float[] { size[0] / 2 + addX, size[0] / 2 + addY };
					}
					float stopX = lastPostion[0] + (float) (Math.cos(Math.toRadians(radius)) * length);
					float stopY = lastPostion[1] + (float) (Math.sin(Math.toRadians(radius)) * length);
					canvas.drawLine(lastPostion[0], lastPostion[1], stopX, stopY, paint);
					lastPostion[0] = stopX + (float) (Math.cos(Math.toRadians(radius)) * length / 2);
					lastPostion[1] = stopY + (float) (Math.sin(Math.toRadians(radius)) * length / 2);
					if (i % 2 == 1 && j == 4) {
						break;
					}
					if (i > 11 && i < 15 && j == 3) {
						break;
					}
				}
			}
		}
	}
}