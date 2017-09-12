package me.alzz.mvp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.alzz.utils.ReflectUtils;

/**
 * Presenter 工厂类，用于实例化 Presenter
 * Created by jeremyhe on 2017/9/12.
 */

class PresenterFactory {

    private static final String TAG = "PresenterFactory";

    private List<Class> mImplClassList = new ArrayList<>();

    PresenterFactory(Context context) {
        mImplClassList = ReflectUtils.loadClassList(context, IPresenter.class);
    }

    /**
     * 通过反射及classLoader查找实现类并实例化返回结果
     */
    @Nullable
    IPresenter newPresenter(Class<?> clazz) {
        for (Class<?> implClass : mImplClassList) {
            if (clazz.isAssignableFrom(implClass)) {
                try {
                    return (IPresenter) implClass.getConstructors()[0].newInstance();
                } catch (Exception e) {
                    Log.e(TAG, "presenter reflect fail. class = " + clazz.getSimpleName() + "  exception = " + e.getMessage());
                }
            }
        }

        return null;
    }

}
