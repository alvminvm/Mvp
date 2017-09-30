package me.alzz.app;

import me.alzz.mvp.IPresenter;

/**
 * main presenter接口
 * Created by Jeremy He on 2017/9/30.
 */

public interface IMainPresenter extends IPresenter {
    /**
     * 从服务器读取消息
     */
    void queryMsg();
}
