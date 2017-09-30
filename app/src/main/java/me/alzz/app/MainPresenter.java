package me.alzz.app;

import me.alzz.mvp.BasePresenter;

/**
 * Created by jeremyhe on 2017/9/12.
 */

public class MainPresenter extends BasePresenter<IMainView> implements IMainPresenter {

    @Override
    public void queryMsg() {
        // may be query from db or network
        String msg = "msg from query";
        mView.showMsg(msg);
    }
}
