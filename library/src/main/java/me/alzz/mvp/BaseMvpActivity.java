package me.alzz.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * mvp简单的Activity实现类
 * Created by jeremyhe on 2017/9/12.
 */

public class BaseMvpActivity extends AppCompatActivity implements IView {

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Presenter.init(this);
        Presenter.bind(this);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
