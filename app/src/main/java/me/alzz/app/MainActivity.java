package me.alzz.app;

import android.os.Bundle;
import android.widget.TextView;

import me.alzz.mvp.BaseMvpActivity;

public class MainActivity extends BaseMvpActivity implements IMainView {

    private TextView mMsgTv;

    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMsgTv = (TextView) findViewById(R.id.msg_tv);

        mMainPresenter.queryMsg();
    }

    @Override
    public void showMsg(String msg) {
        mMsgTv.setText(msg);
    }
}
