package edu.covidianie.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import org.jsoup.Jsoup

fun rasterize(drawable: Drawable, bitmap: Bitmap, size: Int) {
    drawable.setBounds(0, 0, size, size)
    val canvas = Canvas()
    canvas.setBitmap(bitmap)
    drawable.draw(canvas)
}

fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}

fun getArticleContent(url: String): String? {
    val doc = Jsoup.connect(url).get()
    val content = doc.selectFirst("article#main-content > div.editor-content")
    // replace document body leaving only article content (removes ads, sidebars etc.)
    doc.body().html(content.html())
    return doc.html()
}

fun extractArticleContent(url: String): String? {
    val doc = Jsoup.connect(url).get()
    val content = doc.selectFirst("article#main-content")
    doc.body().html(content.outerHtml())
    doc.body().attr("style", "padding:20px;")
    return doc.html()
}