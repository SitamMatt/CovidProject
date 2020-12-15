package edu.covidianie.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.covidianie.R


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val pysznePackageName = "com.yourdelivery.pyszne"
    private val glovoPackageName = "com.glovo"
    private val uberPackageName = "com.ubercab.eats"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        if (root != null) {
            setupLayout(root)
        }

        val pac = context?.packageManager?.getPackageInfo("com.glovo", 0)
        val appctx = context?.createPackageContext("com.glovo", Context.CONTEXT_IGNORE_SECURITY)
        appctx?.resources?.getDrawableForDensity(
            pac?.applicationInfo?.icon!!,
            DisplayMetrics.DENSITY_XXXHIGH
        )


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

    private fun getIcon(ctx: Context, packageName: String): Drawable? {
        val pkg = ctx.packageManager.getPackageInfo(packageName, 0)
        val appCtx = ctx.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        //        return ContextCompat.getDrawable(appCtx, pkg.applicationInfo.icon)
        return ResourcesCompat.getDrawableForDensity(
            appCtx.resources,
            pkg.applicationInfo.icon,
            DisplayMetrics.DENSITY_600,
            null
        )
    }

    private fun setupLayout(root: View) {
        val pyszneCard = root.findViewById<CardView>(R.id.pyszne_card)
        setupCard(pyszneCard, pysznePackageName)
        val uberCard = root.findViewById<CardView>(R.id.uber_eats_card)
        setupCard(uberCard, uberPackageName)
        val dr = resources.getDrawable(R.drawable.glovo)
        val bitmap = (dr as BitmapDrawable).bitmap
        val d: Drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, resources.displayMetrics.densityDpi, resources.displayMetrics.densityDpi, true))
        val glovoCard = root.findViewById<CardView>(R.id.glovo_card)
        setupCard(glovoCard, glovoPackageName, d)
    }

    private fun setupCard(card: CardView, packageName: String) {
        val context = context ?: return
        val launchIntent: Intent? =
            context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
//            val icon = context.packageManager.getApplicationIcon(packageName)
//            val drawable = ScaleDrawable(icon, 0, 64F, 64F).drawable
//            drawable?.setBounds(0,0,64,64)
            val drawable = getIcon(context, packageName)
            val imageView = card.findViewById<AppCompatImageView>(R.id.icon)
            imageView.setImageDrawable(drawable)
            card.setOnClickListener { startActivity(launchIntent) }
        } else {
            card.setOnClickListener { launchGooglePlayApp(packageName) }
            val drawable = resources.getDrawable(R.drawable.ic_baseline_not_interested_64)
            val imageView = card.findViewById<AppCompatImageView>(R.id.icon)
            imageView.setImageDrawable(drawable)
        }
    }

    private fun setupCard(card: CardView, packageName: String, icon: Drawable) {
        val context = context ?: return
        val launchIntent: Intent? =
            context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
//            val icon = context.packageManager.getApplicationIcon(packageName)
//            val drawable = ScaleDrawable(icon, 0, 64F, 64F).drawable
//            drawable?.setBounds(0,0,64,64)
//            val drawable = getIcon(context, packageName)
            val imageView = card.findViewById<AppCompatImageView>(R.id.icon)
            imageView.setImageDrawable(icon)
            card.setOnClickListener { startActivity(launchIntent) }
        } else {
            card.setOnClickListener { launchGooglePlayApp(packageName) }
            val drawable = resources.getDrawable(R.drawable.ic_baseline_not_interested_64)
            val imageView = card.findViewById<AppCompatImageView>(R.id.icon)
            imageView.setImageDrawable(drawable)
        }
    }

    private fun launchGooglePlayApp(packageName: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )

    }
}