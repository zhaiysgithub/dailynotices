package com.suncity.dailynotices.ui.views.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

class EasyDataObserver(private val recyclerView: HeadAndFooterRecyclerView) : RecyclerView.AdapterDataObserver() {

    private var adapter: RecyclerArrayAdapter<*>? = null

    init {
        if (recyclerView.adapter is RecyclerArrayAdapter<*>){
            adapter = recyclerView.adapter as RecyclerArrayAdapter<*>
        }
    }

    private fun isHeaderFooter(position: Int): Boolean {
        return adapter != null && (position < adapter!!.getHeaderCount() || position >= adapter!!.getHeaderCount() + adapter!!.getCount())
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        if (!isHeaderFooter(positionStart)) {
            update()
        }
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        if (!isHeaderFooter(positionStart)) {
            update()
        }
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        if (!isHeaderFooter(positionStart)) {
            update()
        }
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        update()//header&footer不会有移动操作
    }

    override fun onChanged() {
        super.onChanged()
        update()//header&footer不会引起changed
    }


    //自动更改Container的样式
    private fun update() {
        val count: Int = if (recyclerView.adapter is RecyclerArrayAdapter<*>) {
            (recyclerView.adapter as RecyclerArrayAdapter<*>).getCount()
        } else {
            recyclerView.adapter?.itemCount ?: 0
        }
        if (count == 0) {
            recyclerView.showEmpty()
        } else {
            recyclerView.showRecycler()
        }
    }
}