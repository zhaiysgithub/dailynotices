package com.suncity.dailynotices.ui.views.flowlayout

import android.view.View
import java.util.HashSet

abstract class TagAdapter<T> {
    private var mTagDatas: List<T>? = null
    private var mOnDataChangedListener: OnDataChangedListener? = null
    val preCheckedList = HashSet<Int>()


    val count: Int
        get() = if (mTagDatas == null) 0 else mTagDatas!!.size

    constructor(datas: List<T>) {
        mTagDatas = datas
    }


    interface OnDataChangedListener {
        fun onChanged()
    }

    fun setOnDataChangedListener(listener: OnDataChangedListener) {
        mOnDataChangedListener = listener
    }

    open fun setSelectedList(vararg poses: Int) {
        val set = HashSet<Int>()
        for (pos in poses) {
            set.add(pos)
        }
        setSelectedList(set)
    }

    open fun setSelectedList(set: Set<Int>?) {
        preCheckedList.clear()
        if (set != null) {
            preCheckedList.addAll(set)
        }
        notifyDataChanged()
    }

    fun notifyDataChanged() {
        if (mOnDataChangedListener != null)
            mOnDataChangedListener!!.onChanged()
    }

   open fun getItem(position: Int): T {
        return mTagDatas!![position]
    }

    abstract fun getView(parent: FlowLayout, position: Int, t: T): View

    open fun setSelected(position: Int, t: T): Boolean {
        return false
    }


}
