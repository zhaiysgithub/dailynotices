package com.suncity.dailynotices.ui.views.recyclerview.adapter

import android.view.View

/**
 *
 * recyclerView 事件处理.
 */
interface EventDelegate {

     fun addData(length: Int)

     fun clear()

     fun stopLoadMore()

     fun pauseLoadMore()

     fun resumeLoadMore()

     fun setMore(view: View, listener: RecyclerArrayAdapter.OnMoreListener?)

     fun setNoMore(view: View, listener: RecyclerArrayAdapter.OnNoMoreListener?)

     fun setErrorMore(view: View, listener: RecyclerArrayAdapter.OnErrorListener?)

     fun setMore(res: Int, listener: RecyclerArrayAdapter.OnMoreListener?)

     fun setNoMore(res: Int, listener: RecyclerArrayAdapter.OnNoMoreListener?)

     fun setErrorMore(res: Int, listener: RecyclerArrayAdapter.OnErrorListener?)
}