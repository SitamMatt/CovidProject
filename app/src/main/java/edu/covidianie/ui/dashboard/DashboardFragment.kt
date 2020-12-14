package edu.covidianie.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import edu.covidianie.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val myWebView: WebView = root.findViewById<WebView>(R.id.webview)
        myWebView.loadUrl("https://www.gov.pl/web/koronawirus/aktualne-zasady-i-ograniczenia")
        val webSettings: WebSettings = myWebView.settings
        webSettings.javaScriptEnabled = true

        myWebView.webViewClient = WebViewClient()
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}