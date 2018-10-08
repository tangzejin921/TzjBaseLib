package com.tzj.baselib.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * 类似 rx 链式调用，用了rx这个完全可以去除
 */
public class UtilThread {
    public static class Task {
        private Handler handler = new Handler(Looper.getMainLooper());
        private volatile Vector<UiRunnable> list = new Vector<>();

        public Task run(Runnable runnable) {
            synchronized (list) {
                if (list.size() > 0) {
                    list.lastElement().add(runnable);
                } else {
                    AsyncTask.SERIAL_EXECUTOR.execute(runnable);
                }
            }
            return this;
        }

        public Task ui(final Runnable runnable) {
            synchronized (list) {
                final UiRunnable uiRunnable = new UiRunnable(list,runnable);
                list.add(uiRunnable);
                AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(uiRunnable);
                    }
                });
            }
            return this;
        }
    }

    public static Task run(Runnable runnable) {
        return new Task().run(runnable);
    }

    public static Task ui(Runnable runnable) {
        return new Task().ui(runnable);
    }
}

class UiRunnable implements Runnable {
    /**
     * 主线程要运行的
     */
    private Runnable runnable;
    /**
     * 主线程运行完要运行的其他线程
     */
    private List<Runnable> list = new ArrayList<>();
    private volatile Vector<UiRunnable> vector;
    public UiRunnable(Vector vector,Runnable runnable) {
        this.vector = vector;
        this.runnable = runnable;
    }

    public void add(Runnable runnable) {
        list.add(runnable);
    }

    @Override
    public void run() {
        runnable.run();
        Iterator<Runnable> iterator = list.iterator();
        while (iterator.hasNext()) {
            Runnable next = iterator.next();
            iterator.remove();
            AsyncTask.SERIAL_EXECUTOR.execute(next);
        }
        vector.remove(this);
    }
}
