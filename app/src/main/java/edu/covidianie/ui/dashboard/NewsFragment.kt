package edu.covidianie.ui.dashboard

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
    private val newsUrl = "https://www.gov.pl/web/koronawirus/wiadomosci";
    private val rulesUrl = "https://www.gov.pl/web/koronawirus/aktualne-zasady-i-ograniczenia"
    private lateinit var articleList: ListView
    private lateinit var webTextView: TextView

    private val articles = ArrayList<ArticleItem>()
    private val articlesTitles = ArrayList<String>()

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

        lifecycleScope.launch(Dispatchers.IO) {
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
                articlesTitles.add(title)
            }
            val context = activity?.applicationContext
            articleList.setOnItemClickListener { _, _, position, _ ->
                // 1
                val selectedRecipe = articles[position]

                // 2
                val detailIntent = context?.let { ArticleViewActivity.newIntent(it, selectedRecipe) }

                // 3
                startActivity(detailIntent)
            }
            val adapter = context?.let { ArticleAdapter(it, articles) }
//            val article = articles.first()
//            val content = getArticleContent(article.url)
//            if(content != null){
//                lifecycleScope.launch(Dispatchers.Main) {
//                    webTextView.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
//                }
//            }
            lifecycleScope.launch(Dispatchers.Main){
                articleList.adapter = adapter
            }
        }
        return root
    }

    private fun getArticleContent(url: String): String? {
        val doc = Jsoup.connect(url).get()
        val content = doc.selectFirst("article#main-content > div.editor-content")
        // replace document body leaving only article content (removes ads, sidebars etc.)
        doc.body().html(content.html())
        return doc.html()
    }
}