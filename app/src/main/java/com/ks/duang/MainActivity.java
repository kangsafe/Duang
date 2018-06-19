package com.ks.duang;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ks.duang.barrage.BarrageView;
import com.ks.duang.crack.CustomView;
import com.ks.duang.bomb.BombView;
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
    BombView bombView;

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
            }, 2000);
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
    Handler bombHandler = new Handler();
    private Runnable bombRunnable = new Runnable() {
        @Override
        public void run() {
            bombView.startBomb();
            bombHandler.postDelayed(this, 1000);
        }
    };

    MediaProjectionManager mediaProjectionManager;
    boolean isrun = false;//用来标记录屏的状态private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;//录制视频的工具private int width, height, dpi;//屏幕宽高和dpi，后面会用到
    private ScreenRecorder screenRecorder;//这个是自己写的录视频的工具类，下文会放完整的代码
    Thread thread;//录视频要放在线程里去执行
    int width;
    int height;
    int dpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        }
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
        dpi = outMetrics.densityDpi;
        startScreen();
        rainView = new RainView(this, null);
        lightningView = new LightningView(this, null);
        crachView = new CustomView(this, null);
        snowView = new SnowView(this, null);
        bombView = new BombView(this, null);
        layout = new RelativeLayout(this);    // 变量layout是该Activity的成员变量（private LinearLayout layout）
        layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));    // 设置layout布局方向为垂直
        setContentView(layout);
        layout.addView(lightningView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        lightningHandler.postDelayed(lightRunable, 3000);
        rainHandler.postDelayed(rainRunable, 15000);
        snowHandler.postDelayed(snowRunable, 30000);

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
        handler.postDelayed(createBarrageView, 20000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.addView(bombView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                layout.removeView(snowView);
                handler.removeCallbacksAndMessages(null);
                bombHandler.post(bombRunnable);
            }
        }, 45000);
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
        if (bombHandler != null) {
            bombHandler.removeCallbacksAndMessages(null);
        }
        if (bombView != null) {
            bombView.release();
        }
        screenRecorder.destory();
        isrun = false;
        thread.stop();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 102) {
            Toast.makeText(this, "缺少读写权限", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == 103) {
            Toast.makeText(this, "缺少录音权限", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == 104) {
            Toast.makeText(this, "缺少相机权限", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode != 101) {
            Log.e("HandDrawActivity", "error requestCode =" + requestCode);
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "捕捉屏幕被禁止", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        }
        if (mediaProjection != null) {
            screenRecorder = new ScreenRecorder(width, height, mediaProjection, dpi);
        }
        thread = new Thread() {
            @Override
            public void run() {
                screenRecorder.startRecorder();//跟ScreenRecorder有关的下文再说，总之这句话的意思就是开始录屏的意思
            }
        };
        thread.start();
        //binding.startPlayer.setText("停止");//开始和停止我用的同一个按钮，所以开始录屏之后把按钮文字改一下
        isrun = true;//录屏状态改成真
    }

    private void startScreen() {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            intent = mediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(intent, 101);//正常情况是要执行到这里的,作用是申请捕捉屏幕
        } else {
            Toast.makeText(this, "Android版本太低，无法使用该功能", Toast.LENGTH_SHORT).show();
        }
    }
}
