package com.example.mylauncher00h21.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

import com.example.mylauncher00h21.fragments.PackageListFragment

class IconPagerAdapter(fm: FragmentManager, private val pageCount: Int) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = pageCount

    override fun getItem(i: Int): Fragment {
        return PackageListFragment(i)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Page ${(position + 1)}"
    }
}