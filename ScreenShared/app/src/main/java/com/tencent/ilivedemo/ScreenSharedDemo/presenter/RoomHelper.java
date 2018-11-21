package com.tencent.qcloud.ilivedemo.ScreenSharedDemo.presenter;

import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveMemStatusLisenter;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.adapter.CommonConstants;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.viewInterface.IRoomView;

public class RoomHelper implements ILiveRoomOption.onExceptionListener,ILiveRoomOption.onRoomDisconnectListener,ILiveRoomOption.onRequestViewListener, ILiveMemStatusLisenter {

    private AVRootView rootView;
    private IRoomView roomView;
    //绑定View
    public RoomHelper(IRoomView view){
        roomView = view;
    }
    //初始化AVRootView
    public void setRootView(AVRootView view){
        rootView = view;
        ILiveRoomManager.getInstance().initAvRootView(view);
    }

    //创建房间
    public int createRoom(int roomid){
        ILiveRoomOption option = new ILiveRoomOption()
                .imsupport(false)
                .autoRender(true)
                .exceptionListener(this)
                .controlRole("Host")
                .roomDisconnectListener(this)
                .autoCamera(true)
                .autoMic(true);
        return ILiveRoomManager.getInstance().createRoom(roomid, option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onCreateRoomSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onCreateRoomFail(module,errCode,errMsg);
            }
        });
    }

    public int joinRoom(int roomid){
        ILiveRoomOption option = new ILiveRoomOption()
                .imsupport(false)
                .autoRender(false)
                .controlRole("Guest")
                .setRoomMemberStatusLisenter(this)
                .exceptionListener(this)
                .setRequestViewLisenter(this)
                .roomDisconnectListener(this)
                .autoCamera(false)
                .autoMic(false);
        return ILiveRoomManager.getInstance().createRoom(roomid, option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

                roomView.onJoinRoomSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onJoinRoomFail(module,errCode,errMsg);
            }
        });
    }

    /**
     * 开始录制屏幕
     * @param mode 质量模式
     * @param vertical 是否竖屏
     *      超清（1280*720）
     *      public static int Const_Screen_Super_HD = AVVideoCtrl.SCREEN_SUPER_DEFINITION;
     *      高清(960*540)
     *      public static int Const_Screen_HD = AVVideoCtrl.SCREEN_HIGH_DEFINITION;
     *      标清(864*480)
     *      public static int Const_Screen_SD = AVVideoCtrl.SCREEN_STANDARD_DEFINITION;
     *
     */
    public void startRecordScreen(int mode, boolean vertical){
        ILiveRoomManager.getInstance().enableScreen(mode, vertical, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onRecordScreenSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onRecordScreenFail(module,errCode,errMsg);
            }
        });
    }

    /**
     * 停止录屏
     */
    public void endRecordScreen(){
        ILiveRoomManager.getInstance().disableScreen(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onEndRecordScreenSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onEndRecordScreenFail(module,errCode,errMsg);
            }
        });
    }

    //退出房间
    public void quitRoom(){
        ILiveRoomManager.getInstance().quitRoom(true, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                roomView.onQuitRoomSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                roomView.onQuitRoomFail(module, errCode, errMsg);
            }
        });
    }

    @Override
    public void onException(int exceptionId, int errCode, String errMsg) {
        roomView.onException(exceptionId,errCode,errMsg);
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        roomView.onRoomDisconnect(errCode,errMsg);
    }

    @Override
    public void onComplete(String[] identifierList, AVView[] viewList, int count, int result, String errMsg) {
        if (ILiveConstants.NO_ERR == result){
            for (int i=0; i<identifierList.length; i++){
                rootView.renderVideoView(true, identifierList[i], viewList[i].videoSrcType, true);
            }
        }else {
            roomView.onVedioRequestErr(result,errMsg);
        }
    }

    @Override
    public boolean onEndpointsUpdateInfo(int eventid, String[] updateList) {

        switch (eventid){
            case ILiveConstants.TYPE_MEMBER_CHANGE_HAS_CAMERA_VIDEO:
                for (String identifier : updateList){
                    // 请求视频画面
                    ILiveSDK.getInstance().getContextEngine().requestUserVideoData(identifier,
                            CommonConstants.Const_VideoType_Camera);
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_HAS_SCREEN_VIDEO:
                //将屏幕分享视频流设置在主屏幕
                rootView.swapVideoView(1,0);
                for (String identifier : updateList){
                    // 请求视频画面
                    ILiveSDK.getInstance().getContextEngine().requestUserVideoData(identifier,
                            CommonConstants.Const_VideoType_Screen);
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_NO_CAMERA_VIDEO:
                for (String identifier:updateList){
                    ILiveSDK.getInstance().getContextEngine().removeUserVideoData(identifier,CommonConstants.Const_VideoType_Camera);
                    rootView.closeUserView(identifier,CommonConstants.Const_VideoType_Camera,true);
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_NO_SCREEN_VIDEO:
                for (String identifier : updateList){
                    ILiveSDK.getInstance().getContextEngine().removeUserVideoData(identifier,
                            CommonConstants.Const_VideoType_Screen);
                    rootView.closeUserView(identifier,CommonConstants.Const_VideoType_Screen,true);
                }
                break;
            default:
                return false;
        }
        return false;
    }
}
