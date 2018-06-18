package com.ks.duang.fire;

import android.content.Context;

class DotFactory {
    public Dot makeDot(Context mContext, int rand, int x, int y) {
        return new Dot();
    }
}
