package com.example.pablo384.newsapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pablo384.newsapp.Adapters.ArticleAdapter
import com.example.pablo384.newsapp.model.Article
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.pablo384.newsapp.Extensions.iterator
import com.example.pablo384.newsapp.Extensions.listaArticulos
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener, ArticleAdapter.OnButtonClickListener {


    val TAG = "MAINACTIVITY"

    var articleAdapter:ArticleAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        httpRequest()

        articleAdapter = ArticleAdapter(listaArticulos,this,this)
        my_recycler_view_news.setHasFixedSize(true)
        my_recycler_view_news.layoutManager = LinearLayoutManager(baseContext)
        my_recycler_view_news.adapter = articleAdapter

    }

    override fun onItemClick(article: Article, position: Int) {
        Log.d(TAG,"Clic en articulo")
    }

    override fun onButtonClick(article: Article, position: Int) {
        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(article.url)))
    }

    fun httpRequest(){
        val url = "https://newsapi.org/v2/top-headlines?" +
                "sources=bbc-news&" +
                "apiKey=bc1fd0ba7c8444f0b11245116eacef3a"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonrequest = JsonObjectRequest(Request.Method.GET, url,null,
                Response.Listener<JSONObject> { response ->

                    try {
                        listaArticulos.removeAll(listaArticulos)
                        listaArticulos.addAll(procesarJSON(response))
                        articleAdapter!!.notifyDataSetChanged()
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
