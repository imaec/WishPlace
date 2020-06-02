package com.imaec.wishplace.ui.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.utils.Utils

class NaverPlaceItemDecoration(var context: Context) : RecyclerView.ItemDecoration() {

    private val offset1 = Utils.dp(context, 8)
    private val offset2 = Utils.dp(context, 16)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Item Offset
        val position = parent.getChildLayoutPosition(view)

        if (position == 0) {
            outRect.top = offset2
        }
        outRect.left = offset1
        outRect.right = offset1
        outRect.bottom = offset2
    }
}