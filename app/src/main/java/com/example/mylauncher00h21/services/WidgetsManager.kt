package com.example.mylauncher00h21.services

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.example.mylauncher00h21.R

class WidgetsManager(private val context: Context, private val widgetsLayout: LinearLayout) {
    private val APPWIDGET_HOST_ID = 2048
    val REQUEST_PICK_APPWIDGET = 0
    val REQUEST_CREATE_APPWIDGET = 5

    private var appWidgetManager: AppWidgetManager? = null
    private var appWidgetHost: AppWidgetHost? = null

    fun renderViewWidgets(widgets: List<Int>) {
        widgetsLayout.removeAllViews()
        widgets.forEach { w -> addWidgetIntoView(w) }

    }

    private fun addRemoveWidgetButton(index: Int, onChange: (i: Int) -> Unit) {
        val rmButton = Button(context)
        rmButton.text = context.getString(R.string.remove)
        rmButton.setOnClickListener { _ -> onChange(index) }
        widgetsLayout.addView(rmButton)
    }

    fun renderEditWidgets(widgets: List<Int>, onChange: (i: Int) -> Unit) {
        widgetsLayout.removeAllViews()

        val addButton = Button(context)
        addButton.text = context.getString(R.string.add)
        addButton.setOnClickListener { _ -> onChange(-1) }
        widgetsLayout.addView(addButton)

        widgets.forEachIndexed { index, w ->
            addWidgetIntoView(w)
            addRemoveWidgetButton(index, onChange)
        }
    }

     fun getWidgetIntent(): Intent {
        val appWidgetId = appWidgetHost?.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        addEmptyData(pickIntent)
        return pickIntent
    }

    // For some reason you have to add this empty data, else it won't work
    private fun addEmptyData(pickIntent: Intent) {
        val customInfo = ArrayList<AppWidgetProviderInfo>()
        pickIntent.putParcelableArrayListExtra(
            AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo
        )
        val customExtras = ArrayList<Bundle>()
        pickIntent.putParcelableArrayListExtra(
            AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras
        )
    }

    fun getSubWidgetIntent(data: Intent?): Intent? {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = appWidgetManager!!.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.setComponent(appWidgetInfo.configure)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            return intent
        }
        return null
    }

    fun createWidget(data: Intent?): Int {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        addWidgetIntoView(appWidgetId)
        return appWidgetId
    }

    private fun addWidgetIntoView(appWidgetId: Int) {
        val appWidgetInfo = appWidgetManager!!.getAppWidgetInfo(appWidgetId)
        val hostView = appWidgetHost!!.createView(context.applicationContext, appWidgetId, appWidgetInfo)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        hostView.minimumHeight = 300
        hostView.minimumWidth = 300
        widgetsLayout.addView(hostView)
    }

    fun onStart() {
        appWidgetManager = AppWidgetManager.getInstance(context.applicationContext)
        appWidgetHost = AppWidgetHost(context.applicationContext, APPWIDGET_HOST_ID)
        appWidgetHost?.startListening()
    }

    fun onStop() {
        appWidgetHost?.stopListening()
    }
}