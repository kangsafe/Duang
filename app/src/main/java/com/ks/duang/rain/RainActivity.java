package com.ks.duang.rain;

/**
 * Created by Admin on 2018/6/18 0018 12:06.
 * Author: kang
 * Email: kangsafe@163.com
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.Random;


public class RainActivity extends Activity {
    //两两弹幕之间的间隔时间
    public static final int DELAY_TIME = 800;

    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        RainView view = new RainView(this, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        System.exit(0);
    }
}
