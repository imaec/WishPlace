package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityEditBinding
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.viewmodel.EditViewModel

class EditActivity : BaseActivity<ActivityEditBinding>(R.layout.activity_edit) {

    private lateinit var viewModel: EditViewModel
    private lateinit var placeDao: PlaceDao
    private lateinit var placeRepository: PlaceRepository

    private var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(EditViewModel::class.java, placeRepository)

        binding.apply {
            lifecycleOwner = this@EditActivity
            viewModel = this@EditActivity.viewModel
            editName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveTitle.value = p0.toString()
                }
            })
            editAddr.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveAddress.value = p0.toString()
                }
            })
            editSite.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveSite.value = p0.toString()
                }
            })
            editContent.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveContent.value = p0.toString()
                }
            })
        }

        viewModel.apply {
            setData(
                intent.getStringExtra(EXTRA_CATEGORY) ?: "카테고리가 없습니다.",
                intent.getStringExtra(EXTRA_TITLE) ?: "제목이 없습니다.",
                intent.getStringExtra(EXTRA_ADDRESS) ?: "주소가 없습니다.",
                intent.getStringExtra(EXTRA_IMG_URL) ?: "",
                intent.getStringExtra(EXTRA_SITE_URL) ?: "사이트가 없습니다.",
                intent.getStringExtra(EXTRA_CONTENT) ?: "",
                intent.getBooleanExtra(EXTRA_IS_VISIT, false)
            )
            getPlace(intent.getIntExtra(EXTRA_PLACE_ID, 0))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            categoryId = data?.getIntExtra(EXTRA_CATEGORY_ID, 0) ?: 0
            val category = data?.getStringExtra(EXTRA_CATEGORY)
            viewModel.liveCategory.value = category
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.image_back -> {
                onBackPressed()
            }
            R.id.text_category -> {
                startActivityForResult(Intent(this, CategorySelectActivity::class.java), 0)
            }
            R.id.text_save -> {
                viewModel.livePlace.value?.let { entity ->
                    val category = viewModel.liveCategory.value ?: entity.category
                    val title = binding.editName.text.toString()
                    val address = binding.editAddr.text.toString()
                    val result = viewModel.validateData(category, title, address)
                    if (result == ValidateResult.SUCCESS) {
                        if (categoryId != 0) {
                            entity.foreignId = categoryId
                            entity.category = category
                        }
                        checkUrl(entity)
                    } else {
                        Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this@EditActivity, R.string.msg_edit_place_fail, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun init() {
        placeDao = AppDatabase.getInstance(this).placeDao()
        placeRepository = PlaceRepository.getInstance(placeDao)
    }

    private fun checkUrl(entity: PlaceEntity) {
        showProgress()

        viewModel.apply {
            entity.apply {
                name = liveTitle.value ?: "제목이 없습니다."
                address = liveAddress.value ?: "주소가 없습니다."
                siteUrl = liveSite.value ?: "사이트가 없습니다."
                content = liveContent.value ?: ""
            }

            checkUrl(entity.siteUrl, { imgUrl ->
                entity.imageUrl = imgUrl
                update(entity)
            }, { siteUrl ->
                CommonDialog(this@EditActivity, if (siteUrl == null) getString(R.string.msg_image_empty) else getString(R.string.msg_url_empty)).apply {
                    setOnOkClickListener(View.OnClickListener {
                        entity.imageUrl = ""
                        update(entity)
                        dismiss()
                    })
                    setOnCancelClickListener(View.OnClickListener {
                        hideProgress()
                        dismiss()
                    })
                    show()
                }
            })
        }
    }

    private fun update(entity: PlaceEntity) {
        viewModel.update(entity) { isSuccess ->
            hideProgress()

            if (isSuccess) {
                Toast.makeText(this@EditActivity, R.string.msg_edit_place_success, Toast.LENGTH_SHORT).show()
                setResult(RESULT_EDIT, Intent().apply {
                    putExtra(EXTRA_IS_UPDATED, true)
                })
                onBackPressed()
            } else {
                Toast.makeText(this@EditActivity, R.string.msg_edit_place_fail, Toast.LENGTH_SHORT).show()
            }
        }
    }
}