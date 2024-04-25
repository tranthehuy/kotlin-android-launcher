package com.example.mylauncher00h21

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import android.provider.Settings

class HomeFragment : Fragment() {

    private val appNames: ArrayList<String> = ArrayList<String>()
    private val packageNames: ArrayList<String> = ArrayList<String>()
    private lateinit var listView: ListView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {



        return inflater.inflate(R.layout.first_page, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
