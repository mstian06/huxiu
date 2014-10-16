package com.mstian.huxiu;

import android.app.Application;
import android.content.Context;

public class AppData extends Application{
    
    private static Context mContext;
    public static Context getContext()
    {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
