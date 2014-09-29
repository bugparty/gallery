package com.os1.camera.gallery.util;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;

public class TextViewReflector extends ViewReflector {
    public final static String TAG = "TextViewReflector";
    private final static Class mTextView = TextView.class;


    private Field mX, mY;
    private Field mContext;
    public TextViewReflector(Object o) {
        super(o);
        try {

            mX = mTextView.getDeclaredField("mX");
            mX.setAccessible(true);
            mY = mTextView.getDeclaredField("mY");
            mY.setAccessible(true);


        } catch (NoSuchFieldException e) {
            Log.e(TAG, "can`t find "+e.getMessage());
            e.printStackTrace();

        }
    }
    public Context getmContext(){
        try {
            return (Context)mContext.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getX(){
        try {
            return (Integer)mX.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getY(){
        try {
            return (Integer)mY.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
