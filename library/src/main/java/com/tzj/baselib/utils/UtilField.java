package com.tzj.baselib.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 */
public class UtilField {
    /**
     * 获取字段
     */
    public static Field getField(Class cls, String name) throws NoSuchFieldException {
        Field field = null;
        try {
            field = cls.getField(name);//类及其父类的public 字段.
        } catch (NoSuchFieldException e) {
            field  = getDeclaredField(cls,name);
        }
        field.setAccessible(true);
        return field;
    }

    /**
     * 类本身以及父类的所有字段.
     */
    private static Field getDeclaredField(Class cls, String name) throws NoSuchFieldException {
        for (Class<?> c = cls; c != null; c = c.getSuperclass()) {
            try {
                Field result = c.getDeclaredField(name);
                if (result !=null) {
                    return result;
                }
            } catch (NoSuchFieldException e) {
            }
        }
        throw new NoSuchFieldException();
    }

    /**
     * 获取方法
     */
    public static Method getMethed(Object obj, String methed, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = null;
        try {
            method = obj.getClass().getMethod(methed, parameterTypes);//类及其父类的public.
        } catch (NoSuchMethodException e) {
            method  = getDeclaredMethod(obj,methed, parameterTypes);
        }
        method.setAccessible(true);
        return method;

    }

    /**
     * 类本身以及父类的所有方法
     */
    private static Method getDeclaredMethod(Object obj, String methed, Class<?>... parameterTypes) throws NoSuchMethodException {
        for (Class<?> c = obj.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Method method = c.getDeclaredMethod(methed,parameterTypes);
                if (method !=null) {
                    return method;
                }
            } catch (NoSuchMethodException e) {
            }
        }
        throw new NoSuchMethodException();
    }

    /**
     * 代理,必须是接口，并且是实现接口的类不能是子类
     */
    public static Object getProxy(Object obj, InvocationHandler invocationHandler){
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), invocationHandler);
    }
}
