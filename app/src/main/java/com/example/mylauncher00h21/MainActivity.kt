package com.example.mylauncher00h21

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mylauncher00h21.adapters.IconPagerAdapter
import com.example.mylauncher00h21.services.PackagesManager

class MainActivity : AppCompatActivity() {
    private var showIcon = true

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showIcon = !showIcon
        val paper = findViewById<ViewPager>(R.id.pager)
        val iconList = findViewById<LinearLayout>(R.id.widgetsHostLayout)
        if (showIcon) {
            paper.visibility = VISIBLE
            iconList.visibility = INVISIBLE
        } else {
            paper.visibility = INVISIBLE
            iconList.visibility = VISIBLE
        }
    }

    private fun initPackageManager() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        PackagesManager.init(this.packageManager, mainIntent)
    }

    private fun initViewPager() {
        val pagerAdapter = IconPagerAdapter(supportFragmentManager, PackagesManager.pageCount)
        val viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = pagerAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_main)

        initPackageManager()

        initViewPager()
    }
}