//package com.ks.duang.heart;
//
///**
// * Created by Admin on 2018/6/18 0018 16:34.
// * Author: kang
// * Email: kangsafe@163.com
// */
//import android.os.Bundle;
//
//import com.ks.duang.R;
//
///**
// * 测试烟花效果
// */
//
//public class BombViewActivity extends BaseActivity {
//
//    private BombView mBombView;
//
//    private Runnable task = new Runnable() {
//        @Override
//        public void run() {
//            mBombView.startBomb();
//            getUIHandler().postDelayed(task, 1000);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_bomb_view);
//
//        mBombView = (BombView) findViewById(R.id.bombview);
//
//        getUIHandler().postDelayed(task, 1000);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mBombView.release();
//    }
//}
