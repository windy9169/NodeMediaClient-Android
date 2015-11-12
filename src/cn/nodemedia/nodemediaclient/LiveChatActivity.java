package cn.nodemedia.nodemediaclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.nodemedia.LivePlayer;
import cn.nodemedia.LivePublisher;

public class LiveChatActivity extends Activity {
	SurfaceView svPlay, svPub;
	Button btnPlay,btnPub;
	boolean isPlay = false;
	boolean isPub = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		svPlay = (SurfaceView) findViewById(R.id.surfaceView_chat_play);
		svPub = (SurfaceView) findViewById(R.id.surfaceView_chat_pub);
		svPub.setZOrderMediaOverlay(true);
		final String playUrl = (String) SharedPreUtil.get(LiveChatActivity.this, "playUrl", "rtmp://192.168.0.10/live/demo");
		final String pubUrl = (String) SharedPreUtil.get(LiveChatActivity.this, "pubUrl", "rtmp://192.168.0.10/live/demo");
		
		btnPlay = (Button)findViewById(R.id.button_chat_play);
		btnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPlay) {
					LivePlayer.stopPlay();
					btnPlay.setText("播放");
				}else {
					LivePlayer.startPlay(playUrl);
					btnPlay.setText("停止");
				}
				isPlay = !isPlay;
			}
		});
		
		btnPub = (Button)findViewById(R.id.button_chat_pub);
		btnPub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPub) {
					LivePublisher.stopPublish();
					btnPub.setText("上麦");
				}else {
					LivePublisher.startPublish(pubUrl);
					btnPub.setText("下麦");
				}
				isPub = !isPub;
			}
		});
		
		LivePlayer.init(this);
		LivePublisher.init(this);

		LivePlayer.setUIVIew(svPlay);
		LivePlayer.setBufferTime(Integer.valueOf((String) SharedPreUtil.get(LiveChatActivity.this, "bufferTime", "1000")));

		LivePublisher.setAudioParam(32 * 1000, LivePublisher.AAC_PROFILE_HE);
		LivePublisher.setVideoParam(640, 360, 15, 400 * 1000, LivePublisher.AVC_PROFILE_MAIN);
		LivePublisher.setDenoiseEnable(true);
		LivePublisher.startPreview(svPub, getWindowManager().getDefaultDisplay().getRotation(), LivePublisher.CAMERA_BACK);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LivePlayer.stopPlay();
		LivePublisher.stopPreview();
		LivePublisher.stopPublish();
	}
}
