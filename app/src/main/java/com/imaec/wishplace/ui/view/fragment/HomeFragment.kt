package com.imaec.wishplace.ui.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentHomeBinding
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(HomeViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
        }

        viewModel.apply {
            addOnClickListener {
                Toast.makeText(context, it.title, Toast.LENGTH_SHORT).show()
            }
            addOnLongClickListener {
                val dialog = EditDialog(context!!).apply {
                    setTitle(it.title)
                    setOnEditClickListener(View.OnClickListener {
                        dismiss()
                    })
                    setOnDeleteClickListener(View.OnClickListener {
                        dismiss()
                    })
                }
                dialog.show()
            }
        }
    }
}