package me.alzz.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.CallSuper;

/**
 * mvp简单的Fragment实现类
 * Created by jeremyhe on 2017/9/12.
 */

public class BaseMvpFragment extends Fragment implements IView {

    @CallSuper
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Presenter.init(activity);
        Presenter.bind(this);
    }
}
