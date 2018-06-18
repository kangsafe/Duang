package com.ks.duang.fire;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.ks.duang.Utils;

public class RollView extends View {
    private Bitmap mBitmap;
    private Rect mSrc;
    private Rect mDst;
    private int mRollWidth = 60;
    private float mRate;
    private boolean mIsStopAnim;
    private Context mContext;

    public RollView(Context context) {
        super(context);
        mContext = context;
        mSrc = new Rect();
        mDst = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) return;

        drawFromMiddleByFloatCompute(canvas);

    }

    private void drawFromMiddleByFloatCompute(Canvas canvas) {
           /*
           以下src 都需要加上mBitmap. 的前缀，， 因从drawable拿到的是原始图片宽高
           而适配时，可能view的宽高比 drawable的宽高还小或大
            */
        final float rate = mRate;

        mSrc.left = 0;
        mSrc.top = 0;
        mSrc.right = mRollWidth;
        mSrc.bottom = mBitmap.getHeight();

        mDst.left = (int) ((getWidth() / 2 - mRollWidth) - (getWidth() / 2 - mRollWidth) * rate);
        mDst.top = 0;
        mDst.right = mDst.left + mRollWidth + 1;//因精度问题，这里强制+1
        mDst.bottom = getHeight();
        canvas.drawBitmap(mBitmap, mSrc, mDst, null);

        //中间
        int sw = (int) ((mBitmap.getWidth() - mRollWidth * 2) * rate);
        mSrc.left = mBitmap.getWidth() / 2 - sw / 2;
        mSrc.top = 0;
        mSrc.right = mSrc.left + sw;
        mSrc.bottom = mBitmap.getHeight();

        int dw = (int) ((getWidth() - mRollWidth * 2) * rate);
        mDst.left = getWidth() / 2 - dw / 2;
        mDst.top = 0;
        mDst.right = mDst.left + dw;
        mDst.bottom = getHeight();
        canvas.drawBitmap(mBitmap, mSrc, mDst, null);

        //右边
        mSrc.left = mBitmap.getWidth() - mRollWidth;
        mSrc.top = 0;
        mSrc.right = mBitmap.getWidth();
        mSrc.bottom = mBitmap.getHeight();

        mDst.left = (int) (getWidth() / 2 + (getWidth() / 2 - mRollWidth) * rate);
        mDst.top = 0;
        mDst.right = mDst.left + mRollWidth;
        mDst.bottom = getHeight();

        canvas.drawBitmap(mBitmap, mSrc, mDst, null);
    }

    public void setRes(int resId) {
        mBitmap = Utils.getBitmap(mContext, resId);
    }

    public void startFloatComputeAnim() {
           /*
           如果有float获取比率值，从而计算出相应的坐标值，那么可能由于最终在转成Rect的坐标时，
           float to int ，有精度的损失：1个px    而引起效果的不理想
            */
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mIsStopAnim) {
                    animation.cancel();
                    return;
                }
                mRate = (float) animation.getAnimatedValue();
                invalidate();

            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    public void stopAnim() {
        mIsStopAnim = true;
    }
}
