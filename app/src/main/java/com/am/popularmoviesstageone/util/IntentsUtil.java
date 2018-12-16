package com.am.popularmoviesstageone.util;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.am.popularmoviesstageone.R;

import static com.am.popularmoviesstageone.util.CONST.BASE_YOUTUBE_URL;

public class IntentsUtil {

    public static void openVideoOnYoutube(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(BASE_YOUTUBE_URL + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }


    public static void openUrlInChromeCustomTab(Context context, String url) {
        if (!(url.startsWith("http:") || url.startsWith("https:"))) {
            url = "http://" + url;
        }


        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorAccent))
                .addDefaultShareMenuItem()
                .enableUrlBarHiding()
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl(context, Uri.parse(url));

    }


}
