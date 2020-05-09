package com.imaec.wishplace.ui.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.imaec.wishplace.R
import kotlinx.android.synthetic.main.dialog_input.*

class InputDialog(context: Context) : Dialog(context) {

    private lateinit var addClick: (String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input)

        val size = Point()
        window?.windowManager?.defaultDisplay?.getSize(size)
        window?.apply {
            attributes = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = (size.x * 0.8).toInt()
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        if (::addClick.isInitialized) {
            text_add.setOnClickListener {
                if (edit_category.text.isEmpty()) {
                    Toast.makeText(context, R.string.msg_category_empty, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                addClick(edit_category.text.toString())
            }
        }
        text_cancel.setOnClickListener {
            dismiss()
        }
    }

    fun setOnAddClickListener(addClick: (String) -> Unit) {
        this.addClick = addClick
    }
}