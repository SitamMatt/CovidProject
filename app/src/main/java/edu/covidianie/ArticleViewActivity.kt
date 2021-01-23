package edu.covidianie

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import edu.covidianie.model.ArticleItem

class ArticleViewActivity : AppCompatActivity() {
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
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}