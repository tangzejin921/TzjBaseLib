package com.tzj.baselib.utils;


import android.text.TextUtils;

import java.util.Iterator;

/**
 * 集合过滤搜素
 * 需要包 'com.belerweb:pinyin4j:2.5.1'
 */
public class UtilSearch {
    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 词组解析
     */
    public static String toPinYin(String str) {
        if (str == null || str.trim().length() == 0)
            return "";
        String py = "";
        String[] t;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c <= 128)
                py += c;
            else {
                try {
                    t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    return "";
                }
                if (t == null) {
                    py += c;
                } else if (t.length > 0) {
                    py += t[0];
                }
            }
        }
        return py.trim();
    }
    /**
     * 词组解析
     */
    public static String toPinYinAbb(String str) {
        if (str == null || str.trim().length() == 0)
            return "";
        String py = "";
        String[] t;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c <= 128)
                py += c;
            else {
                try {
                    t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    return "";
                }
                if (t == null) {
                    py += c;
                } else if (t.length > 0) {
                    py += t[0].charAt(0);
                }
            }
        }
        return py.trim();
    }


    /**
     * @param filterStr 汉字，拼音
     */
    public static <T extends Search> void search(Iterable<T> collection, String filterStr) {
        if (TextUtils.isEmpty(filterStr)) {
            return;
        }
        filterStr = filterStr.toLowerCase();
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            String text = next.text().toLowerCase();
            if (!text.contains(filterStr) &&
                    !toPinYin(text).toLowerCase().contains(filterStr) &&
                    !toPinYinAbb(text).toLowerCase().contains(filterStr)) {
                iterator.remove();
            }
        }
    }

    public interface Search {
        /**
         * 搜素的内容
         */
        String text();
    }
}
