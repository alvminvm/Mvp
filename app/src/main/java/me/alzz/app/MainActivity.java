package me.alzz.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import me.alzz.mvp.BaseMvpActivity;

public class MainActivity extends BaseMvpActivity implements IMainView {

    private TextView mMsgTv;

    // 只需要声明，不需要赋值
    MainPresenter mMainPresenter;

    // 或者使用接口，实现类可放至单独的 module 中，编译时引用
    // IMainPresenter mMainPresenter;

    // 如果自己赋值也是可以的
    // MainPresenter mMainPresenter = new MainPresenter();
    // IMainPresenter mMainPresenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMsgTv = (TextView) findViewById(R.id.msg_tv);

        if (mMainPresenter == null) {
            Toast.makeText(this, "presenter is null", Toast.LENGTH_LONG).show();
        } else {
            mMainPresenter.queryMsg();
        }
    }

    @Override
    public void showMsg(String msg) {
        mMsgTv.setText(msg);
    }
}
