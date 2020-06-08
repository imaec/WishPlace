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
import kotlinx.android.synthetic.main.dialog_common.*
import kotlinx.android.synthetic.main.dialog_input.*
import kotlinx.android.synthetic.main.dialog_input.text_cancel

class InputDialog(context: Context) : Dialog(context) {

    private var title = ""
    private var content = ""
    private var ok = ""
    private var cancel = ""
    private lateinit var addClick: (String) -> Unit
    private lateinit var cancelClick: () -> Unit

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

        if (title.isEmpty()) title = context.getString(R.string.add)
        if (ok.isEmpty()) ok = context.getString(R.string.ok)
        if (cancel.isEmpty()) cancel = context.getString(R.string.cancel)

        initLayout()
    }

    private fun initLayout() {
        text_title.text = title
        edit_content.setText(content)
        edit_content.setSelection(content.length)
        text_add.text = ok
        text_cancel.text = cancel

        if (::addClick.isInitialized) {
            text_add.setOnClickListener {
                if (edit_content.text.isEmpty()) {
                    Toast.makeText(context, R.string.msg_category_empty, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                addClick(edit_content.text.toString())
            }
        } else text_add.setOnClickListener { dismiss() }

        if (::cancelClick.isInitialized) {
            text_cancel.setOnClickListener {
                cancelClick()
            }
        } else text_cancel.setOnClickListener { dismiss() }
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setContent(content: String) {
        this.content = content
    }

    fun setOk(ok: String) {
        this.ok = ok
    }

    fun setCancel(cancel: String) {
        this.cancel = cancel
    }

    fun setOnAddClickListener(addClick: (String) -> Unit) {
        this.addClick = addClick
    }

    fun setOnCancelClickListener(cancelClick: () -> Unit) {
        this.cancelClick = cancelClick
    }
}