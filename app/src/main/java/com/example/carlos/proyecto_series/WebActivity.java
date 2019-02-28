package com.example.carlos.proyecto_series;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //En esta activadad usamos un WebView para poder visualizar el url de la pagina de la serie si es que tiene

        WebView web =findViewById(R.id.webi);

        Intent intent=getIntent();

        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(intent.getStringExtra("sitio"));
    }
}
