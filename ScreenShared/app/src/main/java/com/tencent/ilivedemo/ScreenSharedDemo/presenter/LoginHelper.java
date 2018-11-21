package com.tencent.qcloud.ilivedemo.ScreenSharedDemo.presenter;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.viewInterface.ILoginView;

public class LoginHelper{

    private ILoginView loginView;

    public LoginHelper(ILoginView view){
        loginView = view;
    }

    public void loginSDK(String id,String sig){
        ILiveLoginManager.getInstance().iLiveLogin(id, sig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                loginView.onLoginSuccess();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                loginView.onLoginFailed(module,errCode, errMsg);
            }
        });
    }
}
