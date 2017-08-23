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

	private Paint cPaint;//����һ����СԲ�Ļ���
	private Paint bPaint;//�������Ļ���
	private int width;
	private int height;
	private int[] colorArray;//СԲ��ɫ������
	private static final int MAX_DOT_CONUNT=6;//���СԲ�ĸ���
	private static int BIG_CIRCLE_RADIUS=80;//СԲ������Ļ���ĵľ���
	private static final int S_CIRCLE_RADIUS=10;//СԲ�İ뾶
	private float singleAngle;//ÿ��СԲ֮��ļн�
	private float rotateAngle=0;//��ת����ת���ĽǶ�
	private int currentRadius=BIG_CIRCLE_RADIUS;//��ǰСԲ������Ļ���ĵľ���
	public SplashView(Context context) {
		super(context);
		init();
	}
	//��ʼ��һЩ����
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
	//�ṩ����ӵ��õ�,����ҳ���汾���º���Ե���ֹͣ��һ�׶ζ������ҿ����ڶ��׶�
	public void stopRotate(){
		if(mAnimator != null)
		{
			mAnimator.cancel();
			design=new ScaleAnim();
			invalidate();
		}
	}
	//����һ������ĸ���,�������ģʽ���Ƿǳ����,��������ѧ����
	private abstract class MatrielDesign{
		public abstract void startAnim(Canvas canvas); 
	}

	//��һ�׶ε���ת����,��ʵ���Ǹı��rotateAngle��С,��drawCircle()��ʱ�����ÿ�μ�ʱ�����ÿ��СԲ������
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
	//�ڶ��׶εĶ���,��ʵ���Ǹı��СԲ����Ļ���ĵľ���
	//����ValueAnimator.ofFloat(1.0f,0.2f,1.2f)�ö�����һ���ص�Ч��
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
	//�����׶�ˮ���ƶ���,��ʵ���ǻ�������ʱ��һ��Բ,Ȼ��������ʵ����߿�ȾͿ�����
	//���˼·Ҳ������һ��Բ��ͷ���Ч��,�������?
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
		//�����׶εĶ���ֻ��Ҫ����������
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
		}else{//�����׶ζ���
			canvas.drawCircle(width/2, height/2, crinkleCircle+strokeWidth/2, bPaint);
		}
	}
	//����ÿ��СԲ������,��СԲ
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
