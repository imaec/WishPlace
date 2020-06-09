package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSettingBinding
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.ui.view.activity.CategoryEditActivity
import com.imaec.wishplace.ui.view.activity.LicenseActivity
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.utils.Utils
import com.imaec.wishplace.viewmodel.SettingViewModel
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private lateinit var viewModel: SettingViewModel
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel = getViewModel(SettingViewModel::class.java, categoryRepository)

        binding.apply {
            lifecycleOwner = this@SettingFragment
            viewModel = this@SettingFragment.viewModel
        }

        viewModel.setAppVersion(Utils.getVersion(context!!))
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
                KakaoLinkService.getInstance()
                    .sendCustom(context, context!!.getString(R.string.template_id_app), null, object : ResponseCallback<KakaoLinkResponse>() {
                        override fun onSuccess(result: KakaoLinkResponse?) {

                        }

                        override fun onFailure(errorResult: ErrorResult?) {
                            Log.e(TAG, errorResult.toString())
                            Toast.makeText(context, R.string.msg_share_fail, Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun init() {
        categoryDao = AppDatabase.getInstance(context!!).categoryDao()
        categoryRepository = CategoryRepository.getInstance(categoryDao)
    }
}