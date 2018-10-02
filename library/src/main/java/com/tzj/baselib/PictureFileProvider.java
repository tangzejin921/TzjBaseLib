package com.tzj.baselib;

import android.support.v4.content.FileProvider;
/**
 * 其他裤如果用了 FileProvider，这里就用不了FileProvider 了
 * 所以继承了一个 PictureFileProvider 避免冲突
 */
public class PictureFileProvider extends FileProvider{

}
