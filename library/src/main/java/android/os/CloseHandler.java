package android.os;

import android.util.Log;

import java.io.Closeable;

/**
 * 可关闭的Handler
 * 1. 清除post内容
 * 2. post将不会执行
 */
public class CloseHandler extends Handler implements Closeable {
    private static final String TAG = "CloseHandler";
    protected boolean mClosed = false;

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        if (mClosed) {
            Log.e(TAG, "sendMessageAtTime handler is closed");
            return false;
        }
        return super.sendMessageAtTime(msg, uptimeMillis);
    }

    @Override
    public void dispatchMessage(Message msg) {
        if (mClosed) {
            Log.e(TAG, "dispatchMessage handler is closed");
        } else {
            super.dispatchMessage(msg);
        }
    }

    public boolean isClosed() {
        return mClosed;
    }

    @Override
    public void close() {
        mClosed = true;
        removeMessages(0);
    }


    /**
     * 阻塞调用线程，等待 Handler 线程执行完runable/超时
     * https://blog.csdn.net/plokmju88/article/details/107551413
     * 1. Handler 的 Looper 不允许退出，例如 Android 主线程 Looper 就不允许退出；
     * 2. Looper 退出时，使用安全退出 quitSafely() 方式退出；
     *
     * @param timeOutRun 超时了runnable是否要执行
     * @return null：还没执行Run false：已经执行Run了但是没在规定时间内完成。 true：timeout时间内完成了
     */
    public final Boolean runWithScissors(final Runnable r, long timeout, boolean timeOutRun) {
        if (r == null) {
            throw new IllegalArgumentException("runnable must not be null");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative");
        }

        if (Looper.myLooper() == getLooper()) {
            r.run();
            return true;
        }

        BlockingRunnable br = new BlockingRunnable(r, timeOutRun);
        return br.postAndWait(this, timeout);
    }


    private static final class BlockingRunnable implements Runnable {
        private final Runnable mTask;
        private final boolean mTimeOutRun;
        //表示是否执行完，没执行完包括没执行和执行中。
        private Boolean mDone;

        public BlockingRunnable(Runnable task, boolean timeOutRun) {
            mTask = task;
            mTimeOutRun = timeOutRun;
        }

        //Handler线程
        @Override
        public void run() {
            try {
                synchronized (this) {
                    mDone = false;
                }
                mTask.run();
            } finally {
                synchronized (this) {
                    mDone = true;
                    notifyAll();
                }
            }
        }

        //调用线程
        public boolean postAndWait(Handler handler, long timeout) {
            if (!handler.post(this)) {
                Log.e(TAG, "postAndWait handler post err");
                return mDone;
            }

            synchronized (this) {
                if (timeout > 0) {
                    final long expirationTime = SystemClock.uptimeMillis() + timeout;
                    while (mDone == null || !mDone) {
                        long delay = expirationTime - SystemClock.uptimeMillis();
                        if (delay <= 0) {
                            //超时了还没执行到,就取消执行
                            if (mTimeOutRun) {
                                handler.removeCallbacks(this);
                            }
                            Log.e(TAG, "postAndWait timeOut=" + timeout + ",mDone=" + mDone + ",mTimeOutRun=" + mTimeOutRun);
                            return mDone; // timeout TODO 这里是否会发生 超时但是Runable被执行了
                        }
                        try {
                            wait(delay);
                        } catch (InterruptedException ex) {
                        }
                    }
                } else {
                    while (mDone == null || !mDone) {
                        try {
                            //这里有问题，会发生死锁
                            wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
            return mDone;
        }
    }
}
