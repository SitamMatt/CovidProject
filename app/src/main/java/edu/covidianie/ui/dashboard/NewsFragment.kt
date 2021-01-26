package edu.covidianie.ui.dashboard

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import edu.covidianie.ArticleViewActivity
import edu.covidianie.R
import edu.covidianie.model.ArticleAdapter
import edu.covidianie.model.ArticleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class NewsFragment : Fragment() {
    private var pageCount = 1
    private var newsUrl = "https://www.gov.pl/web/koronawirus/wiadomosci?page=$pageCount";
    private lateinit var articleList: ListView
    private lateinit var nextArticlesBtn: Button

    private val articles = ArrayList<ArticleItem>()

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_news, container, false)

        //webTextView = root.findViewById(R.id.web_text)
        //webTextView.movementMethod = LinkMovementMethod.getInstance()

        articleList = root.findViewById(R.id.article_list_view)
        nextArticlesBtn = root.findViewById(R.id.getNextArticlesBtn)

        val context = activity?.applicationContext
        articleList.setOnItemClickListener { _, _, position, _ ->
            val selectedArticle = articles[position]
            val detailIntent = context?.let { ArticleViewActivity.newIntent(it, selectedArticle) }
            startActivity(detailIntent)
        }

        nextArticlesBtn.setOnClickListener{
            pageCount += 1
            newsUrl = "https://www.gov.pl/web/koronawirus/wiadomosci?page=$pageCount"
            lifecycleScope.launch(Dispatchers.IO) {
                getArticlesFromUrl()
                val currentPos = articleList.firstVisiblePosition
                articleList.post {
                    run {
                        articleList.setSelection(currentPos)
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            getArticlesFromUrl()
        }
        return root
    }

    private fun getArticlesFromUrl() {
        val doc = Jsoup.connect(newsUrl).get()
        val news = doc.select("div.art-prev > ul > li")

        news?.forEach{
            val title = it.selectFirst("div.title").text()
            val intro = it.selectFirst("div.intro")?.text()
            val url = it.selectFirst("> a").absUrl("href")
            val imgUrl = it.selectFirst("> a > picture > img").absUrl("src")
            val article = ArticleItem(
                title, intro, url, imgUrl
            )
            articles.add(article)
        }
        updateAdapter()
    }

    private fun updateAdapter(){
        val adapter = context?.let { ArticleAdapter(it, articles) }
        lifecycleScope.launch(Dispatchers.Main){
            articleList.adapter = adapter
        }
    }
}