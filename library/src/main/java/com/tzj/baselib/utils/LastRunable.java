package com.tzj.baselib.utils;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;


/**
 * 取消xxx时间之内的操作,保留最后一次
 * 如果超过一定时间一定会执行，避免无限调用延时
 * 是否执行需要判断数据是否发生了改变
 */
public abstract class LastRunable<T> implements Runnable {
    /**
     * 默认延时时间
     */
    public static final int DEFAULT_TIME = 250;
    /**
     * 缺省延时时间，不设置默认是　DEFAULT_TIME
     */
    protected int mDelayTime = DEFAULT_TIME;
    /**
     * 超过多少时间间隔将强制执行一次。忽略延时时间
     */
    protected Integer mForceRunOverTime;
    /**
     * 最后一次执行的时间
     */
    protected Long mLastRunTime;
    /**
     * 将要改变的数据
     */
    protected T mCurtObj = null;

    /**
     * @param delayTime 缺省延时时间
     */
    public void setDelayTime(int delayTime) {
        this.mDelayTime = delayTime;
    }

    /**
     * @param forceRunOverTime 超过forceRunOverTime时间间隔将强制执行一次。忽略延时时间
     */
    public void setForceRunOverTime(int forceRunOverTime) {
        this.mForceRunOverTime = forceRunOverTime;
    }

    /**
     * @return 获取要执行任务所携带的数据
     */
    public T getObj() {
        return mCurtObj;
    }

    public Handler getHandler() {
        return new Handler();
    }

    /**
     * @return 最后一次执行距离当前是否超过指定的时间间隔
     */
    public boolean isOverTime() {
        if (mForceRunOverTime == null || mLastRunTime == null) {
            return false;
        }
        return SystemClock.elapsedRealtime() - mLastRunTime > mForceRunOverTime;
    }

    /**
     * 移除将要执行的任务
     */
    public void remove() {
        getHandler().removeCallbacks(this);
    }

    /**
     * 当数据改变时才会延时默认时间执行
     *
     * @return 是否需要执行
     */
    public boolean changeDelayRun(T obj) {
        return changeDelayRun(obj, mDelayTime);
    }

    /**
     * 当数据改变时才会延时指定时间执行
     *
     * @return 是否需要执行
     */
    public boolean changeDelayRun(T obj, int delayTime) {
        if (mCurtObj != null && mCurtObj.equals(obj) && !isOverTime()) {
            return false;
        }
        delayRun(obj, delayTime);
        return true;
    }

    /**
     * 不检查数据是否改变都去延时默认时间执行
     */
    public void delayRun(T obj) {
        delayRun(obj, mDelayTime);
    }

    /**
     * 不检查数据是否改变都去延时指定时间执行
     */
    public void delayRun(T obj, int delayTime) {
        if (isOverTime()) {
            delayTime = 0;
        }
        mCurtObj = obj;
        remove();
        getHandler().postDelayed(this, delayTime);
    }

    @Override
    public void run() {
        try {
            run(mCurtObj);
        } catch (Throwable e) {
            Log.e(getClass().getSimpleName(), "抛出了异常但被捕获了，请检查代码", e);
        } finally {
            if (mForceRunOverTime != null) {
                mLastRunTime = SystemClock.elapsedRealtime();
            }
        }
    }

    /**
     * 被执行体，已经捕获了异常。
     */
    public abstract void run(T data);
}
