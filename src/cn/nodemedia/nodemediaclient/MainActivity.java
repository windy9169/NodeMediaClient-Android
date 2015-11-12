package cn.nodemedia.nodemediaclient;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	Button playerBtn, encoderBtn, chatBtn;
	EditText playUrl, pubUrl, bufferTime;
	CheckBox enablePlayCB, enableVideoCB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		playerBtn = (Button) findViewById(R.id.button1);
		encoderBtn = (Button) findViewById(R.id.button2);
		chatBtn = (Button) findViewById(R.id.button3);

		playUrl = (EditText) findViewById(R.id.editText_play_url);
		pubUrl = (EditText) findViewById(R.id.editText_pub_url);
		bufferTime = (EditText) findViewById(R.id.editText_buffersize);
		enablePlayCB = (CheckBox) findViewById(R.id.checkBox_play_log);
		enableVideoCB = (CheckBox) findViewById(R.id.CheckBox_video);

		playUrl.setText((String) SharedPreUtil.get(this, "playUrl", "rtmp://play.nodemedia.cn/NodeMedia/stream"));
		pubUrl.setText((String) SharedPreUtil.get(this, "pubUrl", "rtmp://pub.nodemedia.cn/NodeMedia/stream_" + Math.round((Math.random() * 1000 + 1000))));
		bufferTime.setText((String) SharedPreUtil.get(this, "bufferTime", "1000"));
		enablePlayCB.setChecked((Boolean) SharedPreUtil.get(this, "enablePlayLog", true));
		enableVideoCB.setChecked((Boolean) SharedPreUtil.get(this, "enableVideo", true));

		playerBtn.setOnClickListener(this);
		encoderBtn.setOnClickListener(this);
		chatBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 记住上次播放发布配置的信息，只在demo中使用，非SDK方法
		SharedPreUtil.put(MainActivity.this, "pubUrl", pubUrl.getText().toString());
		SharedPreUtil.put(MainActivity.this, "playUrl", playUrl.getText().toString());
		SharedPreUtil.put(MainActivity.this, "bufferTime", bufferTime.getText().toString());
		SharedPreUtil.put(MainActivity.this, "enablePlayLog", enablePlayCB.isChecked());
		SharedPreUtil.put(MainActivity.this, "enableVideo", enableVideoCB.isChecked());

		switch (v.getId()) {
		case R.id.button1:
			MainActivity.this.startActivity(new Intent(MainActivity.this, LivePlayerDemoActivity.class));
			break;
		case R.id.button2:
			MainActivity.this.startActivity(new Intent(MainActivity.this, LivePublisherDemoActivity.class));
			break;
		case R.id.button3:
			MainActivity.this.startActivity(new Intent(MainActivity.this, LiveChatActivity.class));
			break;
		}
	}

}
