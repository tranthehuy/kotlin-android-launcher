package com.example.mylauncher00h21

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.mylauncher00h21.components.homepager.HomePagerAdapter


class MainActivity : AppCompatActivity() {

    private val appNames: ArrayList<String> = ArrayList<String>()
    private val packageNames: ArrayList<String> = ArrayList<String>()

    private lateinit var pagerAdapter: HomePagerAdapter
    private lateinit var viewPager: ViewPager

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_main)

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = this.applicationContext.packageManager
        val pkgAppsList = pm.queryIntentActivities(mainIntent, 0)

        for (app in pkgAppsList) {
            val str = app.loadLabel(pm)
            // appNames.add((appNames.size + 1).toString() + ". " + str.toString());
            appNames.add(str.toString())
            packageNames.add(app.activityInfo.packageName.toString())
        }

        pagerAdapter = HomePagerAdapter(supportFragmentManager)
        viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = pagerAdapter

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position != 0) return;
                val textView = this@MainActivity.findViewById<TextView>(R.id.tvBranding)
                if (textView != null) {
                    textView.setTextColor(Color.WHITE);
                    val colorAnim = ObjectAnimator.ofInt(textView, "textColor",
                        Color.WHITE, Color.BLUE);
                    colorAnim.setEvaluator(ArgbEvaluator());
                    colorAnim.startDelay = 500;
                    colorAnim.start();

                    colorAnim.doOnEnd {
                        Log.d("end", "test")
                    }


                    val aniSlide: Animation =
                        AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)

                    textView.clearAnimation();
                    textView.startAnimation(aniSlide);

                }
            }
            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val callBtn = findViewById<Button>(R.id.btn_call)
        callBtn.setOnClickListener { _ ->
            val i = appNames.indexOf("Contacts")
            val launchIntent = pm.getLaunchIntentForPackage(packageNames[i])
            launchIntent?.let { startActivity(it) }
        }

        val msgBtn = findViewById<Button>(R.id.btn_message)
        msgBtn.setOnClickListener { _ ->
            val i = appNames.indexOf("Messages")
            val launchIntent = pm.getLaunchIntentForPackage(packageNames[i])
            launchIntent?.let { startActivity(it) }
        }

        val browserBtn = findViewById<Button>(R.id.btn_browser)
        browserBtn.setOnClickListener { _ ->
            val i = appNames.indexOf("Chrome")
            val launchIntent = pm.getLaunchIntentForPackage(packageNames[i])
            launchIntent?.let { startActivity(it) }
        }
    }
}