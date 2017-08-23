package com.znt.vodbox.view;

import com.znt.vodbox.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class SplashView extends View{

	private Paint cPaint;//定义一个画小圆的画笔
	private Paint bPaint;//画背景的画笔
	private int width;
	private int height;
	private int[] colorArray;//小圆颜色的数组
	private static final int MAX_DOT_CONUNT=6;//最大小圆的个数
	private static int BIG_CIRCLE_RADIUS=80;//小圆距离屏幕中心的距离
	private static final int S_CIRCLE_RADIUS=10;//小圆的半径
	private float singleAngle;//每个小圆之间的夹角
	private float rotateAngle=0;//旋转动画转动的角度
	private int currentRadius=BIG_CIRCLE_RADIUS;//当前小圆距离屏幕中心的距离
	public SplashView(Context context) {
		super(context);
		init();
	}
	//初始化一些变量
	private void init() {
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bPaint.setStyle(Paint.Style.STROKE);
		bPaint.setColor(0xffffffff);
		colorArray = getContext().getResources().getIntArray(R.array.circleColor);
		singleAngle = (float) (2*Math.PI/MAX_DOT_CONUNT);
		
	}


	@Override
	protected void onDraw(Canvas canvas) {
			if(design==null){
				design=new RotateAnim();
			}
			design.startAnim(canvas);

	}
	private MatrielDesign design;
	private ValueAnimator mAnimator;
	//提供给外加调用的,闪屏页检查版本更新后可以调用停止第一阶段动画并且开启第二阶段
	public void stopRotate(){
		if(mAnimator != null)
		{
			mAnimator.cancel();
			design=new ScaleAnim();
			invalidate();
		}
	}
	//定义一个抽象的父类,这种设计模式还是非常叼的,公开课中学到的
	private abstract class MatrielDesign{
		public abstract void startAnim(Canvas canvas); 
	}

	//第一阶段的旋转动画,其实就是改变的rotateAngle大小,让drawCircle()的时候可以每次及时计算出每个小圆的坐标
	private class RotateAnim extends MatrielDesign{
		public RotateAnim() {
			mAnimator=ValueAnimator.ofFloat(0,1.0f);
			mAnimator.setRepeatCount(Integer.MAX_VALUE);
			mAnimator.setDuration(1300);
			mAnimator.setInterpolator(new LinearInterpolator());
			mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float fraction=(Float) animation.getAnimatedValue();
					rotateAngle=(float) (Math.PI*fraction);
					invalidate();
				}
			});
			mAnimator.start();
		}
		@Override
		public void startAnim(Canvas canvas) {
			drawBg(canvas);
			drawCircle(canvas);
		}
		
	}
	//第二阶段的动画,其实就是改变的小圆和屏幕中心的距离
	//定义ValueAnimator.ofFloat(1.0f,0.2f,1.2f)让动画有一个回弹效果
	private class ScaleAnim extends MatrielDesign{

		public ScaleAnim() {
			mAnimator=ValueAnimator.ofFloat(1.0f,0.2f,1.2f);
			mAnimator.setDuration(600);
			mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float fraction=(Float) animation.getAnimatedValue();
					currentRadius=(int) (BIG_CIRCLE_RADIUS*fraction);
					invalidate();
				}
			});
			mAnimator.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					design=new CrinkleAnim();
					invalidate();
				}
			});
			mAnimator.start();
		}

		@Override
		public void startAnim(Canvas canvas) {
			drawBg(canvas);
			drawCircle(canvas);
		}
		
	}
	private int crinkleCircle;
	private double diagonal;
	private int strokeWidth;
	//第三阶段水波纹动画,其实就是画背景的时候画一个圆,然后调整画笔的描线宽度就可以了
	//这个思路也可以做一个圆形头像的效果,你觉得呢?
	private class CrinkleAnim extends MatrielDesign{
		public CrinkleAnim(){
			mAnimator=ValueAnimator.ofFloat(0,1.0f);
			mAnimator.setDuration(1000);
			mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float fraction=(Float) animation.getAnimatedValue();
					crinkleCircle = (int) (diagonal*fraction);
					strokeWidth=(int) (diagonal-crinkleCircle);
					bPaint.setStrokeWidth(strokeWidth);
					invalidate();
				}
			});
			mAnimator.start();
		}
		//第三阶段的动画只需要画背景即可
		@Override
		public void startAnim(Canvas canvas) {
			isCrinkle=true;
			drawBg(canvas);
		}
		
	}
	private boolean isCrinkle;
	private void drawBg(Canvas canvas) {
		if(!isCrinkle){
			canvas.drawColor(0xffffffff);
		}else{//第三阶段动画
			canvas.drawCircle(width/2, height/2, crinkleCircle+strokeWidth/2, bPaint);
		}
	}
	//计算每个小圆的坐标,画小圆
	private void drawCircle(Canvas canvas) {
		for(int i=0;i<colorArray.length;i++){
			float angle=singleAngle*i+rotateAngle;
			float cx=(float) (currentRadius*Math.cos(angle))+width/2;
			float cy=(float) (currentRadius*Math.sin(angle))+height/2;
			cPaint.setColor(colorArray[i]);
			canvas.drawCircle(cx, cy, S_CIRCLE_RADIUS, cPaint);
		}
		
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width=w;
		height=h;
		diagonal = Math.sqrt(w*w+h*h)/2;
	}
}
