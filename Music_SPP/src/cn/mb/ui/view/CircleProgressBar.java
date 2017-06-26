package cn.mb.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 环形的ProgressBar
 *
 */
public class CircleProgressBar extends View {

	private int progress = 0;
	private int max = 360;

	// 环的路径宽度
	private int pathWidth = PxdpUtils.dip2px(getContext(), 10);;
	// 默认圆的半径
	private int radius = PxdpUtils.dip2px(getContext(), 120);
	/** The width. */
	private int width;
	/** The height. */
	private int height;
	
	// 梯度渐变的填充颜色
	private int[] arcColors = new int[] {0xFF599cd1,0xFF7d70b8,0xFFc8417b,0xFFe35a61,0xFFe98d42,0xFFdabf4a,0xFFdabf4a
			,0xFF4defc8,0xFF47c8db,0xFF599cd1};
	/**
	 * 
	 */
	private int[] shadowsColors = new int[] { 0xFF111111, 0x00AAAAAA,
			0x00AAAAAA };
	
	// 轨迹绘制点
	private Paint pathPaint = null;
	// 绘制填充
	private Paint fillArcPaint = null;
	private RectF oval;
	// 灰色轨迹
	private int pathColor = Color.WHITE;//0xFFF0EEDF
	private int pathBorderColor = 0xFFD2D1C4;
	
	// 指定了光源的方向和环境光强度来添加浮雕效果
	private EmbossMaskFilter emboss = null;
	// 设置光源的方向
	float[] direction = new float[] { 1, 1, 1 };
	// 设置环境光亮度
	float light = 0.4f;
	// 选择要应用的反射等级
	float specular = 6;
	// 向 mask应用一定级别的模糊
	float blur = 3.5f;
	// 指定了一个模糊的样式和半径来处理 Paint 的边缘
	private BlurMaskFilter mBlur = null;
	// view重绘的标记
	private boolean reset = false;

	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		pathPaint = new Paint();
		// 设置是否抗锯齿
		pathPaint.setAntiAlias(true);
		// 帮助消除锯齿
		pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// 设置中空的样式
		pathPaint.setStyle(Paint.Style.STROKE);
		pathPaint.setDither(true);
		pathPaint.setStrokeJoin(Paint.Join.ROUND);

		fillArcPaint = new Paint();
		// 设置是否抗锯齿
		fillArcPaint.setAntiAlias(true);
		// 帮助消除锯齿
		fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// 设置中空的样式
		fillArcPaint.setStyle(Paint.Style.STROKE);
		fillArcPaint.setDither(true);
		fillArcPaint.setStrokeJoin(Paint.Join.ROUND);

		oval = new RectF();
		emboss = new EmbossMaskFilter(direction, light, specular, blur);
		mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (reset) {
			canvas.drawColor(Color.TRANSPARENT);
			reset = false;
		}
		this.width = getMeasuredWidth();
		this.height = getMeasuredHeight();
		this.radius = getMeasuredWidth() / 2 - pathWidth;
		//[start]------渐变圆弧白色底环------------------------
		// 设置画笔颜色
		pathPaint.setColor(pathColor);
		// 设置画笔宽度
		pathPaint.setStrokeWidth(pathWidth);
		// 添加浮雕效果
		pathPaint.setMaskFilter(emboss);


		//[绘制]-渐变圆环底色圆	 在中心的地方画个半径为r的白色圆  半径与渐变圆环相同
		canvas.drawCircle(this.width / 2, this.height / 2, radius, pathPaint);
		// 边线
		pathPaint.setStrokeWidth(0.5f);
		pathPaint.setColor(pathBorderColor);
		//[绘制]-渐变圆环底色圆-玻璃效果边 外
		canvas.drawCircle(this.width / 2, this.height / 2, radius + pathWidth
				/ 2 + 0.5f, pathPaint);
		//[绘制]-渐变圆环底色圆-玻璃效果边 内
		canvas.drawCircle(this.width / 2, this.height / 2, radius - pathWidth
				/ 2 - 0.5f, pathPaint);
		//[end]------渐变圆弧底环------------------------
		
		//[start]------[渐变圆弧] 定义环形颜色  填充------------------------
		SweepGradient sweepGradient = new SweepGradient(this.width / 2,
				this.height / 2, arcColors, null);
		/**
		 * this.width / 2,
				this.height / 2
		 */
		fillArcPaint.setShader(sweepGradient);
		//fillArcPaint.setColor(Color.BLUE);
		// 模糊效果
		fillArcPaint.setMaskFilter(mBlur);
		// 设置线的类型,边是圆的
		fillArcPaint.setStrokeCap(Paint.Cap.ROUND);
		fillArcPaint.setStrokeWidth(pathWidth);
		//fillArcPaint.setStrokeWidth(30);
		// 设置类似于左上角坐标，右下角坐标			RectF(35.0, 35.0, 595.0, 595.0)
		oval.set(this.width / 2 - radius, this.height / 2 - radius, this.width
				/ 2 + radius, this.height / 2 + radius);
		//[绘制]-渐变圆弧  第一个参数为:定义形状和大小，第二个参数为：圆环起始角度/开始的地方，第三个为跨的角度/扫描角度/圆环显示的百分比，第四个为true的时候是实心，false的时候为空心
		canvas.drawArc(oval, -90, progress, false,
				fillArcPaint);
		//[end]------[渐变圆弧] 定义环形颜色  填充--------------------------
	/*	
		//[start]------中心白色圆------------------------
		Paint p = new Paint();  
		p.setStyle(Paint.Style.STROKE);//设置填满  
		p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了  
		RectF oval = new RectF();//定义形状和大小
        BlurMaskFilter mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
        p.setMaskFilter(mBlur);
        // 添加浮雕效果
        EmbossMaskFilter embossemboss = new EmbossMaskFilter(direction, light, specular, blur);
        p.setMaskFilter(embossemboss);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        //绘制-实心圆
        canvas.drawCircle(this.getWidth()/2,  this.getHeight()/2, 210, p);// 
        
        canvas.drawCircle(this.getWidth()/2,this.getHeight()/2, 210+0.5f, pathPaint);
      //[end]----------------------------------------
        */
	}

	/**
	 * 
	 * 描述：获取圆的半径
	 * 
	 * @return
	 * @throws
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * 
	 * 描述：设置圆的半径
	 * 
	 * @param radius
	 * @throws
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	private float currentX,currentY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		boolean up = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			moved(x, y, up);
			break;
		case MotionEvent.ACTION_MOVE:
			moved(x, y, up);
			currentX = event.getX();
			currentY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			up = true;
			moved(x, y, up);
			break;
		}
		return true;
	}
	private boolean IS_PRESSED = false;
	/**
	 * Moved.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param up
	 *            the up
	 */
	float markPointX,markPointY,cx,cy;
	private void moved(float x, float y, boolean up) {
		cx = this.width / 2;
		cy= this.height / 2;
		float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
		System.out.println("=======distance:"+distance+" radius:"+radius+" pathWidth:"+pathWidth+" this.width / 2:"+this.width / 2+" this.height / 2:"+this.height / 2);	
		if (distance < (this.radius+pathWidth) && distance >= (this.radius) && !up) {
			IS_PRESSED = true;
	System.out.println("=======1:"+distance);		
			
//
			markPointX = (float) (cx + radius * Math.cos(Math.atan2(x - cx, cy - y) - (Math.PI /2)));
			markPointY = (float) (cy + radius * Math.sin(Math.atan2(x - cx, cy - y) - (Math.PI /2)));
//
			float degrees = (float) ((float) ((Math.toDegrees(Math.atan2(x - cx, cy - y)) + 360.0)) % 360.0);
//			// and to make it count 0-360
			if (degrees < 0) {
				degrees += 2 * Math.PI;
			}
			if(this.circleProgressCallBack!=null)circleProgressCallBack.callbackDegress((int)degrees);
			setProgress((int)degrees);
			
//
//			setAngle(Math.round(degrees));
//			invalidate();
//
		} else {
			IS_PRESSED = false;
//			invalidate();
		}

	}
	public interface CircleProgressCallBack{
		public void callbackDegress(int data);
	}
	
	private CircleProgressCallBack circleProgressCallBack;

	public void setCircleProgressCallBack(CircleProgressCallBack circleProgressCallBack) {
		this.circleProgressCallBack = circleProgressCallBack;
	}

	
}
