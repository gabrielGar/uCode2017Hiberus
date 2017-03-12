package com.ubeacon.rts.ubeacon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.ubeacon.rts.R;

public class WebViewInfo extends Activity {
    public WebView info_web_view ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        info_web_view = (WebView) findViewById(R.id.info_web_view);
        Intent initIntent = getIntent();
        String html = initIntent.getStringExtra("html");
        info_web_view.getSettings().setJavaScriptEnabled(true);
        info_web_view.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
    }
}
