package com.imaec.wishplace.ui.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.imaec.wishplace.utils.Utils.Companion.dp

class PlaceItemDecoration(var context: Context) : RecyclerView.ItemDecoration() {

    private val offset1 = dp(context, 6)
    private val offset2 = dp(context, 12)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Item Offset
        val position = parent.getChildLayoutPosition(view)
        if (position == 0 || position == 1) {
            outRect.top = offset2
        }
        outRect.bottom = offset2
        outRect.left = offset1
        outRect.right = offset1
    }
}