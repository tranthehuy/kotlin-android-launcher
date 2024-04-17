package com.example.mylauncher00h21

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private val appNames: ArrayList<String> = ArrayList<String>()
    private val packageNames: ArrayList<String> = ArrayList<String>()
    private lateinit var listView: ListView

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = this.applicationContext.packageManager
        val pkgAppsList = pm.queryIntentActivities(mainIntent, 0)

        for (app in pkgAppsList) {
            val str = app.loadLabel(pm)
            // appNames.add((appNames.size + 1).toString() + ". " + str.toString());
            appNames.add(str.toString());
            packageNames.add(app.activityInfo.packageName.toString())
        }

        listView = findViewById<ListView>(R.id.list)
        val arr: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.row,
            appNames
        )
        listView.setAdapter(arr)
        listView.setOnItemClickListener{ _, _, position, _ ->
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