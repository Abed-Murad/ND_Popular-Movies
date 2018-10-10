package com.am.popularmoviesstageone.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        setSupportActionBar(mLayout.toolbar);



        getSupportActionBar().setTitle("Leon: The Professional");
        mLayout.fab.setOnClickListener(view -> Snackbar.make(view, "Added to favorite ",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());



    }
}
