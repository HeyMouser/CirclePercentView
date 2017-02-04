package com.yh.customviewone;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YH on 2017/2/4.
 */

public class CirclePercentView extends View {

    private int mCirColor;//大圆背景色
    private int mProColor;//进度条颜色
    private int mProWidth;//进度条的宽度
    private int mRadius = 30;//圆的半径
    private int mPerTextSize;//百分比大小
    private int mPerTextColor;//百分比字体颜色


    private float mCurPercent = 0.0f;//百分比

    private Paint mCirPaint;//背景画笔
    private Paint mProPaint;//进度条画笔
    private Paint mPerPaint;//百分比画笔

    private Rect mBounds;//绘制百分比的范围
    private RectF mRectF;//圆弧的外接矩形

    private OnClickListener mOnclickListener;

    public void setCurPercent(float curPercent) {
        ValueAnimator anim = ValueAnimator.ofFloat(mCurPercent, curPercent);
        anim.setDuration((long) (Math.abs(mCurPercent - curPercent) * 20));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCurPercent = (float) (Math.round(value * 10)) / 10;//四舍五入保留到小数点后两位
                invalidate();
            }
        });
        anim.start();
    }

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);

        mCirColor = ta.getColor(R.styleable.CirclePercentView_circleBg, 0xff3d3d3d);//默认黑灰色

        mPerTextColor = ta.getColor(R.styleable.CirclePercentView_percentColor, 0xffff3030);
        mPerTextSize = ta.getDimensionPixelSize(R.styleable.CirclePercentView_percentSize, DensityUtils.sp2px(context, 14));

        mProWidth = ta.getDimensionPixelSize(R.styleable.CirclePercentView_progressWidth, DensityUtils.dp2px(context, 5));
        mProColor = ta.getColor(R.styleable.CirclePercentView_progressColor, 0xfff0f0f0);

        mRadius = ta.getDimensionPixelSize(R.styleable.CirclePercentView_radius, DensityUtils.dp2px(context, 100));

        ta.recycle();

        mCirPaint = new Paint();
        mCirPaint.setColor(mCirColor);

        mPerPaint = new Paint();
        mPerPaint.setColor(mPerTextColor);
        mPerPaint.setTextSize(mPerTextSize);


        mProPaint = new Paint();
        mProPaint.setStyle(Paint.Style.STROKE);
        mProPaint.setColor(mProColor);
        mProPaint.setStrokeWidth(mProWidth);

        mBounds = new Rect();
        mRectF = new RectF();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclickListener != null) {
                    mOnclickListener.onClick(CirclePercentView.this);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {//指定宽高了
            result = specSize;
        } else {// 一般为WARP_CONTENT
            result = 2 * mRadius;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画背景圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mCirPaint);
        //画进度条
        mRectF.set(getWidth() / 2 - mRadius + mProWidth / 2, getHeight() / 2 - mRadius + mProWidth / 2, getWidth() / 2 + mRadius - mProWidth / 2, getHeight() / 2 + mRadius - mProWidth / 2);
        canvas.drawArc(mRectF, 270, 360 * mCurPercent / 100, false, mProPaint);
        //画百分比文本
        String text = mCurPercent + "%";
        mPerPaint.getTextBounds(text, 0, text.length(), mBounds);
        canvas.drawText(text, getWidth() / 2 - mBounds.width() / 2, getHeight() / 2 + mBounds.height() / 2, mPerPaint);

    }
}
