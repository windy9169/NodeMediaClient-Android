package cn.nodemedia.nodemediaclient;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import cn.nodemedia.LivePlayer;
import cn.nodemedia.LivePlayer.LivePlayerDelegate;

public class LivePlayerDemoActivity extends Activity {
	// LinearLayout liner0;
	boolean isPlaying;
	int tsID;
	String outTsPath;
	SurfaceView sv;
	EditText logText;
	Boolean showLog, enableVideo;
	float srcWidth;
	float srcHeight;
	DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		dm = getResources().getDisplayMetrics();

		showLog = (Boolean) SharedPreUtil.get(this, "enablePlayLog", true);
		enableVideo = (Boolean) SharedPreUtil.get(this, "enableVideo", true);

		LivePlayer.init(this);
		LivePlayer.setDelegate(new LivePlayerDelegate() {
			@Override
			public void onEventCallback(int event, String msg) {
				Message message = new Message();
				Bundle b = new Bundle();
				b.putString("msg", msg);
				message.setData(b);
				message.what = event;
				handler.sendMessage(message);
			}
		});
		
		sv = (SurfaceView) findViewById(R.id.surfaceview1);
		
		logText = (EditText) findViewById(R.id.editText3);
		if (!showLog) {
			logText.setVisibility(View.GONE);
		}
		
		if(enableVideo) {
			LivePlayer.setUIVIew(sv);
		} else {
			LivePlayer.setUIVIew(null);
		}
		

		/**
		 * 设置本地播放缓冲时长 rtmp流本地缓冲队列时长，单位毫秒,默认1000ms
		 * 
		 */
		LivePlayer.setBufferTime(Integer.valueOf((String) SharedPreUtil.get(LivePlayerDemoActivity.this, "bufferTime", "1000")));


		String playUrl = (String) SharedPreUtil.get(LivePlayerDemoActivity.this, "playUrl", "rtmp://192.168.0.10/live/demo");
		SharedPreUtil.put(LivePlayerDemoActivity.this, "playUrl", playUrl); // 记住上次输入的测试地址，只在demo中用，非SDK方法

		/**
		 * 开始播放 三个参数，分别是： rtmpUrl 必填 ，pageUrl 选填 ，swfUrl 选填
		 */
		// LivePlayer.startPlay(playUrl);
		LivePlayer.startPlay(playUrl, "http://pageUrl.com", "htt://swfurl.com");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LivePlayer.stopPlay();
		
	}

	/**
	 *监听手机旋转，不销毁activity进行画面旋转，再缩放显示区域
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doVideoFix();
	}

	/**
	 * 视频画面高宽等比缩放，此SDK——demo 取屏幕高宽做最大高宽缩放
	 */
	private void doVideoFix() {
		float maxWidth = dm.widthPixels;
		float maxHeight = dm.heightPixels;
		float fixWidth;
		float fixHeight;
		if (srcWidth / srcHeight <= maxWidth / maxHeight) {
			fixWidth = srcWidth * maxHeight / srcHeight;
			fixHeight = maxHeight;
		} else {
			fixWidth = maxWidth;
			fixHeight = srcHeight * maxWidth / srcWidth;
		}
		ViewGroup.LayoutParams lp = sv.getLayoutParams();
		lp.width = (int)fixWidth;
		lp.height = (int)fixHeight;
		
		sv.setLayoutParams(lp);
	}



	private Handler handler = new Handler() {
		// 回调处理
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			StringBuffer sb = new StringBuffer();
			SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
			String sRecTime = sDateFormat.format(new java.util.Date());
			sb.append(sRecTime);
			sb.append(" - ");
			sb.append(msg.getData().getString("msg"));
			sb.append("\r\n");
			logText.append(sb);

			switch (msg.what) {
			case 1000:
				// Toast.makeText(LivePlayerDemoActivity.this, "正在连接视频",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1001:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频连接成功",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1002:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频连接失败",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1004:
//				Toast.makeText(LivePlayerDemoActivity.this, "视频播放结束", Toast.LENGTH_SHORT).show();
				break;
			case 1005:
				// Toast.makeText(LivePlayerDemoActivity.this, "网络异常,播放中断",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1006:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频数据未找到",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1007:
				// Toast.makeText(LivePlayerDemoActivity.this, "音频数据未找到",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1008:
				// Toast.makeText(LivePlayerDemoActivity.this, "无法打开视频解码器",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1009:
				// Toast.makeText(LivePlayerDemoActivity.this, "无法打开音频解码器",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1100:
				System.out.println("NetStream.Buffer.Empty");
				break;
			case 1101:
				System.out.println("NetStream.Buffer.Buffering");
				break;
			case 1102:
				System.out.println("NetStream.Buffer.Full");
				break;
			case 1103:
				System.out.println("Stream EOF");
				break;
			case 1104:
				/**
				 * 得到 解码后得到的视频高宽值,可用于重绘surfaceview的大小比例 格式为:{width}x{height}
				 * 本例使用LinearLayout内嵌SurfaceView
				 * LinearLayout的大小为最大高宽,SurfaceView在内部等比缩放,画面不失真
				 * 等比缩放使用在不确定视频源高宽比例的场景,用上层LinearLayout限定了最大高宽
				 */
				String[] info = msg.getData().getString("msg").split("x");
				srcWidth = Integer.valueOf(info[0]);
				srcHeight = Integer.valueOf(info[1]);
				doVideoFix();
				break;
			default:
				break;
			}
		}
	};

}
