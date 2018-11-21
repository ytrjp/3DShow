package com.tencent.qcloud.ilivedemo.ScreenSharedDemo.viewInterface;

import java.util.LinkedList;

public interface IRoomView {

    void onCreateRoomSucc();

    void onCreateRoomFail(String module, int errCode, String errMsg);

    void onQuitRoomSucc();

    void onQuitRoomFail(String module, int errCode, String errMsg);

    void onException(int exceptionId, int errCode, String errMsg);

    void onRecordScreenSucc();

    void onRecordScreenFail(String module, int errCode, String errMsg);

    void onVedioRequestErr(int result, String errMsg);

    void onRoomDisconnect(int errCode, String errMsg);

    void onEndRecordScreenSucc();

    void onEndRecordScreenFail(String module, int errCode, String errMsg);

    void onJoinRoomSucc();

    void onJoinRoomFail(String module, int errCode, String errMsg);
}
