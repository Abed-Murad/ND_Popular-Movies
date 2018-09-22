package com.am.popularmoviesstageone.activity;

import android.support.v7.app.AppCompatActivity;

import com.am.popularmoviesstageone.util.PrefHelper;

public class BaseActivity extends AppCompatActivity {

    protected PrefHelper getPref() {
        return PrefHelper.getInstance(this);
    }

}
