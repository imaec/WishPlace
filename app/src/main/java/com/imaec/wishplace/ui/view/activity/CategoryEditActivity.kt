package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.CategoryUpdateResult
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityCategoryBinding
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.viewmodel.CategoryViewModel

class CategoryEditActivity : BaseActivity<ActivityCategoryBinding>(R.layout.activity_category) {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var categoryDao: CategoryDao
    private lateinit var placeDao: PlaceDao
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var placeRepository: PlaceRepository
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(CategoryViewModel::class.java, categoryRepository, placeRepository)

        binding.apply {
            lifecycleOwner = this@CategoryEditActivity
            viewModel = this@CategoryEditActivity.viewModel
            recyclerCategory.layoutManager = this@CategoryEditActivity.layoutManager
        }

        viewModel.apply {
            addOnClickListener { it ->
                if (it is CategoryEntity) update(it)
            }
            addOnDeleteClickListener { entity -> delete(entity) }
            selectCategory()
        }
    }

    private fun init() {
        categoryDao = AppDatabase.getInstance(this).categoryDao()
        placeDao = AppDatabase.getInstance(this).placeDao()
        categoryRepository = CategoryRepository.getInstance(categoryDao)
        placeRepository = PlaceRepository.getInstance(placeDao)
        layoutManager = LinearLayoutManager(this)
    }

    private fun update(entity: CategoryEntity) {
        InputDialog(this).apply {
            setTitle(getString(R.string.category_edit))
            setOk(getString(R.string.edit))
            setOnAddClickListener { category ->
                viewModel.update(entity, category) { result ->
                    dismiss()

                    Toast.makeText(this@CategoryEditActivity, result.msg, Toast.LENGTH_SHORT).show()
                    if (result == CategoryUpdateResult.SUCCESS) {
                        viewModel.selectCategory()
                    } else if (result == CategoryUpdateResult.FAIL) {
                        retry(entity)
                    }
                }
            }
            show()
        }
    }

    private fun retry(entity: CategoryEntity) {
        CommonDialog(this, getString(R.string.msg_category_edit_fail)).apply {
            setOk(getString(R.string.retry))
            setOnOkClickListener(View.OnClickListener {
                viewModel.updatePlace(entity) { result ->
                    Toast.makeText(this@CategoryEditActivity, result.msg, Toast.LENGTH_SHORT).show()
                    if (result == CategoryUpdateResult.SUCCESS) {
                        viewModel.selectCategory()
                    } else if (result == CategoryUpdateResult.FAIL) {
                        retry(entity)
                    }
                }
            })
            show()
        }
    }

    private fun delete(entity: CategoryEntity) {
        CommonDialog(this@CategoryEditActivity, getString(R.string.msg_category_delete)).apply {
            setOnOkClickListener(View.OnClickListener {
                // Category 삭제
                viewModel.delete(entity) { isSuccess ->
                    dismiss()
                    if (isSuccess) {
                        Toast.makeText(this@CategoryEditActivity, R.string.msg_category_delete_success, Toast.LENGTH_SHORT).show()
                        viewModel.selectCategory()
                    } else {
                        Toast.makeText(this@CategoryEditActivity, R.string.msg_category_delete_fail, Toast.LENGTH_SHORT).show()
                    }
                }
            })
            show()
        }
    }
}