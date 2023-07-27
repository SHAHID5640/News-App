package com.example.bottomnavigationbar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var v: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, null)
        tabLayout = v.findViewById(R.id.tabs)
        viewPager = v.findViewById(R.id.viewpager)
        viewPager.adapter = PagerAdapter(parentFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        return v
    }
}