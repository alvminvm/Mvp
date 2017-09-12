package me.alzz.mvp;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Presenter 基类
 * Created by jeremyhe on 2017/9/12.
 */

public class BasePresenter<T extends IView> implements IPresenter {

    @Nullable
    protected T mView;

    @CallSuper
    @Override
    public void onAttach(@NonNull IView view) {
        try {
            mView = (T) view;
        } catch (ClassCastException e) {
            mView = null;
        }
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}
