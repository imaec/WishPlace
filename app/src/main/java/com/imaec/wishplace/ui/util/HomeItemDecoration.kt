package com.imaec.wishplace.ui.util

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.TYPE_CATEGORY
import com.imaec.wishplace.utils.Utils.Companion.dp

class HomeItemDecoration(var context: Context) : RecyclerView.ItemDecoration() {

    private val offset1 = dp(context, 6)
    private val offset2 = dp(context, 12)
    private val offset3 = dp(context, 18)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        val layoutManager = parent.layoutManager

        parent.adapter?.let {
            if (it.getItemViewType(position) == TYPE_CATEGORY) {
                // Category Offset
                outRect.top = offset3
                outRect.left = offset2
                outRect.right = offset2
                outRect.bottom = offset1
            } else {
                // Item Offset
                if (layoutManager is GridLayoutManager) {
                    val spanIndex = layoutManager.spanSizeLookup.getSpanIndex(position, 2)
                    outRect.left = if (spanIndex == 0) { offset2 } else { offset1 }
                    outRect.right = if (spanIndex == 0) { offset1 } else { offset2 }
                } else {
                    outRect.left = offset1
                    outRect.right = offset1
                }
                outRect.bottom = offset2
            }
        }
    }
}