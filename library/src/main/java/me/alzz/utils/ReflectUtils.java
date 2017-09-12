package me.alzz.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Java 反射工具类
 * Created by jeremyhe on 2017/9/12.
 */

public class ReflectUtils {

    private static final String TAG = "ReflectUtils";

    /**
     * 获取所有类
     * @param context 上下文
     * @param pkgName 指定要获取类所在的包名
     */
    public static List<Class> loadClassList(@NonNull Context context, String pkgName) {
        return loadClassList(context, pkgName, null);
    }

    /**
     * 获取所有类
     * @param context 上下文
     * @param baseClass 指定要获取类所实现的基类
     */
    public static List<Class> loadClassList(@NonNull Context context, @Nullable Class baseClass) {
        return loadClassList(context, "", baseClass);
    }

    /**
     * 获取所有类
     * @param context 上下文
     * @param pkgName 指定要获取类所在的包名
     * @param baseClass 指定要获取类所实现的基类
     */
    public static List<Class> loadClassList(@NonNull Context context, String pkgName, @Nullable Class baseClass) {
        List<Class> classList = new ArrayList<>();

        Context appContext = context.getApplicationContext();
        ClassLoader classLoader = appContext.getClassLoader();

        String entry = "";
        try {
            Field pathListField = findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);

            Field dexElementsField = findField(dexPathList, "dexElements");
            Object[] dexElements = (Object[]) dexElementsField.get(dexPathList);
            for (Object dexElement : dexElements) {
                Field dexFileField = findField(dexElement, "dexFile");
                DexFile dexFile = (DexFile) dexFileField.get(dexElement);

                Enumeration<String> entries = dexFile.entries();
                while (entries.hasMoreElements()) {
                    entry = entries.nextElement();

                    if (entry.contains(pkgName)) {
                        Class<?> clazz = dexFile.loadClass(entry, classLoader);

                        if (baseClass != null) {
                            if (baseClass.isAssignableFrom(clazz)) {
                                classList.add(clazz);
                            }
                        } else {
                            classList.add(clazz);
                        }
                    }
                }
            }
        } catch (Exception | Error e) {
            Log.e(TAG, "reflect fail. class = " + entry + " Exception: " + e.getMessage());
        }

        return classList;
    }

    /**
     * 查找实例某个变量
     */
    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        Class clazz = instance.getClass();

        while(clazz != null) {
            try {
                Field e = clazz.getDeclaredField(name);
                if(!e.isAccessible()) {
                    e.setAccessible(true);
                }

                return e;
            } catch (NoSuchFieldException var4) {
                clazz = clazz.getSuperclass();
            }
        }

        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }
}
