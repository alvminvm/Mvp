package me.alzz.mvp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
                IPresenter presenter = null;
                try {
                    presenter = (IPresenter) implClass.getConstructors()[0].newInstance();
                    SafePresenter safePresenter = new SafePresenter(presenter);
                    Class cls = presenter.getClass();
                    Class[] interfaces = cls.getInterfaces();
                    if (interfaces.length == 0) {
                        return presenter;
                    } else {
                        return (IPresenter) Proxy.newProxyInstance(cls.getClassLoader(), interfaces, safePresenter);
                    }
                } catch (Exception e) {
                    if (presenter != null) {
                        Log.w(TAG, "wrap presenter fail. return actual presenter instead");
                        return presenter;
                    } else {
                        Log.e(TAG, "presenter reflect fail. class = " + clazz.getSimpleName() + "  exception = " + e.getMessage());
                    }
                }
            }
        }

        return null;
    }

    private class SafePresenter implements InvocationHandler {

        private IPresenter p;

        SafePresenter(IPresenter presenter) {
            this.p = presenter;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                if (p instanceof BasePresenter) {
                    if (((BasePresenter) p).mView == null) {
                        Log.d(TAG, "view didn't attach or has detached");
                        return null;
                    }
                }

                return method.invoke(p, args);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof NullPointerException) {
                    Log.w(TAG, "something is null. it may be the view which has detached");
                } else {
                    Log.e(TAG, "Exception: " + e.getTargetException().toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.toString());
            }

            return null;
        }
    }
}
