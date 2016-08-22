package cn.nodemedia.nodemediaclient_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;

import android.widget.CompoundButton.OnCheckedChangeListener;


public class NodePlayerDemoActivity extends AppCompatActivity implements NodePlayerDelegate {
    private NodePlayer npB, npS, npP;
    private SurfaceView svB, svS, svP;
    private ToggleButton tbB, tbS, tbP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_player_demo);

        svB = (SurfaceView) findViewById(R.id.surfaceView_b);
        svS = (SurfaceView) findViewById(R.id.surfaceView_s);
        svP = (SurfaceView) findViewById(R.id.surfaceView_p);

        tbB = (ToggleButton) findViewById(R.id.toggleButton1);
        tbS = (ToggleButton) findViewById(R.id.toggleButton2);
        tbP = (ToggleButton) findViewById(R.id.toggleButton3);

        svS.setZOrderMediaOverlay(true);
        svP.setZOrderMediaOverlay(true);

        npB = new NodePlayer(this);
        npS = new NodePlayer(this);
        npP = new NodePlayer(this);

        npB.setDelegate(this);
        npS.setDelegate(this);
        npP.setDelegate(this);

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
            Log.d("NodePlayer", "npB event:" + event + " msg:" + msg);
        } else if (player.equals(npS)) {
            Log.d("NodePlayer", "npS event:" + event + " msg:" + msg);
        } else if (player.equals(npP)) {
            Log.d("NodePlayer", "npP event:" + event + " msg:" + msg);
        }
    }

}
