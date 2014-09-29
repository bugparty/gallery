package com.os1.camera.gallery.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ViewReflector {
    public final static String TAG = "ViewReflector";
    private final static  Class mTextView = TextView.class;
    private final static Class mView = View.class;
    private Field mLeft,mRight, mTop, mBottom;
    private Field mScrollX,mScrollY;
    private Field mContext;
    protected Object ctx;
    public ViewReflector(Object o) {
        ctx = o;
        try {
            mLeft =  mView.getDeclaredField("mLeft");
            mLeft.setAccessible(true);
            mRight = mView.getDeclaredField("mRight");
            mRight.setAccessible(true);
            mTop = mView.getDeclaredField("mTop");
            mTop.setAccessible(true);
            mBottom = mView.getDeclaredField("mBottom");
            mBottom.setAccessible(true);
            
            mContext = mView.getDeclaredField("mContext");
            mContext.setAccessible(true);
            mScrollX = mView.getDeclaredField("mScrollX");
            mScrollX.setAccessible(true);
            mScrollY = mView.getDeclaredField("mScrollY");
            mScrollY.setAccessible(true);



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
    public int getSrcollX(){
        try {
            return (Integer)mScrollX.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getmScrollY(){
        try {
            return (Integer)mScrollY.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getLeft(){
        try {
            return (Integer)mLeft.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getRight(){
        try {
            return (Integer)mRight.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getTop(){
        try {
            return (Integer)mTop.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getBottom(){
        try {
            return (Integer)mBottom.get(ctx);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
