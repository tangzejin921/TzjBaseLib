//package com.tzj.baselib.utils;
//
//
//import android.text.TextUtils;
//
//import com.focustech.mm.common.view.sortlistview.CharacterParser;
//
//import java.util.Iterator;
//
///**
// * 集合过滤搜素
// */
//public class UtilSearch {
//
//    /**
//     * @param filterStr 汉字，拼音
//     */
//    public static <T extends Search> void search(Iterable<T> collection,String filterStr){
//        if (TextUtils.isEmpty(filterStr)){
//            return;
//        }
//        filterStr = filterStr.toLowerCase();
//        Iterator<T> iterator = collection.iterator();
//        CharacterParser characterParser = CharacterParser.getInstance();
//        while (iterator.hasNext()){
//            T next = iterator.next();
//            String text = next.text().toLowerCase();
//            if (!text.contains(filterStr)&&
//                    !characterParser.getSelling(text).toLowerCase().contains(filterStr)&&
//                    !characterParser.getSellingAbb(text).toLowerCase().contains(filterStr)){
//                iterator.remove();
//            }
//        }
//    }
//
//    public interface Search{
//        /**
//         * 搜素的内容
//         */
//        String text();
//    }
//}
