package com.ks.duang.crack;

/**
 * Created by Admin on 2018/6/18 0018 12:42.
 * Author: kang
 * Email: kangsafe@163.com
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

public class CrackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomView view = new CustomView(this, null);
        setContentView(view);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Quit")
                .setMessage("要退出吗？")
                .setNegativeButton("我要退出",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        })
                .setPositiveButton("更多推荐",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).show();
    }
}
