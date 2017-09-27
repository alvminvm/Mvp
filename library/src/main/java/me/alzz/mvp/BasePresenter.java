package me.alzz.mvp;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Presenter 基类
 * Created by jeremyhe on 2017/9/12.
 */

public class BasePresenter<T extends IView> implements IPresenter {

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

    @Nullable
    public Context getContext() {
        if (mView != null) {
            return mView.getContext();
        } else {
            return null;
        }
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}
