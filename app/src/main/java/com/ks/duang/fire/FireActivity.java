package com.ks.duang.fire;

/**
 * Created by Admin on 2018/6/18 0018 12:25.
 * Author: kang
 * Email: kangsafe@163.com
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.ks.duang.Utils;

public class FireActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SurfaceView v = new SurfaceView(this);
        HolderSurfaceView.getInstance().setSurfaceView(v);
//        v.setBackgroundResource(R.drawable.bg);
        setContentView(v);
        DrawYH yh = new DrawYH();
        v.setOnTouchListener(yh);
        yh.begin();
        Utils.installShortcut(this, "com.ks.duang.fire");
    }
}
