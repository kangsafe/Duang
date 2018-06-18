package com.ks.duang.explosion;

/**
 * Created by Admin on 2018/6/18 0018 12:25.
 * Author: kang
 * Email: kangsafe@163.com
 */
import android.app.Activity;
import android.os.Bundle;

import com.ks.duang.R;

public class ExplosionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explosion);

        ExplosionField explosionField = new ExplosionField(this);

        explosionField.addListener(findViewById(R.id.root));
    }
}
