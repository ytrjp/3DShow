package com.tencent.qcloud.ilivedemo.ScreenSharedDemo;

import com.tencent.ilivesdk.core.ILiveLoginManager;

import java.util.LinkedList;

public class StatusObservable implements ILiveLoginManager.TILVBStatusListener {

    private LinkedList<ILiveLoginManager.TILVBStatusListener> listeners = new LinkedList<>();
    private static StatusObservable instance;
    public static StatusObservable getInstance(){
        if (null==instance){
            synchronized (StatusObservable.class){
                if (null==instance){
                    instance=new StatusObservable();
                }
            }
        }
        return instance;
    }

    public void addListener(ILiveLoginManager.TILVBStatusListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(ILiveLoginManager.TILVBStatusListener listener){
        listeners.remove(listener);
    }

    // 获取观察者数量
    public int getObserverCount(){
        return listeners.size();
    }

    @Override
    public void onForceOffline(int error, String message) {
        LinkedList<ILiveLoginManager.TILVBStatusListener> observers = new LinkedList<>(listeners);
        for (ILiveLoginManager.TILVBStatusListener observer:observers) {
            observer.onForceOffline(error,message);
        }
    }
}
