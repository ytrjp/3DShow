package com.tencent.qcloud.ilivedemo.ScreenSharedDemo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.ilivesdk.adapter.CommonConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.StatusObservable;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.presenter.LoginHelper;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.presenter.RoomHelper;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.viewInterface.IRoomView;
import com.tencent.qcloud.ilivedemo.demopreview.R;

public class RoomActivity extends Activity implements IRoomView,ILiveLoginManager.TILVBStatusListener{

    private Button recordBtn;
    private AVRootView rootView;
    private RoomHelper roomHelper;

    private boolean recordSwitch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        boolean isHost = getIntent().getBooleanExtra("isHost",false);
        int roomId = getIntent().getIntExtra("roomId",-1);

        StatusObservable.getInstance().addListener(this);

        roomHelper=new RoomHelper(this);
        rootView=findViewById(R.id.av_root_view);
        roomHelper.setRootView(rootView);
        recordBtn=findViewById(R.id.record);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recordSwitch){
                    recordSwitch=true;
                    roomHelper.startRecordScreen(CommonConstants.Const_Screen_Super_HD,true);
                    recordBtn.setText("END");
                }else {
                    recordSwitch=false;
                    roomHelper.endRecordScreen();
                    recordBtn.setText("RECORD");
                }
            }
        });
        if (roomId<0){
            Toast.makeText(this,"roomId error",Toast.LENGTH_SHORT).show();
            return;
        }
        if (isHost){
            roomHelper.createRoom(roomId);
        }else {
            recordBtn.setVisibility(View.GONE);
            roomHelper.joinRoom(roomId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILiveRoomManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ILiveRoomManager.getInstance().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatusObservable.getInstance().removeListener(this);
        ILiveRoomManager.getInstance().onDestory();
    }

    @Override
    public void onCreateRoomSucc() {
        Toast.makeText(this,"create room",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateRoomFail(String module, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuitRoomSucc() {
        Toast.makeText(this,"quit room",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuitRoomFail(String module, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onException(int exceptionId, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordScreenSucc() {
        Toast.makeText(this,"record screen",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordScreenFail(String module, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVedioRequestErr(int result, String errMsg) {
        Toast.makeText(this,"请求失败:"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEndRecordScreenSucc() {
        Toast.makeText(this,"end record",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEndRecordScreenFail(String module, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJoinRoomSucc() {
        Toast.makeText(this,"join room",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJoinRoomFail(String module, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForceOffline(int error, String message) {
        Toast.makeText(this,"账号异地登录",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
