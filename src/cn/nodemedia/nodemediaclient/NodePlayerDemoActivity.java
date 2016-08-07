package cn.nodemedia.nodemediaclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;

public class NodePlayerDemoActivity extends Activity implements NodePlayerDelegate {
	private NodePlayer npB;
	private NodePlayer npS;
	private NodePlayer npP;
	private SurfaceView svB;
	private SurfaceView svS;
	private SurfaceView svP;
	private ToggleButton tbB, tbS, tbP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nodeplayer);
		svB = (SurfaceView) findViewById(R.id.surfaceView_b);
		svS = (SurfaceView) findViewById(R.id.surfaceView_s);
		svP = (SurfaceView) findViewById(R.id.surfaceView_p);

		tbB = (ToggleButton) findViewById(R.id.toggleButton1);
		tbS = (ToggleButton) findViewById(R.id.toggleButton2);
		tbP = (ToggleButton) findViewById(R.id.toggleButton3);

		//这两个surfaceview覆盖于svB的上层，需要设置如下属性
		svS.setZOrderMediaOverlay(true);
		svP.setZOrderMediaOverlay(true);

		npB = new NodePlayer(this);
		npS = new NodePlayer(this);
		npP = new NodePlayer(this);

		npB.setDelegate(this);
		npS.setDelegate(this);
		npP.setDelegate(this);

		/**
	     * 设置播放Surfaceview
	     *   如果Surfaceview传入nil,则不解码播放视频,作为纯音频播放模式
	     *   画面填充模式,当前支持下面三种现实模式，他们的差别是 
	     *   当Surfaceview高宽比与视频高宽比不同时           视频画面是否铺满uiview |画面是否变形|有无黑边|视频画面是否会被裁剪
	     *   拉伸填充 UIViewContentModeScaleToFill           是               |   是      |  无   | 否
	     *   等比缩放 UIViewContentModeScaleAspectFit        否               |   否      |  有   | 否
	     *   等比缩放填充 UIViewContentModeScaleAspectFill    是               |   否      |  无   | 是
	     */
		
		npB.setSurfaceView(svB, NodePlayer.UIViewContentModeScaleAspectFit);
		npS.setSurfaceView(svS, NodePlayer.UIViewContentModeScaleAspectFit);
		npP.setSurfaceView(svP, NodePlayer.UIViewContentModeScaleAspectFit);

		npB.startPlay("rtmp://xyplay.nodemedia.cn/live/demo1");
		npS.startPlay("rtmp://xyplay.nodemedia.cn/live/demo2");
		npP.startPlay("rtmp://xyplay.nodemedia.cn/live/demo3");

		tbB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				npB.setSpkEnable(arg1);
			}
		});

		tbS.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				npS.setSpkEnable(arg1);
			}
		});

		tbP.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				npP.setSpkEnable(arg1);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		npB.stopPlay();
		npS.stopPlay();
		npP.stopPlay();
		npB.deInit();
		npS.deInit();
		npP.deInit();
	}

	@Override
	public void onEventCallback(NodePlayer player, int event, String msg) {
		if (player.equals(npB)) {
			Log.d("NodePlayer.onEventCallback:", "npB event:" + event + " msg:" + msg);
		} else if (player.equals(npS)) {
			Log.d("NodePlayer.onEventCallback:", "npS event:" + event + " msg:" + msg);
		} else if (player.equals(npP)) {
			Log.d("NodePlayer.onEventCallback:", "npP event:" + event + " msg:" + msg);
		}
	}
}
