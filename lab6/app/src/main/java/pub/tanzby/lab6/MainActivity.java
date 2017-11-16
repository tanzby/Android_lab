package pub.tanzby.lab6;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private Button stop_bnt;
    private Button play_bnt;
    private Button quit_bnt;
    private TextView cur_pos_tv;
    private TextView end_pos_tv;
    private TextView status_tv;
    private Integer status;
    private SeekBar seekBar;
    private Boolean is_changing_seek;
    private IBinder mbinder;
    private ServiceConnection mServerConnect;
    private SimpleDateFormat mSmf = new SimpleDateFormat("mm:ss");
    private ObjectAnimator mObjAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permission.verifyStoragePermissions(this);
        bindElem();
        bindEven();
        initServer();
        initHandle();
    }

    private void  bindElem()
    {
        status = -1;
        is_changing_seek = false;
        seekBar =(SeekBar)findViewById(R.id.seekBar);
        stop_bnt=(Button)findViewById(R.id.stop_button);
        play_bnt=(Button)findViewById(R.id.play_button);
        quit_bnt=(Button)findViewById(R.id.quit_button);
        cur_pos_tv=(TextView)findViewById(R.id.nowPoint);
        end_pos_tv=(TextView)findViewById(R.id.endPoint);
        status_tv=(TextView)findViewById(R.id.status_tv);

        ImageView albumImg = (ImageView) findViewById(R.id.imageView);
        mObjAnim = ObjectAnimator.ofFloat(albumImg,"rotation", 0, 359);
        mObjAnim.setDuration(20000);
        mObjAnim.setInterpolator(new LinearInterpolator());
        mObjAnim.setRepeatCount(ObjectAnimator.INFINITE);
        mObjAnim.setRepeatMode(ObjectAnimator.RESTART);
        mObjAnim.start();
        mObjAnim.pause();

    }

    private void bindEven(){
        play_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parcel reply = Parcel.obtain();
                    mbinder.transact(0, null, reply, 0);
                    status = reply.readInt();
                    refreshStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stop_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parcel reply = Parcel.obtain();
                    mbinder.transact(1, null, reply, 0);
                    status = reply.readInt();
                    refreshStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        quit_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cur_pos_tv.setText(mSmf.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                is_changing_seek=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Parcel data = Parcel.obtain();
                    data.writeInt(seekBar.getProgress());
                    mbinder.transact(4, data, null, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is_changing_seek=false;
            }
        });
    }

    private void initServer()
    {
        mServerConnect=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("music_service", "connected");
                mbinder=service;
                try {
                    Parcel reply = Parcel.obtain();
                    mbinder.transact(5, null, reply, 0); //get the duration of the song
                    int timeDuration = reply.readInt();
                    seekBar.setMax(timeDuration);
                    end_pos_tv.setText(mSmf.format(timeDuration));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServerConnect = null;
            }
        };


        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mServerConnect, BIND_AUTO_CREATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                                         permissions,
                                         grantResults);
        if (!(grantResults.length > 0
                &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED))
            onDestroy();
    }


    private void initHandle()
    {
        final Handler refresher = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if(!is_changing_seek) {
                    try {
                        Parcel reply = Parcel.obtain();
                        mbinder.transact(3, null, reply, 0);
                        seekBar.setProgress(reply.readInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };
        Thread t_refresher = new Thread() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mServerConnect != null) {
                        refresher.obtainMessage().sendToTarget();
                    }
                }
            }
        };
        t_refresher.start();
    }

    private void refreshStatus() throws RemoteException {
        switch (status)
        {
            case 0: //play
                play_bnt.setText("PAUSE");
                status_tv.setText("playing");
                mObjAnim.resume();
                break;
            case 1: //pause
                play_bnt.setText("PLAY");
                status_tv.setText("pausing");
                mObjAnim.pause();
                break;
            case 2: //stop
                play_bnt.setText("PLAY");
                status_tv.setText("stopped");
                mObjAnim.pause();
                break;
        }
    }

    @Override
    public void onDestroy() {

        try {
            mbinder.transact(2, null, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        unbindService(mServerConnect);
        mServerConnect = null;

        super.onDestroy();

        this.finish();
    }
}
