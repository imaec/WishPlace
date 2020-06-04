package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSettingBinding
import com.imaec.wishplace.ui.view.activity.CategoryEditActivity
import com.imaec.wishplace.ui.view.activity.LicenseActivity
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.viewmodel.SettingViewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private lateinit var viewModel: SettingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SettingViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SettingFragment
            viewModel = this@SettingFragment.viewModel
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.text_category_add -> {
                InputDialog(context!!).apply {
                    setOnAddClickListener {
                        viewModel.addCategory(it) { isSuccess ->
                            Toast.makeText(context,
                                if (isSuccess) { "'$it' " + context.getString(R.string.msg_category_added) } else "'$it' " + context.getString(R.string.msg_category_exist),
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }
                    show()
                }
            }
            R.id.text_category_edit -> {
                startActivity(Intent(context, CategoryEditActivity::class.java))
            }
            R.id.text_app_license -> {
                startActivity(Intent(context, LicenseActivity::class.java))
            }
            R.id.text_share -> {
                viewModel.share {
                    // Success
                }
            }
        }
    }
}