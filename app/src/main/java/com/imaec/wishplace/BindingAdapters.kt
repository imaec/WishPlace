package com.imaec.wishplace

import android.text.Html
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.utils.Utils

object BindingAdapters {

    private val TAG = this::class.java.simpleName

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        recyclerView.adapter = adapter
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
        Glide.with(imageView)
            .load(imgUrl)
            .error(R.drawable.img_empty)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("isVisit")
    fun setIsVisit(imageView: ImageView, isVisit: Boolean) {
        Glide.with(imageView)
            .load(if (isVisit) R.drawable.ic_visit else R.drawable.ic_novisit)
            .error(R.drawable.img_empty)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("date")
    fun setDate(textView: TextView, strDate: String) {
        textView.text = Utils.getDateChangeFormat(strDate, "yyyyMMddHHmmss", "yyyy.MM.dd")
    }

    @JvmStatic
    @BindingAdapter("html")
    fun setHtml(textView: TextView, text: String) {
        textView.text = Html.fromHtml(text)
    }

    @JvmStatic
    @BindingAdapter(value = ["bind:highlight", "bind:option"], requireAll = true)
    fun setHighlight(textView: TextView, keyword: String?, option: String?) {
        Log.d(TAG, "    ## 1 desc : ${textView.contentDescription}, option : $option")
        if (textView.contentDescription != option) {
            // 주소/이름에 같은 단어가 들어갔을때 주소/이름을 번갈아 검색하면
            // 주소/이름에 모두 하이라이트 되는 현상때문에 평문으로 대치
            val content = textView.text.toString()
            textView.text = content
            return
        }
        Log.d(TAG, "    ## 2 desc : ${textView.contentDescription}, option : $option")

        keyword?.let {
            val content = textView.text.toString()
            val spannableString = SpannableString(content)
            var lastIndex = 0

            while (true) {
                if (content.indexOf(keyword, lastIndex) == -1) break

                val start = content.indexOf(keyword, lastIndex)
                val end = start + keyword.length

                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(textView.context, R.color.colorAccent)), start, end, 0)

                lastIndex = end
            }
            textView.text = spannableString
        }
    }
}