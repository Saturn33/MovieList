package ru.otus.saturn33.movielist.presentation.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class CustomDecoration(
    context: Context,
    orientation: Int,
    private val divDrawable: Drawable?,
    private val paddingLeft: Int,
    private val paddingRight: Int
) : DividerItemDecoration(context, orientation) {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {}

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + paddingLeft
        val right = parent.width - parent.paddingRight - paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {//- 1: футеру не нужен декоратор
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (divDrawable?.intrinsicHeight ?: 1)

            divDrawable?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }

        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = view.marginBottom
    }
}
