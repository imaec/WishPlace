package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.imaec.wishplace.R
import com.imaec.wishplace.RESULT_DELETE
import com.imaec.wishplace.RESULT_EDIT
import com.imaec.wishplace.RESULT_WRITE
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityMainBinding
import com.imaec.wishplace.ui.view.fragment.HomeFragment
import com.imaec.wishplace.ui.view.fragment.SearchFragment
import com.imaec.wishplace.ui.view.fragment.SearchResultFragment
import com.imaec.wishplace.ui.view.fragment.SettingFragment
import com.imaec.wishplace.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    val fragmentHome = HomeFragment()
    val fragmentSearch = SearchFragment()
    val fragmentSearchResult = SearchResultFragment()
    val fragmentSetting = SettingFragment()

    private var isSearchResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(MainViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel =this@MainActivity.viewModel
            bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)
        }

        setFragment(fragmentHome)

        if (intent.action == Intent.ACTION_SEND) {
            receiveIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_WRITE) {
            fragmentHome.notifyItemAdded()
        }
    }

    override fun onBackPressed() {
        when {
            isSearchResult -> {
                isSearchResult = false
                setFragment(fragmentSearch)
            }
            binding.bottomNavigation.selectedItemId != R.id.navigation_home -> {
                binding.bottomNavigation.selectedItemId = R.id.navigation_home
            }
            else -> super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.navigation_home -> {
                binding.fab.show()
                fragmentHome
            }
            R.id.navigation_search -> {
                binding.fab.hide()
                isSearchResult = false
                fragmentSearch
            }
            R.id.navigation_setting -> {
                binding.fab.hide()
                fragmentSetting
            }
            else -> {
                binding.fab.hide()
                fragmentHome
            }
        }
        setFragment(fragment)
        return true
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> {
                startActivityForResult(Intent(this, WriteActivity::class.java), 0)
            }
            R.id.image_search -> {
                isSearchResult = true
                fragmentSearch.onClick(view)
            }
            R.id.text_category_add,
            R.id.text_category_edit,
            R.id.text_share -> {
                fragmentSetting.onClick(view)
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragment)
            .commitAllowingStateLoss()
    }

    private fun receiveIntent() {
        intent.type?.let {
            if (it == "text/plain") {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                startActivityForResult(Intent(this, WriteActivity::class.java).apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                }, 0)
            }
        }
    }
}
