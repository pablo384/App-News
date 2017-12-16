package com.example.pablo384.newsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pablo384.newsapp.model.Article
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.pablo384.newsapp.Extensions.iterator

class MainActivity : AppCompatActivity() {
    val TAG = "MAINACTIVITY"
    var listaArticulos = ArrayList<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        httpRequest()
    }

    fun httpRequest(){
        val url = "https://newsapi.org/v2/top-headlines?" +
                "sources=bbc-news&" +
                "apiKey=bc1fd0ba7c8444f0b11245116eacef3a"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonrequest = JsonObjectRequest(Request.Method.GET, url,null,
                Response.Listener<JSONObject> { response ->

                    try {
                        listaArticulos =  procesarJSON(response)
                    }catch (a:JSONException){
                        a.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Log.d(TAG,error.toString()) })
        requestQueue.add(jsonrequest)
    }
    fun procesarJSON(json:JSONObject):ArrayList<Article>{
        Log.d(TAG,json.toString())
        val jsonObject = json
        val articles:JSONArray = jsonObject.getJSONArray("articles")
        val listArticles = ArrayList<Article>()
        for (i in articles){
            val author = i.getString("author")
            val title = i.getString("title")
            val description = i.getString("description")
            val url = i.getString("url")
            val urlImg = i.getString("urlToImage")
            listArticles.add(Article(author,title,description,url,urlImg))
        }
        return listArticles

    }
}
