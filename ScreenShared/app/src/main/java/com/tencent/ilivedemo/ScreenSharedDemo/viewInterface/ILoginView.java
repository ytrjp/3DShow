package com.tencent.qcloud.ilivedemo.ScreenSharedDemo.viewInterface;

public interface ILoginView {
    void onLoginSuccess();
    void onLoginFailed(String module, int errCode, String errMsg);
}
