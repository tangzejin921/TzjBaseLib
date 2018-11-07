package android.os;

import java.io.Closeable;

/**
 * 可关闭的Handler
 * 1. 清除post内容
 * 2. post将不会执行
 */
public class CloseHandler extends Handler implements Closeable {
    protected boolean clsed = false;

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        if (clsed) {
            return false;
        }
        return super.sendMessageAtTime(msg, uptimeMillis);
    }

    public boolean isClsed() {
        return clsed;
    }

    @Override
    public void close(){
        clsed = true;
        removeMessages(0);
    }
}
