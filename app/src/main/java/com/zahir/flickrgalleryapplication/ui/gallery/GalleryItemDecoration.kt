package com.zahir.flickrgalleryapplication.ui.gallery

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zahir.flickrgalleryapplication.R

/**
 * Item decorator used for Grid recycler view for image gallery
 */
class GalleryItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val itemSpacing = context.resources.getDimension(R.dimen.grid_space).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as GalleryAdapter
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val column = (position % spanCount) + 1
        val row = (position / spanCount) + 1

        val isFirsColumn = column == 1
        val isLastColumn = column == spanCount
        val isMiddleColumn = !(isFirsColumn || isLastColumn)
        val isFirstRow = row == 1
        val isLastRow = row == (adapter.items.size - 1 / spanCount) + 1

        when {
            isFirsColumn && isFirstRow -> {
                outRect.bottom = itemSpacing / 2
                outRect.left = itemSpacing
                outRect.right = itemSpacing / 2
            }

            isFirsColumn && isLastColumn -> {
                outRect.bottom = itemSpacing / 2
                outRect.left = itemSpacing / 2
                outRect.right = itemSpacing
            }

            isFirsColumn && isMiddleColumn -> {
                outRect.bottom = itemSpacing / 2
                outRect.left = itemSpacing / 2
                outRect.right = itemSpacing / 2
            }

            isLastRow && isFirsColumn -> {
                outRect.top = itemSpacing / 2
                outRect.bottom = itemSpacing
                outRect.left = itemSpacing
                outRect.right = itemSpacing / 2
            }

            isLastRow && isLastColumn -> {
                outRect.top = itemSpacing / 2
                outRect.bottom = itemSpacing
                outRect.left = itemSpacing / 2
                outRect.right = itemSpacing
            }

            isLastRow && isMiddleColumn -> {
                outRect.top = itemSpacing / 2
                outRect.bottom = itemSpacing
                outRect.left = itemSpacing / 2
                outRect.right = itemSpacing / 2
            }

            isFirsColumn -> {
                outRect.top = itemSpacing / 2
                outRect.bottom = itemSpacing / 2
                outRect.left = itemSpacing
                outRect.right = itemSpacing / 2
            }

            isLastColumn -> {
                outRect.top = itemSpacing / 2
                outRect.bottom = itemSpacing / 2
                outRect.left = itemSpacing / 2
                outRect.right = itemSpacing
            }

            else -> {
                outRect.top = itemSpacing / 2
                outRect.bottom = itemSpacing / 2
                outRect.left = itemSpacing / 2
                outRect.right = itemSpacing / 2
            }
        }
    }
}