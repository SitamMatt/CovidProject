package edu.covidianie.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.covidianie.R
import com.squareup.picasso.*

class ArticleAdapter(
    private val context: Context,
    private val dataSource: ArrayList<ArticleItem>): BaseAdapter() {

    private val inflater: LayoutInflater
        = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(p0: Int): Any {
        return dataSource[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_article_item, p2, false)
        val titleTextView = rowView.findViewById(R.id.article_list_title) as TextView
        val thumbnailImageView = rowView.findViewById(R.id.article_list_thumbnail) as ImageView

        val article = getItem(p0) as ArticleItem

        titleTextView.text = article.title
        Picasso.with(context).load(article.imgUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        return rowView
    }
}