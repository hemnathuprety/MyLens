package np.com.hemnath.mylens.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import np.com.hemnath.mylens.ui.PlacesFragment
import np.com.hemnath.mylens.ui.SearchFragment
import np.com.hemnath.mylens.ui.ShoppingFragment
import np.com.hemnath.mylens.ui.TextSearchFragment
import np.com.hemnath.mylens.ui.TranslateFragment

class LensPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity){

    private val list =
        mutableListOf("Translate", "Text", "Search", "Shopping", "Places")

     private fun getItem(i: Int): Fragment {
        val fragment =
            when (i) {
                0 -> TranslateFragment()
                1 -> TextSearchFragment()
                2 -> SearchFragment()
                3 -> ShoppingFragment()
                else -> PlacesFragment()
            }
        fragment.arguments = Bundle().apply {}
        return fragment
    }

    fun getTabTitle(position : Int): String{
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return getItem(position)
    }

}




