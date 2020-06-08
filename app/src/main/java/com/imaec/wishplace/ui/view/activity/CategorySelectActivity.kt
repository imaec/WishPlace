package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.EXTRA_CATEGORY
import com.imaec.wishplace.EXTRA_CATEGORY_ID
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityCategorySelectBinding
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.viewmodel.CategorySelectViewModel

class CategorySelectActivity : BaseActivity<ActivityCategorySelectBinding>(R.layout.activity_category_select) {

    private lateinit var viewModel: CategorySelectViewModel
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(CategorySelectViewModel::class.java, categoryRepository)

        binding.apply {
            lifecycleOwner = this@CategorySelectActivity
            viewModel = this@CategorySelectActivity.viewModel
            recyclerCategory.layoutManager = this@CategorySelectActivity.layoutManager
        }

        viewModel.apply {
            addOnClickListener { it ->
                if (it is CategoryEntity) {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(EXTRA_CATEGORY_ID, it.categoryId)
                        putExtra(EXTRA_CATEGORY, it.category)
                    })
                    finish()
                }
            }
            selectCategory()
        }
    }

    private fun init() {
        categoryDao = AppDatabase.getInstance(this).categoryDao()
        categoryRepository = CategoryRepository.getInstance(categoryDao)
        layoutManager = LinearLayoutManager(this)
    }
}