package com.zyf.view.lemonhello;

import android.content.Context;

/**
 * LemonHello info包
 * 在消息队列中使用，包含Context和LemonHelloInfo
 */

public class LemonHelloInfoPack {

    private Context context;
    private LemonHelloInfo helloInfo;

    public LemonHelloInfoPack(Context context, LemonHelloInfo helloInfo) {
        this.context = context;
        this.helloInfo = helloInfo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LemonHelloInfo getHelloInfo() {
        return helloInfo;
    }

    public void setHelloInfo(LemonHelloInfo helloInfo) {
        this.helloInfo = helloInfo;
    }
}
