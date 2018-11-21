package com.tencent.qcloud.ilivedemo.ScreenSharedDemo.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.StatusObservable;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.viewInterface.ILoginView;
import com.tencent.qcloud.ilivedemo.demopreview.R;
import com.tencent.qcloud.ilivedemo.ScreenSharedDemo.presenter.LoginHelper;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener,ILoginView {

    private static final String USERSIG1 = "eJxlz11PgzAUgOF7fgXpLUZbWAuY7AKWGZmrG84tetXw0WFlgw66wTT*dyMusYnn9nlzTs6nYZomeJ6vrpMsq4*VYuosOTBvTQDB1R9KKXKWKOY0*T-kvRQNZ8lW8WZAhDG2IdQbkfNKia24FD4mPnY1b-OSDUd*F4wgRAR6EOmJKAak03gS3e1WQT871wGW7obPju8T*rAIYUh2ikpvLdK469LH*xfrNQ1EEBcWnUrLpd1yuY9CJ82iNn5aL0j58YYVD24O8470hw0ti-FYO6nEnl8*GtnI823f0fTEm1bU1RDYEGFkO-BngPFlfAOOE1xd";
    private static final String USERSIG2 = "eJxlz0FrgzAUwPG7nyLk6tgSTUQHO5TVrrKWsrWl6kVCE0smNVZjNjv23be5QgN719*f93ifDgAAbhbrW7bfq77WhR4aAcE9gAjeXLFpJC*YLvyW-0Px0chWFKzUoh0RU0o9hOxGclFrWcpLEdEgoqHlHa*K8cjfAoIQDlCIsJ3Iw4jLePuYvEzV7DyLY1pNUu6e3LkxbrLmp*17yerKKH4Ui7vV85Dh*TBJDjz9qfC5d3M37V67Jsue0Iq8Lae7XBHRGyaTfBMwg0n8YJ3U8iguHxEPh5HvUUuNaDup6jHwEKbY89HvQOfL*QbEL1yv";

    //------------------------------------test---------------------------------------------------------
    /**
     *
     * 此处请自行参考文档注册用户account并生成userSig进行测试
     * 文档地址：https://gitee.com/vqcloud/doc_demo/blob/master/%E5%BC%80%E5%8F%91%E5%89%8D%E5%BF%85%E7%9C%8B/%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5.md#%E7%94%A8%E6%88%B7%E7%AD%BE%E5%90%8D
     *
     */
    private final String userSig1 = USERSIG1;
    private final String identifier1 = "956957";
    private final String userSig2 = USERSIG2;
    private final String identifier2 = "956958";
    //------------------------------------test---------------------------------------------------------

    private boolean isHost = false;
    private EditText roomIdEt;
    private LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button host = findViewById(R.id.btn_host);
        Button guest = findViewById(R.id.btn_guest);
        roomIdEt=findViewById(R.id.et_room_id);
        host.setOnClickListener(this);
        guest.setOnClickListener(this);

        checkPermission();

        loginHelper = new LoginHelper(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_host:
                isHost=true;
                loginHelper.loginSDK(identifier1,userSig1);
                break;
            case R.id.btn_guest:
                isHost=false;
                loginHelper.loginSDK(identifier2,userSig2);
                break;
        }

    }

    @Override
    public void onLoginSuccess() {
        ILiveLoginManager.getInstance().setUserStatusListener(StatusObservable.getInstance());
        Toast.makeText(this,"login success",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this,RoomActivity.class);
        intent.putExtra("isHost",isHost);
        intent.putExtra("roomId",Integer.parseInt(roomIdEt.getText().toString()));
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed(String module, int errCode, String errMsg) {
        Toast.makeText(this,errCode+":"+errMsg,Toast.LENGTH_SHORT).show();
    }

    protected void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        (String[]) permissions.toArray(new String[0]),
                        100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults.length>0){
                    String needPermission = "";
                    for (int i=0;i<grantResults.length;i++) {
                        if (grantResults[i] < 0) {
                            needPermission += permissions[i] + " ";
                        }
                    }
                    if (!"".equals(needPermission)){
                        Toast.makeText(LoginActivity.this,"需要权限:"+needPermission,Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LoginActivity.this,"没有获取权限",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
