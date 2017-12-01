# Mvp

[![Release](https://jitpack.io/v/me.alzz/mvp.svg)](https://jitpack.io/#me.alzz/mvp)

用于快速引入 MVP 框架至 Android 应用开发中  

## 添加依赖  
1. 添加 jitpack 仓库至根目录下的`build.gradle`文件中
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. 添加编译依赖
```gradle
dependencies {
    compile 'me.alzz:mvp:1.0.3'
}
```

## 快速使用
1. 定义 view 接口
```java
public interface IMainView extends IView {
    void showMsg(String msg);
}
```

2. 定义 presenter 接口（可选）
```java
public interface IMainPresenter extends IPresenter {
    /**
     * 从服务器读取消息
     */
    void queryMsg();
}
```

3. 继承 `BasePresenter` 实现 presenter
```java
public class MainPresenter extends BasePresenter<IMainView> implements IMainPresenter {

    public void queryMsg() {
        // may be query from db or network
        String msg = "msg from query";
        // ... 

        mView.showMsg(msg);
    }
}
```

4. 继承`BaseMvpActivity` 并实现 view 接口。可考虑由基类继承 `BaseMvpActivity`
```java
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

        mMainPresenter.queryMsg();
    }

    @Override
    public void showMsg(String msg) {
        mMsgTv.setText(msg);
    }
}
```
## Proguard
```
# 保留presenter默认构造函数
-keepclasseswithmembers class * extends me.alzz.mvp.BasePresenter {
    public <init>();
}
```

## 致谢
- [dexmaker](https://github.com/linkedin/dexmaker):A utility for doing compile or runtime code generation targeting Android's Dalvik VM

