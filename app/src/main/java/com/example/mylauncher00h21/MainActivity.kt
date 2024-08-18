package com.example.mylauncher00h21

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mylauncher00h21.adapters.IconPagerAdapter
import com.example.mylauncher00h21.services.PackagesManager
import com.example.mylauncher00h21.services.Preferences
import com.example.mylauncher00h21.services.WidgetsManager
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var showIcon = true
    private var isEditing = false
    private lateinit var widgetsManager: WidgetsManager
    private var widgetsLayout: LinearLayout? = null

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showIcon = !showIcon
        val paper = findViewById<LinearLayout>(R.id.wrapperPaper)
        val iconList = findViewById<LinearLayout>(R.id.wrapperLayout)
        if (showIcon) {
            paper.visibility = VISIBLE
            iconList.visibility = INVISIBLE
            onChangeEditMode(false)
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

            if (shouldAdd && (data != null)) {
                onCreateWidget(data)
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

        initSettings()
        initPackageManager()
        initWidgetManager()
        initBottomBar()
        initViewPager()
    }

    private fun initSettings() {
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener { _ ->
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val background = Preferences.loadPreferences(this.applicationContext, "wallpaper");
        background?.let {
            try {
                val drawable = Drawable.createFromPath(background)
                val layout = findViewById<ImageView>(R.id.bgView)
                layout.setImageDrawable(drawable)
            } finally {
            }
        }

        val homepage = Preferences.loadPreferences(this.applicationContext, "homepage");
        homepage?.let {
            try {
                val maxIcons = homepage.toInt()
                if (maxIcons > 0) {
                    PackagesManager.pageLength = maxIcons
                }
            } catch (_: Exception) {}
        }
    }

    private fun initBottomBar() {
        val bar = findViewById<LinearLayout>(R.id.bottomBarLayout)
        val apps = PackagesManager.getRecentApps()
        for (i in 0 until apps.size) {
            val app = apps[i]
            val bottomIcon = ImageButton(this)
            bottomIcon.background = app.icon
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(10, 0, 10, 0)
            bottomIcon.setOnClickListener{_ ->
                PackagesManager.startAppByName(this, app.name)
            }
            bar.addView(bottomIcon, layoutParams)
        }
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

    private fun removeWidgetAndSave(index: Int) {
        val widgets = getWidgetIds()
        widgets.removeAt(index)
        saveWidgetIds(widgets)
    }

    private fun onChangeEditMode(mode: Boolean) {
        isEditing = mode
        renderWidgets()
    }

    private fun onCreateWidget(data: Intent) {
        val newId = widgetsManager.createWidget(data)
        val widgets = getWidgetIds()
        widgets.add(newId)
        saveWidgetIds(widgets)
        onChangeEditMode(false)
    }

    private fun onTouchAddButton() {
        val pickIntent = widgetsManager.getWidgetIntent()
        startActivityForResult(pickIntent, widgetsManager.REQUEST_PICK_APPWIDGET)
    }

    private fun onTouchRemoveButton(index: Int) {
        removeWidgetAndSave(index)
        onChangeEditMode(false)
    }

    private fun renderWidgets () {
        val widgets = getWidgetIds()
        if (isEditing) {
            widgetsManager.renderEditWidgets(widgets) { index: Int ->
                if (index == -1) {
                    onTouchAddButton()
                } else {
                    onTouchRemoveButton(index)
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
            onChangeEditMode(isEditing)
        }
    }
}