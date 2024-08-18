package com.example.mylauncher00h21

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mylauncher00h21.adapters.IconPagerAdapter
import com.example.mylauncher00h21.services.PackagesManager
import com.example.mylauncher00h21.services.Preferences
import com.example.mylauncher00h21.services.WidgetsManager

class MainActivity : AppCompatActivity() {
    private var showIcon = true
    private var isEditing = false
    private lateinit var widgetsManager: WidgetsManager
    private var widgetsLayout: LinearLayout? = null

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showIcon = !showIcon
        val paper = findViewById<ViewPager>(R.id.pager)
        val iconList = findViewById<LinearLayout>(R.id.wrapperLayout)
        if (showIcon) {
            paper.visibility = VISIBLE
            iconList.visibility = INVISIBLE
        } else {
            paper.visibility = INVISIBLE
            iconList.visibility = VISIBLE
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        var shouldAdd = false
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == widgetsManager.REQUEST_PICK_APPWIDGET) {
                val subIntent = widgetsManager.getSubWidgetIntent(data)
                if (subIntent != null) {
                    startActivityForResult(subIntent, widgetsManager.REQUEST_CREATE_APPWIDGET)
                } else {
                    shouldAdd = true
                }
            } else if (requestCode == widgetsManager.REQUEST_CREATE_APPWIDGET) {
                shouldAdd = true
            }

            if (shouldAdd) {
                val newId = widgetsManager.createWidget(data)
                val widgets = getWidgetIds()
                widgets.add(newId)
                saveWidgetIds(widgets)
                isEditing = false
                renderWidgets()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        widgetsManager.onStart()
        renderWidgets()
    }

    override fun onStop() {
        super.onStop()
        widgetsManager.onStop()
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
        initWidgetManager()
    }

    private fun getWidgetIds(): MutableList<Int> {
        val widgetPref = Preferences.loadPreferences(this, "widgets")
        var widgets = mutableListOf<Int>()
        if (widgetPref?.length!! > 0) {
            widgets = widgetPref.split(";").map{it.toInt()}.toMutableList()
        }
        return widgets
    }

    private fun saveWidgetIds(widgets: MutableList<Int>) {
        val widgetArray = widgets.joinToString(";") { it.toString() }
        Preferences.savePreferences(this, "widgets", widgetArray)
    }

    private fun removeWidgetAndSave(index: Int): MutableList<Int> {
        isEditing = false
        val widgets = getWidgetIds()
        widgets.removeAt(index)
        saveWidgetIds(widgets)
        return widgets
    }

    private fun renderWidgets () {
        val widgets = getWidgetIds()
        if (isEditing) {
            widgetsManager.renderEditWidgets(widgets) { index: Int ->
                if (index == -1) {
                    val pickIntent = widgetsManager.getWidgetIntent()
                    startActivityForResult(pickIntent, widgetsManager.REQUEST_PICK_APPWIDGET)
                } else {
                    val newList = removeWidgetAndSave(index)
                    widgetsManager.renderViewWidgets(newList)
                }
            }
        } else {
            widgetsManager.renderViewWidgets(widgets)
        }
    }


    private fun initWidgetManager() {
        widgetsLayout = findViewById(R.id.widgetHost)
        widgetsManager = WidgetsManager(this, widgetsLayout!!)

        val customBtn = findViewById<Button>(R.id.btnCustom)
        customBtn.setOnClickListener { _ ->
            isEditing = !isEditing
            renderWidgets()
        }
    }
}