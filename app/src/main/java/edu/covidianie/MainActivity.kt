package edu.covidianie

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    private val glovoPackageName = "com.ubercab.eats"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycleScope.launch(Dispatchers.IO){
//            val layerSize = 1024
//            val icon = packageManager.getApplicationIcon(glovoPackageName) as AdaptiveIconDrawable
//            val extStorageDirectory: String = Environment.getExternalStorageDirectory().toString()
//            val foreground = Bitmap.createBitmap(layerSize, layerSize, Bitmap.Config.ARGB_8888)
//            val c  = Canvas()
//            rasterize(icon, foreground, c)
//
//            val file = File(extStorageDirectory, "ic_launcher.PNG")
//            val outStream = FileOutputStream(file)
//            foreground?.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//            outStream.flush()
//            outStream.close()
//        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun rasterize(drawable: Drawable, bitmap: Bitmap, canvas: Canvas) {
        drawable.setBounds(0, 0, 1024, 1024)
        canvas.setBitmap(bitmap)
        drawable.draw(canvas)
    }

    fun getDefaultBitmap(d: Drawable?): Bitmap? {
        if (d is BitmapDrawable) {
            return d.bitmap
        } else if (Build.VERSION.SDK_INT >= 26
            && d is AdaptiveIconDrawable
        ) {
            val w = d.intrinsicWidth
            val h = d.intrinsicHeight
            val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            d.setBounds(0, 0, w, h)
            d.draw(canvas)
            return result
        }
        val density: Float = resources.displayMetrics.density
        val defaultWidth = (48 * density).toInt()
        val defaultHeight = (48 * density).toInt()
        return Bitmap.createBitmap(defaultWidth, defaultHeight, Bitmap.Config.ARGB_8888)
    }


}