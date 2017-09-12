package me.alzz.mvp;

import android.content.Context;

/**
 * mvp中的视图。一般由 {@link android.app.Activity} 或 {@link android.app.Fragment} 实现
 * Created by jeremyhe on 2017/9/12.
 */

public interface IView {
    /**
     * 返回上下文
     */
    Context getContext();
}
