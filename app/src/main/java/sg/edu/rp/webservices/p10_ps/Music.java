package sg.edu.rp.webservices.p10_ps;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

public class Music extends Service {
    private MediaPlayer player = new MediaPlayer();
    public Music() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test", "music.mp3");
            player.setDataSource(file.getPath());
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }

        player.setLooping(true);
        player.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        Log.d("Music", "Stopped");
        super.onDestroy();
        player.stop();
    }

}