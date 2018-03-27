package com.example.philip.mytestwithintents;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Philip G on 2018-03-27.
 */

public class SecondActivity extends Activity {


    private static final String TAG = Activity.class.getName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.second_layout);
        Log.d(TAG,"Hello from onCreate in SecondActivity");


    }
}
