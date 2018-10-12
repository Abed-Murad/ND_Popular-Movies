package com.am.popularmoviesstageone.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;
import com.am.popularmoviesstageone.util.PrefHelper;

public class BaseActivity extends AppCompatActivity {
    protected ApiRequests mApiService = APIClient.getClient().create(ApiRequests.class);

    protected PrefHelper getPref() {
        return PrefHelper.getInstance(this);
    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}
