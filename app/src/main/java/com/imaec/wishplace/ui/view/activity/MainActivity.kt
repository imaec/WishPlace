package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.imaec.wishplace.EXTRA_ADDRESS
import com.imaec.wishplace.EXTRA_TITLE
import com.imaec.wishplace.R
import com.imaec.wishplace.RESULT_WRITE
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityMainBinding
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.ui.view.fragment.HomeFragment
import com.imaec.wishplace.ui.view.fragment.SearchFragment
import com.imaec.wishplace.ui.view.fragment.SearchResultFragment
import com.imaec.wishplace.ui.view.fragment.SettingFragment
import com.imaec.wishplace.utils.AdRemoveHandler
import com.imaec.wishplace.utils.BackPressHandler
import com.imaec.wishplace.viewmodel.MainViewModel


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var activeFragment: Fragment

    private val backPressHandler = BackPressHandler(this)
    private var adRemoveHandler = AdRemoveHandler(this)

    private val fragmentHome = HomeFragment()
    private val fragmentSearch = SearchFragment()
    val fragmentSearchResult = SearchResultFragment()
    private val fragmentSetting = SettingFragment()

    var isSearchResult = false
    var isDataChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startIntro()
        init()

        viewModel = getViewModel(MainViewModel::class.java, categoryRepository)

        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel =this@MainActivity.viewModel
            bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)
        }

        setFragment(fragmentHome)

        viewModel.isExistCategory { isExist -> if (!isExist) addCategory() }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            when (intent.action) {
                Intent.ACTION_SEND -> receiveIntent(intent)
                Intent.ACTION_VIEW -> receiveKakaoIntent(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_WRITE) {
            fragmentHome.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        when {
            isSearchResult -> {
                isSearchResult = false
                setFragment(fragmentSearch)

                if (binding.bottomNavigation.selectedItemId != R.id.navigation_home &&
                    binding.bottomNavigation.selectedItemId != R.id.navigation_search) {
                    binding.bottomNavigation.selectedItemId = R.id.navigation_home
                } else if (binding.bottomNavigation.selectedItemId == R.id.navigation_home) {
                    backPressHandler.onBackPressed()
                }
            }
            binding.bottomNavigation.selectedItemId != R.id.navigation_home -> {
                binding.bottomNavigation.selectedItemId = R.id.navigation_home
            }
            else -> backPressHandler.onBackPressed()
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
            R.id.image_logo -> {
                adRemoveHandler.onClick()
            }
            R.id.linear_option,
            R.id.image_search -> {
                fragmentSearch.onClick(view)
            }
            R.id.text_category_add,
            R.id.text_category_edit,
            R.id.text_app_license,
            R.id.text_share -> {
                fragmentSetting.onClick(view)
            }
        }
    }

    private fun startIntro() {
        startActivity(Intent(this, IntroActivity::class.java))
        Handler().postDelayed({
            binding.viewTemp.visibility = View.GONE
        }, 1000)
    }

    private fun init() {
        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentSearchResult, getString(R.string.search_result)).hide(fragmentSearchResult).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentSearch, getString(R.string.search)).hide(fragmentSearch).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentSetting, getString(R.string.setting)).hide(fragmentSetting).commit()
        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentHome, getString(R.string.home)).commit()
        activeFragment = fragmentHome

        categoryDao = AppDatabase.getInstance(this).categoryDao()
        categoryRepository = CategoryRepository.getInstance(categoryDao)
    }

    private fun addCategory() {
        InputDialog(this).apply {
            setTitle(getString(R.string.msg_category_add))
            setOnAddClickListener {
                viewModel.addCategory(it) {
                    Toast.makeText(this@MainActivity, "'$it' " + getString(R.string.msg_category_added), Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
            setOnCancelClickListener {
                dismiss()
                Toast.makeText(this@MainActivity, R.string.msg_category_add_canceled, Toast.LENGTH_SHORT).show()
            }
            show()
        }
    }

    private fun receiveIntent(intent: Intent) {
        intent.type?.let {
            if (it == "text/plain") {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                startActivityForResult(Intent(this, WriteActivity::class.java).apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                }, 0)
            }
        }
    }

    private fun receiveKakaoIntent(intent: Intent) {
        intent.data?.let {
            if (it.host == getString(R.string.kakaolink_host)) {
                if (it.query.isNullOrEmpty()) return

                startActivityForResult(Intent(this, WriteActivity::class.java).apply {
                    putExtra(Intent.EXTRA_TEXT, it.getQueryParameter("site"))
                    putExtra(EXTRA_TITLE, it.getQueryParameter("name"))
                    putExtra(EXTRA_ADDRESS, it.getQueryParameter("addr"))
                }, 0)
            }
        }
    }

    fun setFragment(fragment: Fragment) {
        updateStatusBarColor(ContextCompat.getColor(this, R.color.white))
        when(fragment) {
            is HomeFragment -> {
                adRemoveHandler = AdRemoveHandler(this)
                if (isDataChanged) {
                    isDataChanged = false
                    fragmentHome.notifyDataSetChanged()
                }
            }
            is SearchFragment -> {
                fragment.getKeyword()
                updateStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            is SearchResultFragment -> fragment.search()
        }

        supportFragmentManager
            .beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }
}
