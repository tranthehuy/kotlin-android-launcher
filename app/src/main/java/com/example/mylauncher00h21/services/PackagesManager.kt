package com.example.mylauncher00h21.services

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log

data class Package(val name: String, val id: String, val icon: Drawable)

class PackagesManager {
    companion object {
        val DEFAULT_APPS = arrayListOf(
            "com.google.android.dialer",
            "com.android.chrome",
            "com.google.android.apps.messaging",
            "com.android.camera2"
        )
        private const val pageLength = 10
        var pageCount = 1
        val packages = ArrayList<Package>()
        private var packageManager: android.content.pm.PackageManager? = null;

        fun init(pm: android.content.pm.PackageManager, mainIntent: Intent) {
            packageManager = pm
            packages.clear()
            for (app in pm.queryIntentActivities(mainIntent, 0)) {
                val str = app.loadLabel(pm).toString();
                Log.d("id", app.activityInfo.packageName.toString())
                packages.add(Package(
                    str,
                    app.activityInfo.packageName.toString(),
                    pm.getApplicationIcon(app.activityInfo.packageName.toString())
                ))
            }

            pageCount = calculatePageCount()
        }

        fun getAppId(id: String): Package? {
            return packages.find { p -> p.id == id }
        }

        fun getRecentApps(): ArrayList<Package> {
            val results = ArrayList<Package>()
            for (i in 0..3) {
                val idApp = DEFAULT_APPS[i]
                val app = PackagesManager.getAppId(idApp)
                if (app != null) {
                    results.add(app)
                }
            }
            return results
        }

        fun startAppByName(context: Context, name: String) {
            val p = packages.find { p -> p.name == name } ?: return
            val launchIntent = packageManager?.getLaunchIntentForPackage(p.id)
            launchIntent?.let { context.startActivity(it) }
        }

        fun startAppInfoByName(context: Context, name: String) {
            val p = packages.find { p -> p.name == name } ?: return
            val launchIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            launchIntent.addCategory(Intent.CATEGORY_DEFAULT)
            launchIntent.setData(Uri.parse("package:${p.id}"))
            launchIntent.let { context.startActivity(it) }
        }

        private fun calculatePageCount(): Int {
            val factor = (packages.size / pageLength)
            return if (packages.size % pageLength == 0) factor else factor+1
        }

        data class PaginationRecord(val startIndex: Int, val endIndex: Int)
        fun getPagination(pageIndex: Int): PaginationRecord {
            val start = pageIndex * 10;
            val max = (pageIndex + 1) * 10;
            val end = if (max > packages.size) packages.size else max
            return PaginationRecord(start, end)
        }
    }
}