package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityMainBinding
import com.imaec.wishplace.ui.view.fragment.HomeFragment
import com.imaec.wishplace.ui.view.fragment.SearchFragment
import com.imaec.wishplace.ui.view.fragment.SettingFragment
import com.imaec.wishplace.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    private val fragmentHome = HomeFragment()
    private val fragmentSearch = SearchFragment()
    private val fragmentSetting = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(MainViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel =this@MainActivity.viewModel
            bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragmentHome)
            .commitAllowingStateLoss()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = when (item.itemId) {
            R.id.navigation_home -> {
                binding.fab.show()
                fragmentHome
            }
            R.id.navigation_search -> {
                binding.fab.hide()
                fragmentSearch
            }
            R.id.navigation_setting -> {
                binding.fab.hide()
                fragmentSetting
            }
            else -> fragmentHome
        }
        transaction.replace(R.id.frame, fragment).commitAllowingStateLoss()
        return true
    }

    fun onClick(view: View) {
        if (view.id == R.id.fab) {
            startActivity(Intent(this, WriteActivity::class.java))
        }
    }
}
