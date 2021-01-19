package edu.covidianie.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

fun rasterize(drawable: Drawable, bitmap: Bitmap, size: Int) {
    drawable.setBounds(0, 0, size, size)
    val canvas = Canvas()
    canvas.setBitmap(bitmap)
    drawable.draw(canvas)
}

fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}