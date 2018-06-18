package com.ks.duang.snow;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.ks.duang.R;

/**
 * Created by Admin on 2018/6/18 0018 13:05.
 * Author: kang
 * Email: kangsafe@163.com
 */
public class SnowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_snow);
        SnowView view = new SnowView(this, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(view);
    }
}
