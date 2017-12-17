package com.example.pablo384.newsapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import android.support.v4.widget.SearchViewCompat.setSearchableInfo
import android.support.v4.view.MenuItemCompat.getActionView
import android.content.Context.SEARCH_SERVICE
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.SearchView
import android.support.v4.widget.SearchViewCompat.setIconified
import android.support.v4.widget.SearchViewCompat.isIconified
import android.support.v4.widget.SearchViewCompat.setOnQueryTextListener
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), ArticleAdapter.OnItemClickListener, ArticleAdapter.OnButtonClickListener {


    val TAG = "MAINACTIVITY"

    var articleAdapter:ArticleAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        httpRequest()
        val actb = appbar as Toolbar
        actb.title=""
        setSupportActionBar(actb)

        articleAdapter = ArticleAdapter(listaArticulos,this,this)
        my_recycler_view_news.setHasFixedSize(true)
        my_recycler_view_news.layoutManager = LinearLayoutManager(baseContext)
        my_recycler_view_news.adapter = articleAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu.findItem(R.id.search).actionView as SearchView
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(componentName))

//        val searchItem = menu.findItem(R.id.search)
//
//        val searchManager = this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//
//        var searchView: SearchView? = null
//        if (searchItem != null) {
//            searchView = searchItem.actionView as SearchView
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))
//        }
//        return super.onCreateOptionsMenu(menu)

        val myActionMenuItem = menu.findItem(R.id.search)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print
                toast("SearchOnQueryTextSubmit: " + query)
                httpRequest(search = query)
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_bbc -> httpRequest("bbc-news")
            R.id.action_verge -> httpRequest("the-next-web,the-verge")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(article: Article, position: Int) {
        Log.d(TAG,"Clic en articulo")
    }

    override fun onButtonClick(article: Article, position: Int) {
        startActivity(Intent(this@MainActivity,WebViewActivity::class.java).putExtra("article",position))
    }

    override fun onButtonShareClick(article: Article, position: Int) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, article.url)
        intent.type = "text/plain"
        startActivity(intent)
    }

    fun httpRequest( source:String="the-next-web,the-verge", search:String?=null){
        var url = "https://newsapi.org/v2/top-headlines?sources=$source&apiKey=bc1fd0ba7c8444f0b11245116eacef3a"
        if (search != null){
            val param = "q=$search"
            url = "https://newsapi.org/v2/top-headlines?$param&apiKey=bc1fd0ba7c8444f0b11245116eacef3a"
        }
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
