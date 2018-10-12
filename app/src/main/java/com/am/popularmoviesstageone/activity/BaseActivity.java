package com.am.popularmoviesstageone.activity;

import android.support.v7.app.AppCompatActivity;

import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;
import com.am.popularmoviesstageone.util.PrefHelper;

public class BaseActivity extends AppCompatActivity {
    protected ApiRequests mApiService = APIClient.getClient().create(ApiRequests.class);

    protected PrefHelper getPref() {
        return PrefHelper.getInstance(this);
    }

}
