package me.alzz.mvp;

import android.support.annotation.NonNull;

/**
 * mvp 中的 Presenter
 * Created by jeremyhe on 2017/9/12.
 */

public interface IPresenter {
    /**
     * 关联视图
     */
    void onAttach(@NonNull IView view);

    /**
     * 分离视图。视图即将销毁时调用
     */
    void onDetach();
}
