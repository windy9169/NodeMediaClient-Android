package cn.nodemedia.nodemediaclient_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import cn.nodemedia.LivePlayer;
import cn.nodemedia.LivePlayerDelegate;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;

public class LivePlayerDemoActivity extends AppCompatActivity implements NodePlayerDelegate {

    NodePlayer np;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_player_demo);
        getSupportActionBar().hide();

        SurfaceView sv = (SurfaceView)findViewById(R.id.surfaceView_play);
        np = new NodePlayer(this);
        np.setDelegate(this);
        np.setSurfaceView(sv,NodePlayer.UIViewContentModeScaleAspectFit);

        int bufferTime = Integer.valueOf(SharedPreUtil.getString(this, "bufferTime"));
        np.setBufferTime(bufferTime);

        int maxBufferTime = Integer.valueOf(SharedPreUtil.getString(this, "maxBufferTime"));
        np.setMaxBufferTime(maxBufferTime);

        String playUrl = SharedPreUtil.getString(this, "playUrl");
        np.startPlay(playUrl);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        np.stopPlay();
        np.setDelegate(null);
        np.deInit();
    }


    @Override
    public void onEventCallback(NodePlayer nodePlayer, int i, String s) {
        Log.i("NodeMediaClient","onEventCallback:"+i+" msg:"+s);
    }
}
