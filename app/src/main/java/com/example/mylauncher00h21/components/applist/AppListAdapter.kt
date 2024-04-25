package com.example.mylauncher00h21.components.applist

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.mylauncher00h21.R


class AppListAdapter(private val activity: FragmentActivity, private val items: ArrayList<String>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): Any {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val customView: View = if (view == null) {
            val vi = activity.layoutInflater
            vi.inflate(R.layout.app_row, null)
        } else {
            view
        }

        val tvName = customView.findViewById<TextView>(R.id.app_name)
        tvName.text = items[i]

//        val rnd = Random
//        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
//        tvName.setTextColor(color)
//        tvName.setShadowLayer(1.0f,1.0f,1.0f, Color.WHITE)
        return customView
    }
}