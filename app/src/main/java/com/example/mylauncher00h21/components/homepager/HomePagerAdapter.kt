package com.example.mylauncher00h21.components.homepager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.mylauncher00h21.fragments.HomeFragment
import com.example.mylauncher00h21.fragments.ListAppFragment


class HomePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = 2

    override fun getItem(i: Int): Fragment {
        if (i % 2 == 0) {
            return HomeFragment()
        }
//        val fragment = ListAppFragment()
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, i + 1)
//        }
        return ListAppFragment()
    }
    override fun getPageTitle(position: Int): CharSequence {
        return "Page ${(position + 1)}"
    }
}