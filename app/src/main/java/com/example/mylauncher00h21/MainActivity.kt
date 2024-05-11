package com.example.mylauncher00h21

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.mylauncher00h21.components.homepager.HomePagerAdapter


class MainActivity : AppCompatActivity() {

//    private val appNames: ArrayList<String> = ArrayList<String>()
//    private val packageNames: ArrayList<String> = ArrayList<String>()

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

//        val mainIntent = Intent(Intent.ACTION_MAIN, null)
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//        val pm = this.applicationContext.packageManager
//        val pkgAppsList = pm.queryIntentActivities(mainIntent, 0)
//
//        for (app in pkgAppsList) {
//            val str = app.loadLabel(pm)
//            // appNames.add((appNames.size + 1).toString() + ". " + str.toString());
//            appNames.add(str.toString())
//            packageNames.add(app.activityInfo.packageName.toString())
//        }

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

                val imageView = this@MainActivity.findViewById<ImageView>(R.id.imageView)
                val textView = this@MainActivity.findViewById<TextView>(R.id.tvBranding)

//                val textView2 = this@MainActivity.findViewById<TextView>(R.id.tvBorder)

                if (imageView != null) {
                    if (position == 1) {
                        imageView.alpha = 1.0f;
                        val colorAnim = ObjectAnimator.ofFloat(
                            imageView, "alpha", 1.0f, 0.25f
                        ).setDuration(1000);
                        colorAnim.start();
                    } else {
                        imageView.alpha = 0f;
                        val colorAnim = ObjectAnimator.ofFloat(
                            imageView, "alpha", 0.25f, 1.0f
                        ).setDuration(500);
                        colorAnim.start();

                        textView.alpha = 1.0f;
                        val colorAnim2 = ObjectAnimator.ofFloat(
                            textView, "alpha", 1.0f, 0f
                        ).setDuration(2000);
                        colorAnim2.start();

//                        val scale = AnimationUtils.loadAnimation(applicationContext, R.anim.fadeout)
//                        textView2.clearAnimation();
//                        textView2.startAnimation(scale);

                    }

//                    textView.setTextColor(Color.WHITE);
//                    val colorAnim = ObjectAnimator.ofInt(textView, "textColor",
//                        Color.WHITE, Color.BLUE);
//                    colorAnim.setEvaluator(ArgbEvaluator());
//                    colorAnim.startDelay = 500;
//                    colorAnim.start();
//
//                    colorAnim.doOnEnd {
//                        Log.d("end", "test")
//                    }
//
//
//                    val aniSlide: Animation =
//                        AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
//
//                    textView.clearAnimation();
//                    textView.startAnimation(aniSlide);

                }
            }
            override fun onPageScrollStateChanged(state: Int) {

            }
        })

//        val callBtn = findViewById<Button>(R.id.btn_call)
//        callBtn.setOnClickListener { _ ->
//            val i = appNames.indexOf("Contacts")
//            val launchIntent = pm.getLaunchIntentForPackage(packageNames[i])
//            launchIntent?.let { startActivity(it) }
//        }
//
//        val msgBtn = findViewById<Button>(R.id.btn_message)
//        msgBtn.setOnClickListener { _ ->
//            val i = appNames.indexOf("Messages")
//            val launchIntent = pm.getLaunchIntentForPackage(packageNames[i])
//            launchIntent?.let { startActivity(it) }
//        }
//
//        val browserBtn = findViewById<Button>(R.id.btn_browser)
//        browserBtn.setOnClickListener { _ ->
//            val i = appNames.indexOf("Chrome")
//            val launchIntent = pm.getLaunchIntentForPackage(packageNames[i])
//            launchIntent?.let { startActivity(it) }
//        }
    }

}