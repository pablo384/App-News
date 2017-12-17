package com.example.pablo384.newsapp.Adapters

import android.widget.AdapterView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pablo384.newsapp.R
import com.example.pablo384.newsapp.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_news_items.view.*

/**
 * Created by pablo384 on 16/12/17.
 */
class ArticleAdapter(val list:ArrayList<Article>, val itemClick:OnItemClickListener, val buttonClic:OnButtonClickListener):RecyclerView.Adapter<ArticleAdapter.NewsViewHolder>(){
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        return holder.bind(list[position], itemClick, buttonClic )
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder
            = NewsViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.layout_news_items,parent,false))

    override fun getItemCount(): Int = list.size


    class NewsViewHolder(newsItemView: View):RecyclerView.ViewHolder(newsItemView){
        fun bind(article:Article, onItemClickListener: OnItemClickListener, onButtonClickListener: OnButtonClickListener)= with(itemView){
            textViewAuthor.text= article.author
            textViewDescription.text= article.description
            buttonViewNews.setOnClickListener { onButtonClickListener.onButtonClick(article,position) }
            buttonShareNews.setOnClickListener { onButtonClickListener.onButtonShareClick(article,position) }
            setOnClickListener { onItemClickListener.onItemClick(article,position) }

            Picasso.with(context).load(article.urlToImage)
                    .into(imageViewimage)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(article:Article, position:Int)
    }
    interface OnButtonClickListener{
        fun onButtonClick(article:Article, position:Int)
        fun onButtonShareClick(article:Article, position:Int)
    }


}