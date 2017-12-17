package com.example.pablo384.newsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.pablo384.newsapp.Extensions.listaArticulos
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webview.loadUrl(listaArticulos[intent.getIntExtra("article",0)].url)
    }
}
