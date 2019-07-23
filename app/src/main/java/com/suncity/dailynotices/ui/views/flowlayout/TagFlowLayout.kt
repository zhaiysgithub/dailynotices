package com.suncity.dailynotices.ui.views.flowlayout

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.DisplayUtils
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.ToastUtils

import java.util.HashSet

class TagFlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FlowLayout(context, attrs, defStyle), TagAdapter.OnDataChangedListener {

    var mAdapter: TagAdapter<String>? = null
        set(adapter) {
            field = adapter
            mAdapter?.setOnDataChangedListener(this)
            mSelectedView.clear()
            changeAdapter()
        }
    private var mSelectedMax = -1//-1为不限制数量

    private val mSelectedView = HashSet<Int>()

    private var mOnSelectListener: OnSelectListener? = null
    private var mOnTagClickListener: OnTagClickListener? = null

    val selectedList: Set<Int>
        get() = HashSet(mSelectedView)

    interface OnSelectListener {
        fun onSelected(selectPosSet: Set<Int>)
    }

    interface OnTagClickListener {
        fun onTagClick(view: TagView, position: Int, parent: FlowLayout): Boolean
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout)
        mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1)
        ta.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val cCount = childCount
        for (i in 0 until cCount) {
            val tagView = getChildAt(i) as TagView
            if (tagView.visibility == View.GONE) {
                continue
            }
            if (tagView.tagView.visibility == View.GONE) {
                tagView.visibility = View.GONE
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    fun setOnSelectListener(onSelectListener: OnSelectListener) {
        mOnSelectListener = onSelectListener
    }


    fun setOnTagClickListener(onTagClickListener: OnTagClickListener) {
        mOnTagClickListener = onTagClickListener
    }

    private fun changeAdapter() {
        removeAllViews()
        val adapter = mAdapter
        var tagViewContainer: TagView?
        val preCheckedList = (mAdapter?.preCheckedList ?: HashSet())
        for (i in 0 until (adapter?.count ?: 0)) {
            val tagView = adapter?.getView(this, i, adapter.getItem(i))

            tagViewContainer = TagView(context)
            tagView?.isDuplicateParentStateEnabled = true
            if (tagView?.layoutParams != null) {
                tagViewContainer.layoutParams = tagView.layoutParams


            } else {
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(margin, margin, margin, margin)
                tagViewContainer.layoutParams = lp
            }
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            tagView?.layoutParams = lp
            tagViewContainer.addView(tagView)
            addView(tagViewContainer)

            if (preCheckedList.contains(i)) {
                setChildChecked(i, tagViewContainer)
            }
            val item = adapter?.getItem(i)

            if (item != null && mAdapter?.setSelected(i, item) == true) {
                setChildChecked(i, tagViewContainer)
            }
            tagView?.isClickable = false
            val finalTagViewContainer = tagViewContainer
            tagViewContainer.setOnClickListener {
                doSelect(finalTagViewContainer, i)
                if (mOnTagClickListener != null) {
                    mOnTagClickListener!!.onTagClick(
                        finalTagViewContainer, i,
                        this@TagFlowLayout
                    )
                }
            }
        }
        mSelectedView.addAll(preCheckedList)
    }

    fun setMaxSelectCount(count: Int) {
        if (mSelectedView.size > count) {
            mSelectedView.clear()
        }
        mSelectedMax = count
    }

    private fun setChildChecked(position: Int, view: TagView) {
        view.isChecked = true
        LogUtils.e("selected $position")
    }

    private fun setChildUnChecked(position: Int, view: TagView) {
        view.isChecked = false
        LogUtils.e("unSelected $position")
    }

    private fun doSelect(child: TagView, position: Int) {
        if (!child.isChecked) {
            //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size == 1) {
                val iterator = mSelectedView.iterator()
                val preIndex = iterator.next()
                val pre = getChildAt(preIndex) as TagView
                setChildUnChecked(preIndex, pre)
                setChildChecked(position, child)

                mSelectedView.remove(preIndex)
                mSelectedView.add(position)
            } else {
                if (mSelectedMax > 0 && mSelectedView.size >= mSelectedMax) {
                    return
                }
                setChildChecked(position, child)
                mSelectedView.add(position)
            }
        } else {
            setChildUnChecked(position, child)
            mSelectedView.remove(position)
        }
        if (mOnSelectListener != null) {
            mOnSelectListener?.onSelected(HashSet(mSelectedView))
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState())

        var selectPos = ""
        if (mSelectedView.size > 0) {
            for (key in mSelectedView) {
                selectPos += "$key|"
            }
            selectPos = selectPos.substring(0, selectPos.length - 1)
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val mSelectPos = state.getString(KEY_CHOOSE_POS)
            if (mSelectPos != null && !TextUtils.isEmpty(mSelectPos)) {
                val split = mSelectPos.split("\\|")
                for (pos in split) {
                    val index = Integer.parseInt(pos)
                    mSelectedView.add(index)

                    val tagView = getChildAt(index) as TagView
                    setChildChecked(index, tagView)
                }

            }
            super.onRestoreInstanceState(state.getParcelable(KEY_DEFAULT))
            return
        }
        super.onRestoreInstanceState(state)
    }


    override fun onChanged() {
        mSelectedView.clear()
        changeAdapter()
    }

    companion object {

        private const val KEY_CHOOSE_POS = "key_choose_pos"
        private const val KEY_DEFAULT = "key_default"

        private val margin = DisplayUtils.dip2px(5f)
    }
}
