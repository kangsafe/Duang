package com.ks.duang.fire;

/**
 * Created by Admin on 2018/6/18 0018 12:25.
 * Author: kang
 * Email: kangsafe@163.com
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.ks.duang.R;
import com.ks.duang.Utils;
import com.plattysoft.leonids.ParticleSystem;

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


    @Override
    protected void onResume() {
        super.onResume();
        //爆炸效果
        new ParticleSystem(this, 1000, R.drawable.ic_praise_five, 3000)
                .setSpeedModuleAndAngleRange(0.05f, 0.2f, 0, 360)
                .setRotationSpeed(30)
                .setAcceleration(0, 90)
                .oneShot(getWindow().getDecorView(), 200);
        //飞花
        new ParticleSystem(this, 1000, R.drawable.ic_praise_six, 10000)
                .setSpeedModuleAndAngleRange(0.05f, 0.2f, 0, 90)
                .setRotationSpeed(60)
                .setAcceleration(0.00005f, 90)
                .emit(0, -100, 30, 10000);
    }
}
