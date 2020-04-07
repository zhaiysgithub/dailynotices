package com.suncity.dailynotices.ui.views.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

class FixDataObserver(recyclerView: RecyclerView) : RecyclerView.AdapterDataObserver() {
    private var mRecyclerView:RecyclerView? = recyclerView

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        if (mRecyclerView != null && mRecyclerView!!.adapter is RecyclerArrayAdapter<*>) {
            val adapter = mRecyclerView!!.adapter as RecyclerArrayAdapter<*>
            if (adapter.getFooterCount() > 0 && adapter.getCount() === itemCount) {
                mRecyclerView!!.scrollToPosition(0)
            }
        }
    }
}