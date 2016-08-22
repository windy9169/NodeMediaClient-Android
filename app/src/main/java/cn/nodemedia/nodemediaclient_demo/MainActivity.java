package cn.nodemedia.nodemediaclient_demo;

import android.content.Intent;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button playerBtn, encoderBtn,nodePlayerBtn;
    EditText playUrl, pubUrl, bufferTime,maxBufferTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Android 6.0及以上并且targetSdkVersion 23及以上,摄像头和麦克风权限需要用代码请求
        //这里使用第三方的权限管理库来请求
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this,
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {

                    }
                });
        playerBtn = (Button) findViewById(R.id.button1);
        encoderBtn = (Button) findViewById(R.id.button2);
        nodePlayerBtn = (Button)findViewById(R.id.button3);
        playUrl = (EditText) findViewById(R.id.editTextPlay);
        pubUrl = (EditText) findViewById(R.id.editTextPublish);
        bufferTime = (EditText) findViewById(R.id.editText_bufferTime);
        maxBufferTime = (EditText) findViewById(R.id.editText_maxBufferTime);

        playUrl.setText(SharedPreUtil.getString(this, "playUrl", "rtmp://play.nodemedia.cn/NodeMedia/stream_1000"));
        pubUrl.setText(SharedPreUtil.getString(this, "pubUrl","rtmp://pub.nodemedia.cn/NodeMedia/stream_" + Math.round((Math.random() * 1000 + 1000))));
        bufferTime.setText(SharedPreUtil.getString(this, "bufferTime", "500"));
        maxBufferTime.setText(SharedPreUtil.getString(this, "maxBufferTime", "1000"));

        playerBtn.setOnClickListener(this);
        encoderBtn.setOnClickListener(this);
        nodePlayerBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                // 记住上次播放配置的信息，只在demo中使用，非SDK方法
                SharedPreUtil.put(MainActivity.this, "playUrl", playUrl.getText().toString());
                SharedPreUtil.put(MainActivity.this, "bufferTime", bufferTime.getText().toString());
                SharedPreUtil.put(MainActivity.this, "maxBufferTime", maxBufferTime.getText().toString());
                MainActivity.this.startActivity(new Intent(MainActivity.this, LivePlayerDemoActivity.class));
                break;
            case R.id.button2:
                // 记住上次输入的发布地址，只在demo中使用，非SDK方法
                SharedPreUtil.put(MainActivity.this, "pubUrl", pubUrl.getText().toString());
                MainActivity.this.startActivity(new Intent(MainActivity.this, LivePublisherDemoActivity.class));
                break;
            case R.id.button3:
                MainActivity.this.startActivity(new Intent(MainActivity.this, NodePlayerDemoActivity.class));
                break;
        }
    }
}
