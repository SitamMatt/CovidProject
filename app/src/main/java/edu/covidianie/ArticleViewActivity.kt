package edu.covidianie

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.lifecycle.lifecycleScope
import edu.covidianie.model.ArticleItem
import edu.covidianie.utils.extractArticleContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewActivity : AppCompatActivity() {
    private var baseUrl: String = "https://www.gov.pl"
    private lateinit var webView: WebView

    companion object {
        //const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"

        fun newIntent(context: Context, article: ArticleItem): Intent {
            val detailIntent = Intent(context, ArticleViewActivity::class.java)

            //detailIntent.putExtra(EXTRA_TITLE, article.title)
            detailIntent.putExtra(EXTRA_URL, article.url)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_view)

        //val title = intent.extras.getString(EXTRA_TITLE)
        val url = intent.extras?.getString(EXTRA_URL)

        webView = findViewById(R.id.detail_web_view)
        lifecycleScope.launch(Dispatchers.IO) {
            if (url != null) {
                val html = extractArticleContent(url)
                lifecycleScope.launch(Dispatchers.Main) {
                    if (html != null) {
                        webView.loadDataWithBaseURL(baseUrl, html, "text/html", "UTF-8", null)
                    }
                }
            }
        }
    }
}