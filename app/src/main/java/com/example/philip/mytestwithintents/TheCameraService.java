package com.example.philip.mytestwithintents;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Philip G on 2018-03-27.
 */

public class TheCameraService extends Service {

    @Override
    public void onCreate() {
        //Initiali setup
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //The things it will do

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        return START_STICKY;

       // return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
