package cn.mb.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import cn.mb.http.VDLog;

public class MistAnimView {
	private final String TAG;
	private final Context context;
	// ����
	private DrawShine drawShine;
	private final int maxTimer;
	private int timer;
	private int[] size;
	private int color;
	private float[] stopPosition1;
	private float[] stopPosition2;
	private float[] stopPosition3;
	// �ӿ�
	private PaintState paintState;

	public MistAnimView(Context context) {
		this.TAG = getClass().getSimpleName();
		this.context = context;
		this.maxTimer = 5;
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
			// ����ԲȦ
			drawCircle(canvas);
		}

		// ����ԲȦ
		private void drawCircle(Canvas canvas) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			// ���ư�ɫ����
			int sideLength = (size[0] / 2);
			final float radius = 318F;
			float startX;
			float startY;
			float stopX;
			float stopY;
			float[] last;
			float length1 = sideLength * 0.4F;
			float dash1 = length1 / 10;
			float radiusCircle = sideLength / 15F;
			// -->Line3
			last = null;
			paint.setStrokeWidth(3);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			startX = (sideLength + sideLength / 13)
					+ (float) (Math.cos(Math.toRadians(radius)) * (sideLength / 3 - sideLength / 10))
					+ (float) (Math.cos(Math.toRadians(radius)) * dash1 * 2 * (timer) / maxTimer);
			startY = sideLength + (float) (Math.sin(Math.toRadians(radius)) * (sideLength / 3 - sideLength / 10))
					+ (float) (Math.sin(Math.toRadians(radius)) * dash1 * 2 * (timer) / maxTimer);
			stopX = startX + (float) (Math.cos(Math.toRadians(radius)) * sideLength * 0.7);
			stopY = startY + (float) (Math.sin(Math.toRadians(radius)) * sideLength * 0.7);
			if (stopPosition1 == null) {
				stopPosition1 = new float[] { stopX, stopY };
			}
			for (int i = 0; i < 7; i++) {
				if (last == null) {
					last = new float[] { startX, startY };
				}
				float endX = last[0] + dash1;
				float endY = last[1] - dash1;
				canvas.drawLine(last[0], last[1], endX, endY, paint);
				last[0] = last[0] + dash1 * 1.5F;
				last[1] = last[1] - dash1 * 1.5F;
			}
			// -->Circle3
			int startLength = 20;
			float[] dash = new float[] { startLength, startLength - 10, startLength, startLength - 10 };
			PathEffect effects = new DashPathEffect(dash, timer * 10);
			paint.setPathEffect(effects);
			float centerX = stopPosition1[0] + (float) (Math.sin(Math.toRadians(radius)) * radiusCircle) * 2;
			float centerY = stopPosition1[1] - (float) (Math.cos(Math.toRadians(radius)) * radiusCircle);
			/*paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(3);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(centerX, centerY, radiusCircle, paint);*///ȥ��ԲȦ�İ�ɫ��
			paint.setStrokeWidth(3);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(centerX, centerY, radiusCircle, paint);
			// -->Line2
			last = null;
			paint.setStrokeWidth(3);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			startX = sideLength + (float) (Math.cos(Math.toRadians(radius)) * (sideLength / 3 - sideLength / 13))
					+ (float) (Math.cos(Math.toRadians(radius)) * dash1 * 2 * (timer) / maxTimer);
			startY = sideLength + (float) (Math.sin(Math.toRadians(radius)) * (sideLength / 3 - sideLength / 13))
					+ (float) (Math.sin(Math.toRadians(radius)) * dash1 * 2 * (timer) / maxTimer);
			stopX = startX + (float) (Math.cos(Math.toRadians(radius)) * sideLength * 0.65);
			stopY = startY + (float) (Math.sin(Math.toRadians(radius)) * sideLength * 0.65);
			if (stopPosition2 == null) {
				stopPosition2 = new float[] { stopX, stopY };
			}
			for (int i = 0; i < 5; i++) {
				if (last == null) {
					last = new float[] { startX, startY };
				}
				float endX = last[0] + dash1;
				float endY = last[1] - dash1;
				canvas.drawLine(last[0], last[1], endX, endY, paint);
				last[0] = last[0] + dash1 * 1.5F;
				last[1] = last[1] - dash1 * 1.5F;
			}
			// -->Circle2
			paint.setPathEffect(effects);
			centerX = stopPosition2[0] + (float) (Math.sin(Math.toRadians(radius)) * radiusCircle * 2) * 2;
			centerY = stopPosition2[1] - (float) (Math.cos(Math.toRadians(radius)) * radiusCircle * 2);
		/*	paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(3);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(centerX, centerY, radiusCircle * 2.5F, paint);*///ȥ��ԲȦ�İ�ɫ��
			paint.setStrokeWidth(3);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(centerX, centerY, radiusCircle * 2.5F, paint);
			// -->Line1
			last = null;
			paint.setStrokeWidth(3);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			startX = (sideLength - sideLength / 13) + (float) (Math.cos(Math.toRadians(radius)) * sideLength / 3)
					+ (float) (Math.cos(Math.toRadians(radius)) * dash1 * 2 * (timer) / maxTimer);
			startY = sideLength + (float) (Math.sin(Math.toRadians(radius)) * sideLength / 3)
					+ (float) (Math.sin(Math.toRadians(radius)) * dash1 * 2 * (timer) / maxTimer);
			stopX = startX + (float) (Math.cos(Math.toRadians(radius)) * length1);
			stopY = startY + (float) (Math.sin(Math.toRadians(radius)) * length1);
			if (stopPosition3 == null) {
				stopPosition3 = new float[] { stopX, stopY };
			}
			for (int i = 0; i < 3; i++) {
				if (last == null) {
					last = new float[] { startX, startY };
				}
				float endX = last[0] + dash1;
				float endY = last[1] - dash1;
				canvas.drawLine(last[0], last[1], endX, endY, paint);
				last[0] = last[0] + dash1 * 1.5F;
				last[1] = last[1] - dash1 * 1.5F;
			}
			// -->Circle1
			paint.setPathEffect(effects);
			centerX = stopPosition3[0] + (float) (Math.sin(Math.toRadians(radius)) * radiusCircle) * 2;
			centerY = stopPosition3[1] - (float) (Math.cos(Math.toRadians(radius)) * radiusCircle);
		/*	paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(3);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(centerX, centerY, radiusCircle * 1.2F, paint);*///ȥ��ԲȦ�İ�ɫ��
			paint.setStrokeWidth(3);
			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(centerX, centerY, radiusCircle * 1.2F, paint);
		}
	}
}