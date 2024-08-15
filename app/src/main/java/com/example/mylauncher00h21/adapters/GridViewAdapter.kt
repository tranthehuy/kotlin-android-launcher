package com.example.mylauncher00h21.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.mylauncher00h21.R
import com.example.mylauncher00h21.services.Package

class GridViewAdapter (private val activity: FragmentActivity, private val items: List<Package>) :
    BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = if (convertView == null) {
            val vi = activity.layoutInflater
            vi.inflate(R.layout.gridview_item, null)
        } else {
            convertView
        }

        val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)

        val item = items[position]

        iconImageView.setImageDrawable(item.icon)
        titleTextView.text = item.name

        return view
    }


}