package com.suncity.dailynotices.ui.views.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.suncity.dailynotices.R


import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter

import java.util.ArrayList


open class HeadAndFooterRecyclerView : FrameLayout {
    private var recyclerView: RecyclerView? = null
    private var mProgressView: ViewGroup? = null
    private var mEmptyView: ViewGroup? = null
    private var mErrorView: ViewGroup? = null
    private var mContainer: FrameLayout? = null
    private var mProgressId: Int = 0
    private var mEmptyId: Int = 0
    private var mErrorId: Int = 0

    private var mClipToPadding: Boolean = false
    private var mPadding: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingBottom: Int = 0
    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0
    private var mScrollbarStyle: Int = 0
    private var mScrollbar: Int = 0


    private var mInternalOnScrollListener: RecyclerView.OnScrollListener? = null
    protected var mExternalOnScrollListener: RecyclerView.OnScrollListener? = null
    protected var mChildAttachStateChangeListener: RecyclerView.OnChildAttachStateChangeListener? = null
    protected var mExternalOnScrollListenerList = ArrayList<RecyclerView.OnScrollListener>()


    val firstVisiblePosition: Int
        get() {
            val position: Int
            position = when {
                recyclerView?.layoutManager is LinearLayoutManager -> (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                recyclerView?.layoutManager is GridLayoutManager -> (recyclerView?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                recyclerView?.layoutManager is StaggeredGridLayoutManager -> {
                    val layoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager?
                    if(layoutManager != null){
                        val lastPositions = layoutManager!!.findFirstVisibleItemPositions(IntArray(layoutManager.spanCount))
                        getMinPosition(lastPositions)
                    }else{
                        0
                    }
                }
                else -> 0
            }
            return position
        }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    val lastVisiblePosition: Int
        get() {
            val position: Int
            position = when {
                recyclerView?.layoutManager is LinearLayoutManager -> (recyclerView?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                recyclerView?.layoutManager is GridLayoutManager -> (recyclerView?.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                recyclerView?.layoutManager is StaggeredGridLayoutManager -> {
                    val layoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager?
                    if(layoutManager != null){
                        val lastPositions = layoutManager.findLastVisibleItemPositions(IntArray(layoutManager.spanCount))
                        getMaxPosition(lastPositions)
                    }else{
                        0
                    }
                }
                else -> ((recyclerView?.layoutManager?.itemCount ?: 1) - 1)
            }
            return position
        }

    /**
     * 设置适配器，关闭所有副view。展示recyclerView
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     */
    var adapter: RecyclerView.Adapter<*>?
        get() = recyclerView?.adapter
        set(adapter) {
            recyclerView?.adapter = adapter
            adapter?.registerAdapterDataObserver(EasyDataObserver(this))
            showRecycler()
        }


    /**
     * @return inflated error view or null
     */
    var errorView: View?
        get() = if (mErrorView != null && mErrorView!!.childCount > 0) mErrorView!!.getChildAt(0) else null
        set(errorView) {
            mErrorView?.removeAllViews()
            mErrorView?.addView(errorView)
        }

    /**
     * @return inflated progress view or null
     */
    var progressView: View?
        get() = if (mProgressView != null && mProgressView!!.childCount > 0) mProgressView!!.getChildAt(0) else null
        set(progressView) {
            mProgressView?.removeAllViews()
            mProgressView?.addView(progressView)
        }


    /**
     * @return inflated empty view or null
     */
    var emptyView: View?
        get() = if (mEmptyView != null && mEmptyView!!.childCount > 0) mEmptyView!!.getChildAt(0) else null
        set(emptyView) {
            mEmptyView?.removeAllViews()
            mEmptyView?.addView(emptyView)
        }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttrs(attrs)
        initView()
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.superrecyclerview)
        try {
            mClipToPadding = a.getBoolean(R.styleable.superrecyclerview_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.superrecyclerview_recyclerPadding, -1.0f).toInt()
            mPaddingTop = a.getDimension(R.styleable.superrecyclerview_recyclerPaddingTop, 0.0f).toInt()
            mPaddingBottom = a.getDimension(R.styleable.superrecyclerview_recyclerPaddingBottom, 0.0f).toInt()
            mPaddingLeft = a.getDimension(R.styleable.superrecyclerview_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingRight = a.getDimension(R.styleable.superrecyclerview_recyclerPaddingRight, 0.0f).toInt()
            mScrollbarStyle = a.getInteger(R.styleable.superrecyclerview_scrollbarStyle, -1)
            mScrollbar = a.getInteger(R.styleable.superrecyclerview_scrollbars, -1)

            mEmptyId = a.getResourceId(R.styleable.superrecyclerview_layout_empty, 0)
            mProgressId = a.getResourceId(R.styleable.superrecyclerview_layout_progress, 0)
            mErrorId = a.getResourceId(R.styleable.superrecyclerview_layout_error, 0)
        } finally {
            a.recycle()
        }
    }

    private fun initView() {
        if (isInEditMode) {
            return
        }
        //生成主View
        val v = LayoutInflater.from(context).inflate(R.layout.layout_progress_recyclerview, this)
        mContainer = v.findViewById(R.id.frame_layout)

        mProgressView = v.findViewById<View>(R.id.progress) as ViewGroup
        if (mProgressId != 0) LayoutInflater.from(context).inflate(mProgressId, mProgressView)
        mEmptyView = v.findViewById<View>(R.id.empty) as ViewGroup
        if (mEmptyId != 0) LayoutInflater.from(context).inflate(mEmptyId, mEmptyView)
        mErrorView = v.findViewById<View>(R.id.error) as ViewGroup
        if (mErrorId != 0) LayoutInflater.from(context).inflate(mErrorId, mErrorView)
        initRecyclerView(v)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return mContainer?.dispatchTouchEvent(ev) ?: false
    }

    fun setRecyclerPadding(left: Int, top: Int, right: Int, bottom: Int) {
        this.mPaddingLeft = left
        this.mPaddingTop = top
        this.mPaddingRight = right
        this.mPaddingBottom = bottom
        recyclerView?.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
    }

    override fun setClipToPadding(isClip: Boolean) {
        recyclerView?.clipToPadding = isClip
    }

    fun setEmptyView(emptyView: Int) {
        mEmptyView?.removeAllViews()
        LayoutInflater.from(context).inflate(emptyView, mEmptyView)
    }

    fun setProgressView(progressView: Int) {
        mProgressView?.removeAllViews()
        LayoutInflater.from(context).inflate(progressView, mProgressView)
    }

    fun setErrorView(errorView: Int) {
        mErrorView?.removeAllViews()
        LayoutInflater.from(context).inflate(errorView, mErrorView)
    }

    fun scrollToPosition(position: Int) {
        recyclerView?.scrollToPosition(position)
    }


    fun initRecyclerView(view: View) {
        recyclerView = view.findViewById<View>(android.R.id.list) as RecyclerView
        setItemAnimator(null)
        if (recyclerView != null) {
            recyclerView?.setHasFixedSize(true)
            recyclerView?.clipToPadding = mClipToPadding
            mInternalOnScrollListener = object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener?.onScrolled(recyclerView, dx, dy)
                    for (listener in mExternalOnScrollListenerList) {
                        listener.onScrolled(recyclerView, dx, dy)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener?.onScrollStateChanged(recyclerView, newState)
                    for (listener in mExternalOnScrollListenerList) {
                        listener.onScrollStateChanged(recyclerView, newState)
                    }
                }
            }
            recyclerView?.addOnScrollListener(mInternalOnScrollListener!!)
            recyclerView?.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    if (mChildAttachStateChangeListener != null) {
                        mChildAttachStateChangeListener?.onChildViewAttachedToWindow(view)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {
                    if (mChildAttachStateChangeListener != null) {
                        mChildAttachStateChangeListener?.onChildViewDetachedFromWindow(view)
                    }
                }
            })

            if (mPadding.toFloat() != -1.0f) {
                recyclerView?.setPadding(mPadding, mPadding, mPadding, mPadding)
            } else {
                recyclerView?.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            }
            if (mScrollbarStyle != -1) {
                recyclerView?.scrollBarStyle = mScrollbarStyle
            }
            when (mScrollbar) {
                0 -> isVerticalScrollBarEnabled = false
                1 -> isHorizontalScrollBarEnabled = false
                2 -> {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
            }
        }
    }

    override fun setVerticalScrollBarEnabled(verticalScrollBarEnabled: Boolean) {
        recyclerView?.isVerticalScrollBarEnabled = verticalScrollBarEnabled
    }

    override fun setHorizontalScrollBarEnabled(horizontalScrollBarEnabled: Boolean) {
        recyclerView?.isHorizontalScrollBarEnabled = horizontalScrollBarEnabled
    }

    fun setLayoutManager(manager: RecyclerView.LayoutManager) {
        recyclerView?.layoutManager = manager
    }

    /**
     * 设置适配器，关闭所有副view。展示进度条View
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    fun setAdapterWithProgress(adapter: RecyclerView.Adapter<*>) {
        recyclerView?.adapter = adapter
        adapter.registerAdapterDataObserver(EasyDataObserver(this))
        //只有Adapter为空时才显示ProgressView
        if (adapter is RecyclerArrayAdapter<*>)
            run {
                if (adapter.getCount() === 0) {
                    showProgress()
                } else {
                    showRecycler()
                }
            }
        run {
            if (adapter.itemCount == 0) {
                showProgress()
            } else {
                showRecycler()
            }
        }
    }

    /**
     * Remove the adapter from the recycler
     */
    fun clear() {
        recyclerView?.adapter = null
    }


    private fun hideAll() {
        mEmptyView?.visibility = View.GONE
        mProgressView?.visibility = View.GONE
        mErrorView?.visibility = View.GONE
        recyclerView?.visibility = View.INVISIBLE
    }


    fun showError() {
        if (mErrorView != null && mErrorView!!.childCount > 0) {
            hideAll()
            mErrorView!!.visibility = View.VISIBLE
        } else {
            showRecycler()
        }

    }

    fun showEmpty() {
        if (mEmptyView != null && mEmptyView!!.childCount > 0) {
            hideAll()
            mEmptyView!!.visibility = View.VISIBLE
        } else {
            showRecycler()
        }
    }


    fun showProgress() {
        if (mProgressView != null && mProgressView!!.childCount > 0) {
            hideAll()
            mProgressView!!.visibility = View.VISIBLE
        } else {
            showRecycler()
        }
    }


    fun showRecycler() {
        hideAll()
        recyclerView?.visibility = View.VISIBLE
    }

    private fun getMinPosition(positions: IntArray): Int {

        var minPosition = Integer.MAX_VALUE
        for (i in positions.indices) {
            minPosition = Math.min(minPosition, positions[i])
        }
        return minPosition
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private fun getMaxPosition(positions: IntArray): Int {

        var maxPosition = Integer.MIN_VALUE
        for (i in positions.indices) {
            maxPosition = Math.max(maxPosition, positions[i])
        }
        return maxPosition
    }

    /**
     * Set the scroll listener for the recycler
     *
     * @param listener
     */
    fun setOnScrollListener(listener: RecyclerView.OnScrollListener) {
        mExternalOnScrollListener = listener
    }

    fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        mExternalOnScrollListenerList.add(listener)
    }

    fun removeOnScrollListener(listener: RecyclerView.OnScrollListener) {
        mExternalOnScrollListenerList.remove(listener)
    }

    fun removeAllOnScrollListeners() {
        mExternalOnScrollListenerList.clear()
    }

    fun setOnChildeAttachToWindowListener(listener: RecyclerView.OnChildAttachStateChangeListener) {
        mChildAttachStateChangeListener = listener
    }

    /**
     * Add the onItemTouchListener for the recycler
     *
     * @param listener
     */
    fun addOnItemTouchListener(listener: RecyclerView.OnItemTouchListener) {
        recyclerView?.addOnItemTouchListener(listener)
    }

    /**
     * Remove the onItemTouchListener for the recycler
     *
     * @param listener
     */
    fun removeOnItemTouchListener(listener: RecyclerView.OnItemTouchListener) {
        recyclerView?.removeOnItemTouchListener(listener)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(listener: View.OnTouchListener) {
        recyclerView?.setOnTouchListener(listener)
    }

    fun setItemAnimator(animator: RecyclerView.ItemAnimator?) {
        recyclerView?.itemAnimator = animator
    }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView?.addItemDecoration(itemDecoration)
    }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration, index: Int) {
        recyclerView?.addItemDecoration(itemDecoration, index)
    }

    fun removeItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView?.removeItemDecoration(itemDecoration)
    }


}
