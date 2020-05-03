package com.ahmedesmail111.chaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ahmedesmail111.chaty.mainwindow.ChatFragment
import com.ahmedesmail111.chaty.mainwindow.FriendsFragment
import com.ahmedesmail111.chaty.mainwindow.MoreFragment
import com.google.android.gms.common.api.Api
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val chatFragment = ChatFragment()
    private val friendsFragment = FriendsFragment()
    private val moreFragment = MoreFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation_main_activity.setOnNavigationItemSelectedListener(this)
        setFragment(chatFragment)


    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.coordinatorLayout_main_activity, fragment)
        fragmentTransaction.commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_chat_item -> {
                setFragment(chatFragment)
                return true

            }
            R.id.menu_friend_item -> {
                setFragment(friendsFragment)
                return true
            }
            R.id.menu_more_item -> {

              setFragment(moreFragment)
                return true
            }else -> return  false
        }
    }
}