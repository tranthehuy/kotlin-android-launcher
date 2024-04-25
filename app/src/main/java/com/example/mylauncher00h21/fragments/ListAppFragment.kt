package com.example.mylauncher00h21.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.mylauncher00h21.R
import com.example.mylauncher00h21.components.applist.AppListAdapter

class ListAppFragment : Fragment() {

    private val appNames: ArrayList<String> = ArrayList<String>()
    private val packageNames: ArrayList<String> = ArrayList<String>()
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.app_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pm = view.context.packageManager
        val pkgAppsList = pm.queryIntentActivities(mainIntent, 0)

        for (app in pkgAppsList) {
            val str = app.loadLabel(pm)
            // appNames.add((appNames.size + 1).toString() + ". " + str.toString());
            appNames.add(str.toString())
            packageNames.add(app.activityInfo.packageName.toString())
        }

        listView = view.findViewById<ListView>(R.id.list)

        val adapter = activity?.let { AppListAdapter(it, appNames) }

        if (activity == null) return

        listView.setAdapter(adapter)

        listView.setOnItemClickListener { _, _, position, _ ->
            val launchIntent = pm.getLaunchIntentForPackage(packageNames[position])
            launchIntent?.let { startActivity(it) }
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val launchIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            launchIntent.addCategory(Intent.CATEGORY_DEFAULT)
            val packageName = packageNames[position]
            launchIntent.setData(Uri.parse("package:$packageName"))
            startActivity(launchIntent)
            true
        }
    }
}
