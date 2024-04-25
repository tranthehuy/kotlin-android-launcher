package com.example.mylauncher00h21

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager


// Since this is an object collection, use a FragmentStatePagerAdapter,
// NOT a FragmentPagerAdapter.
class PagePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int  = 2

    override fun getItem(i: Int): Fragment {
        if (i % 2 == 0) {
            return HomeFragment()
        }
        val fragment = ListAppFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, i + 1)
        }
        return fragment
    }
    override fun getPageTitle(position: Int): CharSequence {
        return "Page ${(position + 1)}"
    }
}

private const val ARG_OBJECT = "page"

