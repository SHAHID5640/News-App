package com.example.bottomnavigationbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return 6
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return AllFragment()
            }
            1 -> {
                return SportsFragment()
            }
            2 -> {
                return HealthFragment()
            }
            3 -> {
                return ScienceFragment()
            }
            4 -> {
                return EntertainmentFragment()
            }
            5 -> {
                return TechnologyFragment()
            }
            else -> {
                return AllFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "All"
            }
            1 -> {
                return "Sports"
            }
            2 -> {
                return "Health"
            }
            3 -> {
                return "Science"
            }
            4 -> {
                return "Entertainment"
            }
            5 -> {
                return "Technology"
            }
        }
        return super.getPageTitle(position)
    }

}

