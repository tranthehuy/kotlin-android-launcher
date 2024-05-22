package com.example.mylauncher00h21.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.mylauncher00h21.R
import com.example.mylauncher00h21.SettingActivity
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

        initAppListData(view, mainIntent);

        initSearchListener(view, mainIntent);
        initSettingsListener(view);
    }

    private fun initSettingsListener(view: View) {
        val settingButton = view.findViewById<Button>(R.id.setting_button)
        settingButton.setOnClickListener { _ ->
            val intent = Intent(this.context, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSearchListener(view: View, mainIntent: Intent) {
        val settingButton = view.findViewById<Button>(R.id.search_button)
        val editText = view.findViewById<EditText>(R.id.search_edit);
        settingButton.setOnClickListener { _ ->
            editText.showSoftInputOnFocus = true;
            editText.text.clear() // Clear the text
            editText.requestFocus() // Set focus to the EditText

        }

        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                // Handle the key press here
                when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {


                        val searchApp = editText.text.toString().lowercase()
                        reinitAppListData(view, mainIntent, searchApp)

                        true // Return true to consume the event
                    }
                    else -> false // Return false to let the system handle other keys
                }
            } else {
                false // Return false for other key actions (e.g., key release)
            }
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val searchApp = editText.text.toString().lowercase()
                reinitAppListData(view, mainIntent, searchApp)

                true // Return true to consume the event
            } else {

                false // Return false to let the system handle other actions
            }
        }
    }

    private fun reinitAppListData(view: View, mainIntent: Intent, searchApp: String) {
        initAppListData(view, mainIntent, searchApp)
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = activity?.currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initAppListData(view: View, mainIntent: Intent, filterName: String = "") {
        val pm = view.context.packageManager
        val pkgAppsList = pm.queryIntentActivities(mainIntent, 0)
        val sortedList = pkgAppsList.sortedBy { it.loadLabel(pm).toString() }

        appNames.clear()
        packageNames.clear()

        for (app in sortedList) {
            val str = app.loadLabel(pm).toString();
            if (str.lowercase().contains(filterName)) {
                appNames.add(str)
                packageNames.add(app.activityInfo.packageName.toString())
            }
        }
        listView = view.findViewById<ListView>(R.id.list)
        val adapter = activity?.let { AppListAdapter(it, appNames) }
        if (activity == null) return

        listView.setAdapter(adapter)

        listView.setOnItemClickListener { _, _, position, _ ->
            val launchIntent = pm.getLaunchIntentForPackage(packageNames[position])
            launchIntent?.let { startActivity(it) }
        }

//        listView.setOnItemLongClickListener { parent, view, position, id ->
//            val launchIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            launchIntent.addCategory(Intent.CATEGORY_DEFAULT)
//            val packageName = packageNames[position]
//            launchIntent.setData(Uri.parse("package:$packageName"))
//            startActivity(launchIntent)
//            true
//        }

    }
}
