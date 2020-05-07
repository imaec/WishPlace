package com.imaec.wishplace.ui.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.utils.Utils.Companion.dp

class HomeItemDecoration(var context: Context) : RecyclerView.ItemDecoration() {

    private val offset1 = dp(context, 6)
    private val offset2 = dp(context, 12)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildLayoutPosition(view) % 5 == 0) {
            // Category Offset
            outRect.top = offset1
        } else {
            // Item Offset
            outRect.left = offset1
            outRect.right = offset1
            outRect.bottom = offset2
        }
    }
}