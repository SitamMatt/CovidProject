package edu.covidianie.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.AdaptiveIconDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import edu.covidianie.R
import edu.covidianie.utils.dpToPx
import edu.covidianie.utils.rasterize

class AppCard : CardView {
    private val iconSizePixels = 64

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private var alternativeText: String? = null

    private var _iconSize: Int = iconSizePixels
    var iconSize: Int
        get() = _iconSize
        set(value) {
            _iconSize = value
        }

    private var _packageName: String? = null
    var packageName: String?
        get() = _packageName
        set(value) {
            _packageName = value
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    @SuppressLint("NewApi")
    private fun init(attrs: AttributeSet?, defStyle: Int) {
        View.inflate(context, R.layout.view_app_card, this)

        // Load attributes
        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.AppCard, defStyle, 0
        )

        alternativeText = attributes.getString(
            R.styleable.AppCard_alternativeText
        )

        _packageName = attributes.getString(
            R.styleable.AppCard_packageName
        )

        _iconSize = attributes.getDimensionPixelSize(
            R.styleable.AppCard_appIconSize,
            dpToPx(context, iconSizePixels)
        )

        attributes.recycle()

        val packageName = _packageName ?: return

        textView = findViewById(R.id.app_name_text_view)
        imageView = findViewById(R.id.app_icon_image_view)

        try {
            val info = packageName.let { context.packageManager.getApplicationInfo(it, 0) }

            val icon = Bitmap.createBitmap(_iconSize, _iconSize, Bitmap.Config.ARGB_8888).also {
                val iconDrawable = info.loadIcon(context.packageManager) as AdaptiveIconDrawable
                rasterize(iconDrawable, it, _iconSize)
            }
            val name = context.packageManager.getApplicationLabel(info)
            val launchIntent =
                context.packageManager.getLaunchIntentForPackage(packageName) ?: return

            textView.text = name
            imageView.setImageBitmap(icon)
            setOnClickListener { startActivity(context, launchIntent, null) }

        } catch (ex: PackageManager.NameNotFoundException) {
            val notFoundDrawable =
                resources.getDrawable(R.drawable.ic_baseline_not_interested_64, null)
            imageView.setImageDrawable(notFoundDrawable)
            textView.text = alternativeText ?: packageName
            val launchIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
            setOnClickListener { startActivity(context, launchIntent, null) }
        }

    }
}