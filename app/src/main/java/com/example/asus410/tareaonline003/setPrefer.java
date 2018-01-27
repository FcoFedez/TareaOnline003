package com.example.asus410.tareaonline003;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by asus410 on 27/12/2017.
 */

public class setPrefer extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new ActividadadPreferences()).commit();
    }
}
