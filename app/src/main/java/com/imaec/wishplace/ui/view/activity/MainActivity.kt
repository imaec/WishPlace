package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
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
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.ui.view.fragment.HomeFragment
import com.imaec.wishplace.ui.view.fragment.SearchFragment
import com.imaec.wishplace.ui.view.fragment.SearchResultFragment
import com.imaec.wishplace.ui.view.fragment.SettingFragment
import com.imaec.wishplace.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel: MainViewModel

    val fragmentHome = HomeFragment()
    val fragmentSearch = SearchFragment()
    val fragmentSearchResult = SearchResultFragment()
    val fragmentSetting = SettingFragment()

    var isSearchResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startIntro()

        viewModel = getViewModel(MainViewModel::class.java)

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
            fragmentHome.notifyItemAdded()
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
                    super.onBackPressed()
                }
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
            R.id.linear_option -> {
                fragmentSearch.onClick(view)
            }
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

    private fun setFragment(fragment: Fragment) {
        updateStatusBarColor(if (fragment is SearchFragment) ContextCompat.getColor(this, R.color.colorPrimary) else ContextCompat.getColor(this, R.color.white))
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragment)
            .commitAllowingStateLoss()
    }

    private fun addCategory() {
        InputDialog(this).apply {
            setTitle("카테고리 추가해주세요.")
            setOnAddClickListener {
                viewModel.addCategory(it) { isSuccess ->
                    Toast.makeText(this@MainActivity,
                        if (isSuccess) { "'$it' " + getString(R.string.msg_category_added) } else "'$it' " + context.getString(R.string.msg_category_exist),
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                }
            }
            setOnCancelClickListener {
                Toast.makeText(this@MainActivity, R.string.msg_category_add_canceled, Toast.LENGTH_SHORT).show()
                dismiss()
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
            if (it.host == "kakaolink") {
                if (it.query.isNullOrEmpty()) return

                startActivityForResult(Intent(this, WriteActivity::class.java).apply {
                    putExtra(Intent.EXTRA_TEXT, it.getQueryParameter("site"))
                    putExtra(EXTRA_TITLE, it.getQueryParameter("name"))
                    putExtra(EXTRA_ADDRESS, it.getQueryParameter("addr"))
                }, 0)
            }
        }
    }
}
