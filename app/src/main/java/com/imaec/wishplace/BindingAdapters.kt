package com.imaec.wishplace

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.ui.adapter.HomeAdapter
import com.imaec.wishplace.utils.Utils

object BindingAdapters {

    private val TAG = this::class.java.simpleName

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("layoutManager")
    fun setLayoutManager(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        if (recyclerView.id == R.id.recycler_home) {
            (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((recyclerView.adapter as HomeAdapter).getItem(position) is CategoryEntity) {
                        2
                    } else {
                        1
                    }
                }
            }
        }
        recyclerView.layoutManager = layoutManager
    }

    @JvmStatic
    @BindingAdapter("itemDecoration")
    fun setItemDecoration(recyclerView: RecyclerView, itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(itemDecoration)
    }

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: ArrayList<Any>) {
        (recyclerView.adapter as BaseAdapter).apply {
            clearItem()
            addItems(items)
            notifyDataSetChanged()
        }
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun isVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("imgUrl")
    fun setImgUrl(imageView: ImageView, imgUrl: String) {
        Glide.with(imageView).load(imgUrl).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("date")
    fun setDate(textView: TextView, strDate: String) {
        textView.text = Utils.getDateChangeFormat(strDate, "yyyyMMddHHmmss", "yyyy.MM.dd")
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:highlight", "bind:option"], requireAll = true)
    fun setHighlight(textView: TextView, keyword: String?, option: String?) {
        if (textView.contentDescription != option) return

        keyword?.let {
            val content = textView.text.toString()
            val spannableString = SpannableString(content)
            var lastIndex = 0

            while (true) {
                if (content.indexOf(keyword, lastIndex) == -1) break

                val start = content.indexOf(keyword, lastIndex)
                val end = start + keyword.length

                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#2196F3")), start, end, 0)

                lastIndex = end
            }
            textView.text = spannableString
        }
    }
}