package com.ks.duang;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ks.duang.barrage.BarrageView;
import com.ks.duang.crack.CustomView;
import com.ks.duang.lightning.LightningView;
import com.ks.duang.rain.RainView;
import com.ks.duang.snow.SnowView;

import java.util.Random;

public class MainActivity extends Activity {

    //两两弹幕之间的间隔时间
    public static final int DELAY_TIME = 500;

    /**
     * 标签：程序是否处于暂停状态
     * 15/11/01 测试按Home后一分钟以上回到程序会发生满屏线程阻塞
     */
    private boolean isOnPause = false;
    private Random random = new Random();
    RainView rainView;
    LightningView lightningView;
    CustomView crachView;
    SnowView snowView;
    RelativeLayout layout;

    //模拟触屏点击屏幕事件
    private void doOnTouch(final View view) {
        if (view != null && view.getWidth() > 0) {
            final int x = random.nextInt(view.getWidth());
            final int y = random.nextInt(view.getHeight());
            final long downTime = SystemClock.uptimeMillis();
            final MotionEvent downEvent = MotionEvent.obtain(
                    downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);

            //添加到webview_loading_round_iv上
            view.onTouchEvent(downEvent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final MotionEvent upEvent = MotionEvent.obtain(
                            downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
                    view.onTouchEvent(upEvent);
                    downEvent.recycle();
                    upEvent.recycle();
                }
            }, 1000);
        }
    }

    //添加玻璃破碎效果
    final Handler crackHandler = new Handler();
    Runnable crackRunable = new Runnable() {
        @Override
        public void run() {
            doOnTouch(crachView);
            //发送下一条消息
            crackHandler.postDelayed(this, 5000);
        }
    };
    //产生闪电效果
    final Handler lightningHandler = new Handler();
    final Runnable lightRunable = new Runnable() {
        @Override
        public void run() {
            doOnTouch(lightningView);
            //发送下一条消息
            lightningHandler.postDelayed(this, 5000);
        }
    };//下雨
    Handler rainHandler = new Handler();
    Runnable rainRunable = new Runnable() {
        @Override
        public void run() {
            layout.addView(rainView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout.addView(crachView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            lightningHandler.removeCallbacksAndMessages(null);
            layout.removeView(lightningView);
            crackHandler.postDelayed(crackRunable, 3000);
        }
    };
    //下雪
    Handler snowHandler = new Handler();
    Runnable snowRunable = new Runnable() {
        @Override
        public void run() {
            layout.addView(snowView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout.removeView(rainView);
            layout.removeView(crachView);
            rainHandler.removeCallbacksAndMessages(null);
            crackHandler.removeCallbacksAndMessages(null);
        }
    };
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rainView = new RainView(this, null);
        lightningView = new LightningView(this, null);
        crachView = new CustomView(this, null);
        snowView = new SnowView(this, null);
        layout = new RelativeLayout(this);    // 变量layout是该Activity的成员变量（private LinearLayout layout）
        layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));    // 设置layout布局方向为垂直
        setContentView(layout);
        layout.addView(lightningView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lightningHandler.postDelayed(lightRunable, 3000);
        rainHandler.postDelayed(rainRunable, 30000);
        snowHandler.postDelayed(snowRunable, 60000);
        //读取文字资源
        final String[] texts = getResources().getStringArray(R.array.default_text_array);

        //设置宽高全屏
        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        Runnable createBarrageView = new Runnable() {
            @Override
            public void run() {
                if (!isOnPause) {
                    Log.e("azzz", "发送弹幕");
                    //新建一条弹幕，并设置文字
                    final BarrageView barrageView = new BarrageView(MainActivity.this);
                    barrageView.setText(texts[random.nextInt(texts.length)]); //随机设置文字
                    addContentView(barrageView, lp);
                }
                //发送下一条消息
                handler.postDelayed(this, DELAY_TIME);
            }
        };
        handler.postDelayed(createBarrageView, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    protected void onDestroy() {
        if (lightningHandler != null) {
            lightningHandler.removeCallbacksAndMessages(null);
        }
        if (rainHandler != null) {
            rainHandler.removeCallbacksAndMessages(null);
        }
        if (crackHandler != null) {
            crackHandler.removeCallbacksAndMessages(null);
        }
        if (snowHandler != null) {
            snowHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
