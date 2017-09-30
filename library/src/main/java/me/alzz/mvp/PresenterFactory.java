package me.alzz.mvp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.dx.stock.ProxyBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.alzz.utils.ReflectUtils;

/**
 * Presenter 工厂类，用于实例化 Presenter
 * Created by jeremyhe on 2017/9/12.
 */

class PresenterFactory {

    private static final String TAG = "PresenterFactory";

    private List<Class> mPresenterClassList = new ArrayList<>();

    private InvocationHandler mHandler = new InvocationHandler() {

        private final Method[] ignoreMethods = BasePresenter.class.getDeclaredMethods();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                // 忽略方法，则直接调用原来的方法
                for (Method m : ignoreMethods) {
                    if (m.equals(method)) {
                        return ProxyBuilder.callSuper(proxy, method, args);
                    }
                }

                if (proxy instanceof BasePresenter) {
                    final IView v = ((BasePresenter) proxy).mView;
                    if (v == null) {
                        Log.w(TAG, "view didn't attach or has detached");
                        return null;
                    }
                }

                return ProxyBuilder.callSuper(proxy, method, args);
            } catch (NullPointerException e) {
                // 忽略因为耗时操作之后对已回收 view 进行调用而产生的空指针异常
                if (proxy instanceof BasePresenter) {
                    if (((BasePresenter) proxy).mView == null) {
                        Log.w(TAG, "please stop background task when view has detached. ignore this exception");
                        return null;
                    }
                }

                // 其他原因的空指针异常，正常抛出
                throw e;
            }
        }
    };

    PresenterFactory(Context context) {
        mPresenterClassList = ReflectUtils.loadClassList(context, IPresenter.class);
    }

    /**
     * 通过反射及classLoader查找实现类并实例化返回结果
     */
    @Nullable
    IPresenter newPresenter(Context context, Class<?> clazz) {
        if (context == null) {
            return null;
        }

        for (Class<?> presenterClass : mPresenterClassList) {
            if (clazz.isAssignableFrom(presenterClass)) {
                IPresenter p = null;
                try {
                    p = (IPresenter) ProxyBuilder.forClass(presenterClass)
                            .dexCache(context.getApplicationContext().getDir("dx", Context.MODE_PRIVATE))
                            .handler(mHandler)
                            .build();

                } catch (Exception e) {
                    Log.e(TAG, "presenter proxy fail. exception: " + e.toString());
                }

                if (p == null) {
                    try {
                        return (IPresenter) presenterClass.getConstructors()[0].newInstance();
                    } catch (Exception e) {
                        Log.e(TAG, "presenter create fail. exception: " + e.toString());
                    }
                } else {
                    return p;
                }
            }
        }

        Log.e(TAG, "presenter reflect fail. ");
        return null;
    }
}
