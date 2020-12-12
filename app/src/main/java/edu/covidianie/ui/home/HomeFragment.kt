package edu.covidianie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import edu.covidianie.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

//        viewLifecycleOwner.lifecycleScope.apply {
//            val doc: Document = Jsoup.connect("https://en.wikipedia.org/").get()
////            log(doc.title())
//            val newsHeadlines: Elements = doc.select("#mp-itn b a")
//            for (headline in newsHeadlines) {
////                log("%s\n\t%s",
////                        headline.attr("title"), headline.absUrl("href"))
//            }
//        }
        return root
    }
}