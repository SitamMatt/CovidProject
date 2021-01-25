package edu.covidianie.ui.restrictions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.lifecycle.lifecycleScope
import edu.covidianie.R
import edu.covidianie.ui.dashboard.DashboardViewModel
import edu.covidianie.utils.extractArticleContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestrictionsFragment : Fragment() {

    private val rulesUrl = "https://www.gov.pl/web/koronawirus/aktualne-zasady-i-ograniczenia"
    private val baseUrl = "https://www.gov.pl"
    private lateinit var webTextView: WebView

    private lateinit var viewModel: RestrictionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RestrictionsViewModel::class.java)
        val root = inflater.inflate(R.layout.restrictions_fragment, container, false)

        webTextView = root.findViewById(R.id.restrView)

        lifecycleScope.launch(Dispatchers.IO){
            val html = extractArticleContent(rulesUrl)

            lifecycleScope.launch(Dispatchers.Main){
                if (html != null) {
                    webTextView.loadDataWithBaseURL(baseUrl, html, "text/html", "UTF-8", null)
                }
            }
        }

        return root
    }

}