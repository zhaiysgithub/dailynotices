package com.suncity.dailynotices.ui.views.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 *
 * 事件处理类
 */
class DefaultEventDelegate<T>(adapter: RecyclerArrayAdapter<T>) : EventDelegate {

    companion object {
        private const val STATUS_INITIAL = 291//初始状态
        private const val STATUS_MORE = 260//加载更多状态
        private const val STATUS_NOMORE = 408//无更多状态
        private const val STATUS_ERROR = 732//错误状态
        private const val Hide = 0
        private const val ShowMore = 1
        private const val ShowError = 2
        private const val ShowNoMore = 3
    }

    private var mAdapter: RecyclerArrayAdapter<T>? = adapter
    private var footer: EventFooter? = null

    private var onMoreListener: RecyclerArrayAdapter.OnMoreListener? = null
    private var onNoMoreListener: RecyclerArrayAdapter.OnNoMoreListener? = null
    private var onErrorListener: RecyclerArrayAdapter.OnErrorListener? = null

    private var status = STATUS_INITIAL
    private var isLoadingMore = false//正在加载更多

    private var hasMore = false
    private var hasNoMore = false
    private var hasData = false
    private var hasError = false

    init {
        footer = EventFooter()
        mAdapter!!.addFooter(footer)
    }

    fun onMoreViewClicked() {
        if (onMoreListener != null) onMoreListener!!.onMoreClick()
    }

    fun onErrorViewShowed() {
        if (onErrorListener != null) onErrorListener!!.onErrorShow()
    }

    fun onErrorViewClicked() {
        if (onErrorListener != null) onErrorListener!!.onErrorClick()
    }

    fun onNoMoreViewShowed() {
        if (onNoMoreListener != null) onNoMoreListener!!.onNoMoreShow()
    }

    fun onNoMoreViewClicked() {
        if (onNoMoreListener != null) onNoMoreListener!!.onNoMoreClick()
    }

    fun onMoreViewShowed() {
        if (!isLoadingMore && onMoreListener != null) {
            isLoadingMore = true
            onMoreListener!!.onMoreShow()
        }
    }

    //-------------------5个状态触发事件-------------------
    override fun addData(length: Int) {
        if (hasMore) {
            if (length == 0) {
                //当添加0个时，认为已结束加载到底
                if (status == STATUS_INITIAL || status == STATUS_MORE) {
                    footer?.showNoMore()
                    status = STATUS_NOMORE
                }
            } else {
                //当Error或初始时。添加数据，如果有More则还原。
                footer?.showMore()
                status = STATUS_MORE
                hasData = true
            }
        } else {
            if (hasNoMore) {
                footer?.showNoMore()
                status = STATUS_NOMORE
            }
        }
        isLoadingMore = false
    }

    override fun clear() {
        hasData = false
        status = STATUS_INITIAL
        footer?.hide()
        isLoadingMore = false
    }

    override fun stopLoadMore() {
        footer?.showNoMore()
        status = STATUS_NOMORE
        isLoadingMore = false
    }

    override fun pauseLoadMore() {//发生错误暂停加载更多
        footer?.showError()
        status = STATUS_ERROR
        isLoadingMore = false
    }

    override fun resumeLoadMore() {
        isLoadingMore = false
        footer?.showMore()
        status = STATUS_MORE
        onMoreViewShowed()
    }

    //-------------------3种View设置-------------------
    override fun setMore(view: View, listener: RecyclerArrayAdapter.OnMoreListener?) {
        this.footer?.setMoreView(view)
        this.onMoreListener = listener
        hasMore = true
        // 为了处理setMore之前就添加了数据的情况
        if (mAdapter?.getCount() ?: 0 > 0) {
            addData(mAdapter?.getCount() ?: 0)
        }
    }

    override fun setNoMore(view: View, listener: RecyclerArrayAdapter.OnNoMoreListener?) {
        this.footer?.setNoMoreView(view)
        this.onNoMoreListener = listener
        hasNoMore = true
    }

    override fun setErrorMore(view: View, listener: RecyclerArrayAdapter.OnErrorListener?) {
        this.footer?.setErrorView(view)
        this.onErrorListener = listener
        hasError = true
    }

    override fun setMore(res: Int, listener: RecyclerArrayAdapter.OnMoreListener?) {
        this.footer?.setMoreViewRes(res)
        this.onMoreListener = listener
        hasMore = true
        // 为了处理setMore之前就添加了数据的情况
        if (mAdapter?.getCount() ?: 0 > 0) {
            addData(mAdapter?.getCount() ?: 0)
        }
    }

    override fun setNoMore(res: Int, listener: RecyclerArrayAdapter.OnNoMoreListener?) {
        this.footer?.setNoMoreViewRes(res)
        this.onNoMoreListener = listener
        hasNoMore = true
    }

    override fun setErrorMore(res: Int, listener: RecyclerArrayAdapter.OnErrorListener?) {
        this.footer?.setErrorViewRes(res)
        this.onErrorListener = listener
        hasError = true
    }

    inner class EventFooter : RecyclerArrayAdapter.ItemView {

        private var moreView: View? = null
        private var noMoreView: View? = null
        private var errorView: View? = null
        private var moreViewRes = 0
        private var noMoreViewRes = 0
        private var errorViewRes = 0

        private var flag = Hide
        var skipError = false
        var skipNoMore = false

        override fun onCreateView(parent: ViewGroup): View {
            return refreshStatus(parent)
        }

        override fun onBindView(headerView: View) {
            headerView.post {
                when (flag) {
                    ShowMore -> onMoreViewShowed()
                    ShowNoMore -> {
                        if (!skipNoMore) onNoMoreViewShowed()
                        skipNoMore = false
                    }
                    ShowError -> {
                        if (!skipError) onErrorViewShowed()
                        skipError = false
                    }
                }
            }
        }

        private fun refreshStatus(parent: ViewGroup): View {
            var view: View? = null
            when (flag) {
                ShowMore -> {
                    if (moreView != null)
                        view = moreView
                    else if (moreViewRes != 0)
                        view = LayoutInflater.from(parent.context).inflate(moreViewRes, parent, false)
                    view?.setOnClickListener { onMoreViewClicked() }
                }
                ShowError -> {
                    if (errorView != null)
                        view = errorView
                    else if (errorViewRes != 0)
                        view = LayoutInflater.from(parent.context).inflate(errorViewRes, parent, false)
                    view?.setOnClickListener { onErrorViewClicked() }
                }
                ShowNoMore -> {
                    if (noMoreView != null)
                        view = noMoreView
                    else if (noMoreViewRes != 0)
                        view = LayoutInflater.from(parent.context).inflate(noMoreViewRes, parent, false)
                    view?.setOnClickListener { onNoMoreViewClicked() }
                }
            }
            if (view == null)
                view = FrameLayout(parent.context)
            return view
        }

        fun showError() {
            skipError = true
            flag = ShowError
            if (mAdapter != null && mAdapter!!.itemCount > 0)
                mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
        }

        fun showMore() {
            flag = ShowMore
            if (mAdapter != null && mAdapter!!.itemCount > 0)
                mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
        }

        fun showNoMore() {
            skipNoMore = true
            flag = ShowNoMore
            if (mAdapter != null && mAdapter!!.itemCount > 0)
                mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
        }

        //初始化
        fun hide() {
            flag = Hide
            if (mAdapter != null && mAdapter!!.itemCount > 0)
                mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
        }

        fun setMoreView(moreView: View) {
            this.moreView = moreView
            this.moreViewRes = 0
        }

        fun setNoMoreView(noMoreView: View) {
            this.noMoreView = noMoreView
            this.noMoreViewRes = 0
        }

        fun setErrorView(errorView: View) {
            this.errorView = errorView
            this.errorViewRes = 0
        }

        fun setMoreViewRes(moreViewRes: Int) {
            this.moreView = null
            this.moreViewRes = moreViewRes
        }

        fun setNoMoreViewRes(noMoreViewRes: Int) {
            this.noMoreView = null
            this.noMoreViewRes = noMoreViewRes
        }

        fun setErrorViewRes(errorViewRes: Int) {
            this.errorView = null
            this.errorViewRes = errorViewRes
        }

        override fun hashCode(): Int {
            return flag + 12345
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DefaultEventDelegate<*>.EventFooter

            if (moreView != other.moreView) return false
            if (noMoreView != other.noMoreView) return false
            if (errorView != other.errorView) return false
            if (moreViewRes != other.moreViewRes) return false
            if (noMoreViewRes != other.noMoreViewRes) return false
            if (errorViewRes != other.errorViewRes) return false
            if (flag != other.flag) return false
            if (skipError != other.skipError) return false
            if (skipNoMore != other.skipNoMore) return false

            return true
        }
    }

}