package com.suncity.dailynotices.ui.views.recyclerview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.suncity.dailynotices.utils.DisplayUtils


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.views.recyclerview
 * @ClassName:      RecycleGridDivider
 * @Description:     作用描述
 * @UpdateDate:     14/7/2019
 */
class RecycleGridDivider : RecyclerView.ItemDecoration {

    private var space: Int = DisplayUtils.dip2px(2f)
    private var color: Int = Color.WHITE
    private var mPaint: Paint? = null

    constructor() : this(1)

    constructor(space: Int) : this(space, Color.WHITE)

    constructor(space: Int, color: Int) : super() {
        this.space = space
        this.color = color
        initPaint()
    }


    private fun initPaint() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = color
        mPaint?.style = Paint.Style.FILL
        mPaint?.strokeWidth = space.toFloat()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager as GridLayoutManager?
        val childSize = parent.childCount
        val span = manager?.spanCount ?: 0
        //为了Item大小均匀，将设定分割线平均分给左右两边Item各一半
        val offset = space / 2
        //得到View的位置
        val childPosition = parent.getChildAdapterPosition(view)
        //第一排，顶部不画
        if (childPosition < span) {
            //最左边的，左边不画
            when {
                childPosition % span == 0 -> outRect.set(0, 0, offset, 0)
                //最右边，右边不画
                childPosition % span == span - 1 -> outRect.set(offset, 0, 0, 0)
                else -> outRect.set(offset, 0, offset, 0)
            }
        } else {
            //上下的分割线，就从第二排开始，每个区域的顶部直接添加设定大小，不用再均分了
            when {
                childPosition % span == 0 -> outRect.set(0, space, offset, 0)
                childPosition % span == span - 1 -> outRect.set(offset, space, 0, 0)
                else -> outRect.set(offset, space, offset, 0)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }
}