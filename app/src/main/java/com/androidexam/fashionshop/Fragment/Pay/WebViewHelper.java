package com.androidexam.fashionshop.Fragment.Pay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class WebViewHelper {

    public static void openUrlInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
