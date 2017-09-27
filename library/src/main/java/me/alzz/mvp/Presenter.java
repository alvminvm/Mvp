package me.alzz.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Presenter 绑定类
 * Created by jeremyhe on 2017/9/12.
 */

public class Presenter {

    @Nullable
    private static PresenterFactory sFactory;

    private Presenter() {
        // do nothing
    }

    /**
     * 初始化
     */
    public static void init(@NonNull Context context) {
        if (sFactory == null) {
            sFactory = new PresenterFactory(context);
        }
    }

    /**
     * 绑定 Presenter
     */
    static void bind(@NonNull IView view) {
        if (sFactory == null) {
            throw new RuntimeException("must invoke Presenter.init() first!");
        }

        Class<?> viewClass = view.getClass();

        do {
            Field[] fields = viewClass.getDeclaredFields();
            for (Field f : fields) {
                // 如果是 IPresenter 成员变量，则自动绑定
                Class<?> clazz = f.getType();
                if (IPresenter.class.isAssignableFrom(clazz)) {
                    IPresenter presenter = sFactory.newPresenter(view.getContext(), clazz);
                    if (presenter == null) {
                        continue;
                    }

                    presenter.onAttach(view);

                    // 对成员变量赋值
                    try {
                        f.setAccessible(true);
                        f.set(view, presenter);
                    } catch (IllegalAccessException e) {
                        Log.e("Presenter", "can't bind presenter: " + f.getName());
                    }
                }
            }

            viewClass = viewClass.getSuperclass();
        } while (IView.class.isAssignableFrom(viewClass));
    }
}
