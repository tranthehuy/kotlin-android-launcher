package com.example.mylauncher00h21.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.mylauncher00h21.R
import com.example.mylauncher00h21.adapters.GridViewAdapter
import com.example.mylauncher00h21.services.PackagesManager

class PackageListFragment(private val pageIndex: Int): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.view_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val gridView: GridView = view.findViewById<GridView>(R.id.gridViewApps)
        val items = PackagesManager.packages

        val pagination = PackagesManager.getPagination(pageIndex)
        val renderItems = items.subList(pagination.startIndex, pagination.endIndex)

        val adapter = activity?.let {GridViewAdapter(it, renderItems)}
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            context?.let { PackagesManager.startAppByName(it, items[position].name) }
        }

        gridView.setOnItemLongClickListener { _, _, position, _ ->
            context?.let {
                PackagesManager.startAppInfoByName(it, items[position].name)
            }

            true
        }

    }
}
