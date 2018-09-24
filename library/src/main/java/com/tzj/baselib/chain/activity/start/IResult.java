package com.tzj.baselib.chain.activity.start;

/**
 * activity 返回的接口
 */
public interface IResult<T extends ActivityResult> {
    /**
     * 不是一定会被调用，比如被拦截了
     * @param result
     */
    void onActivityResult(T result);
}
