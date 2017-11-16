package pub.tanzby.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MusicService extends Service {

    static private MediaPlayer player;
    private IBinder mBinder;

    public MusicService() {
        mBinder = new MyBinder();
        if (player==null) {
            player = new MediaPlayer();
        }
        try {
            String path = Environment.getExternalStorageDirectory() + "/melt.mp3";
            player.setDataSource(path);
            player.prepare();
            player.seekTo(0);
            player.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class MyBinder extends Binder {
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            switch (code) {
                case 0:// play or pause
                    if (player.isPlaying()) {
                        player.pause();
                        reply.writeInt(1);
                    } else {
                        player.start();
                        reply.writeInt(0);
                    }
                    break;
                case 1: // stop
                    try {
                        player.stop();
                        player.prepare();
                        player.seekTo(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reply.writeInt(2);
                    break;
                case 2: // exit
                    onDestroy();
                    break;
                case 3: // get current  point
                    reply.writeInt(player.getCurrentPosition());
                    break;
                case 4: // seek to some point
                    player.seekTo(data.readInt());
                    break;
                case 5: // get the play time of song
                    reply.writeInt(player.getDuration());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    @Override
    public void onDestroy() {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
        super.onDestroy();
    }

}
