package com.manhdong.recycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Saphiro on 7/18/2016.
 */
public class myWebClient extends WebViewClient {

    Context context;
    public myWebClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
        return true;
//        return super.shouldOverrideUrlLoading(view, url);
    }
}
