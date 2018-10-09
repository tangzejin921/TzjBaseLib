package com.tzj.baselib.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;

import com.tzj.baselib.env.AppEnv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作相关
 */
public class UtilFile {

    /**
     * 保存类
     */
    public static void saveObj(Object obj, File file) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file, false));
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取类
     */
    public static Object readObj(File file) {
        ObjectInputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectInputStream(new FileInputStream(file));
            Object o = objectOutputStream.readObject();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * path为文件夹 保证存在
     * path为文件 保证父文件夹存在
     *
     * @return: boolean 是否存在此文件(如里是文件夹则为true)
     */
    public static boolean isHaveFile(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        if (file.isFile()) {// 是文件
            if (file.exists()) {
                return true;// 存在此文件
            }
            file = file.getParentFile();
        }
        if (!file.exists()) {
            file.mkdirs();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 输入流保存为文件
     * 注意is这里没有关闭确保外部关闭is
     *
     * @param path     这个由外部保证不为空,且文件夹存在并以/结速
     * @param name     ---这个由外部保证不为空
     * @param isStrong --有文件时是否要复盖
     * @return: String 文件路径
     */
    public static String save(InputStream is, String path, String name, boolean isStrong) {
        return save(is, path, name, isStrong, null);
    }

    /**
     * 输入流保存为文件
     * 注意is这里没有关闭确保外部关闭is
     *
     * @param is
     * @param path     这个由外部保证不为空,且文件夹存在并以/结速
     * @param name     ---这个由外部保证不为空
     * @param isStrong --有文件时是否要复盖
     * @return: String 文件路径
     */
    public static String save(InputStream is, String path, String name, boolean isStrong, final Handler.Callback p) {
        File filePath = new File(path + name);
        if (!isStrong && filePath.exists()) {//
            return filePath.getAbsolutePath();
        }
        return save(is, filePath, p, null);
    }

    /**
     * @param is       is 结束后is没关
     * @param file
     * @param isStrong 判断是否存在此文件了,存在则不下载
     * @return: String
     */
    public static String save(InputStream is, File file, boolean isStrong) {
        return save(is, file, isStrong, null);
    }

    /**
     * @param is       is 结束后is没关
     * @param file
     * @param isStrong 判断是否存在此文件了,存在则不下载
     * @param p
     * @return
     * @return: String
     */
    public static String save(InputStream is, File file, boolean isStrong, final Handler.Callback p) {
        if (!isStrong && file.exists()) {//
            return file.getAbsolutePath();
        }
        return save(is, file, p, null);
    }

    /**
     * 不做任判断直接保存，可能覆盖文件
     * Callback 返回false 不进行进度通知
     *
     * @param is   结束后is没关
     * @param file
     * @param p
     * @return
     * @return: String
     */
    public static String save(InputStream is, File file, final Handler.Callback p, Message msg) {
        if (msg == null && p != null) {
            msg = Message.obtain();
        }
        final Message obtain = msg;
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                if (!p.handleMessage(obtain)) {//中断通知进度吗？
                    obtain.what = -1;
                }
            }
        };

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int len;
            if (p != null) {    //对外通知进度且在ui线程
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    obtain.arg1 += len;
                    if (obtain.what != -1) {
                        AppEnv.post(run);
                    }
                }
            } else {            //不对外通知进度
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveBitmap(Bitmap bitmap, String path, String name, boolean isStrong) {
        return saveBitmap(bitmap, path, name, isStrong, 75);
    }

    /**
     * 根据Bitmap 保存图片
     *
     * @param bitmap
     * @param path     这个由外部保证不为空,且文件夹存在并以/结速
     * @param name     外部保证不为空
     * @param isStrong --有文件时是否要复盖
     * @return: String
     * 文件路径
     */
    public static String saveBitmap(Bitmap bitmap, String path, String name, boolean isStrong, int zip) {
        //bitmap
        if (bitmap == null) {
            return null;
        }
        //是否已存在此图片
        File haveName = new File(path + name);
        if (!isStrong && haveName.exists()) {
            return haveName.getPath();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(haveName);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, zip, fos);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception e) {
            }
        }
        return haveName.getAbsolutePath();
    }

    /**
     * 删除文件/文件夹
     *
     * @param path
     * @return: void
     */
    public static boolean delFile(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        if (file.isFile()) {
            return file.delete();
        }
        return file.isDirectory() && delFolder(path);
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹的路径
     */
    public static boolean delFolder(String folderPath) {
        delAllFile(folderPath);
        File myFilePath = new File(folderPath);
        return myFilePath.delete();
    }

    /**
     * 删除文件
     *
     * @param path 文件的路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp;
        for (String aTempList : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + aTempList);
            } else {
                temp = new File(path + File.separator + aTempList);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + aTempList);
                delFolder(path + File.separator + aTempList);
            }
        }
    }

    /**
     * 取得文件大小
     *
     * @param f
     * @return
     * @throws Exception
     * @return: long
     */
    public static long getFileSize(File f) {// 取得文件夹大小
        long size = 0;
        if (f.isFile()) {
            return f.length();
        }
        File flist[] = f.listFiles();
        for (File aFlist : flist) {
            if (aFlist.isDirectory()) {
                size = size + getFileSize(aFlist);
            } else {
                size += aFlist.length();
            }
        }
        return size;
    }

    /**
     * 换算文件大小
     *
     * @param size
     * @return
     */
    public static String getFileSize(long size) {
        return Formatter.formatFileSize(AppEnv.getAppCtx(), size);
    }

    /**
     * 复制文件夹
     */
    public static void copyFile(File from, String filePath) {
        File to = new File(filePath);
        if (!to.exists()) {
            to.mkdirs();
        }
        if (from.isFile()) {
            try {
                copyFileFast(new FileInputStream(from), new FileOutputStream(filePath + "/" + from.getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        File[] listFiles = from.listFiles();
        for (File listFile : listFiles) {
            if (listFile.isFile()) {
                copyFile(listFile, filePath);
            } else {
                copyFile(listFile, filePath + "/" + listFile.getName());
            }
        }
    }

    /**
     * 复制文件
     *
     * @param from 文件
     * @param to   文件
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 快速复制文件（采用nio操作）
     *
     * @param is 数据来源
     * @param os 数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os) throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    /**
     * 读出为string
     */
    public static StringBuffer readToString(InputStream is) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = fileReader.readLine();
            while (null != line) {
                stringBuffer.append(line);
                line = fileReader.readLine();
            }
            fileReader.close();//读完要及时关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * 得到path下的所有文件
     *
     * @param path
     * @return
     * @return: File[]
     */
    public static List<String> getFiles(String path) {
        File file = new File(path);
        List<String> retFiles = new ArrayList<>();
        File[] listFiles = file.listFiles();
        for (File listFile : listFiles) {
            if (listFile.isDirectory()) {
                List<String> files = getFiles(listFile.getAbsolutePath());
                for (int j = 0; j < files.size(); j++) {
                    retFiles.add(files.get(j));
                }
            } else {
                retFiles.add(listFile.getAbsolutePath());
            }
        }
        return retFiles;
    }

    /**
     * 文件类型
     */
    public static FileType getType(String str) {
        if (str == null) {
            return FileType.未知;
        }

        String music = ".mp3.wav.mid.midi.wma.amr.ogg.m4a";
        String video = ".3gp.mp4.flv.avi.mov.3gp.m4v.wmv.rm.rmvb.mkv.ts.webm";
        String zip = ".zip";
        String pic = ".jpg.png.jpeg.bmp.gif";
        int lastIndexOf = str.lastIndexOf('.');
        str = str.substring(lastIndexOf, str.length());
        if (music.contains(str)) {
            return FileType.音乐;
        }
        if (video.contains(str)) {
            return FileType.视屏;
        }
        if (zip.contains(str)) {
            return FileType.zip;
        }
        if (pic.contains(str)) {
            return FileType.图片;
        }
        return FileType.图片;
    }

    enum FileType {
        未知, 文件夹, 音乐, 视屏, zip, 图片
    }
}
