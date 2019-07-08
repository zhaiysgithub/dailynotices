package com.suncity.dailynotices.ui.views.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import com.suncity.dailynotices.ui.views.recyclerview.FixDataObserver
import java.util.*

/**
 *
 * 包含各种类型的recyclerView adapter
 */
abstract class RecyclerArrayAdapter<T> : RecyclerView.Adapter<HAFViewHolder<T>> {

    protected var mObjects: MutableList<T>? = null
    private var mContext: Context? = null

    private var mEventDelegate: EventDelegate? = null
    protected var headers = ArrayList<ItemView>()
    protected var footers = ArrayList<ItemView>()

    private var mItemClickListener: OnItemClickListener? = null
    private var mItemLongClickListener: OnItemLongClickListener? = null

    private var mRecyclerView: RecyclerView? = null

    constructor(context:Context):super(){
        init(context, ArrayList())
    }

    constructor(context:Context , objects:ArrayList<T>):super(){
        init(context, objects)
    }

    private fun init(context: Context, objects: List<T>) {
        mContext = context
        mObjects = ArrayList(objects)
    }

    fun getCount(): Int {
        return (mObjects?.size ?: 0)
    }

    inner class GridSpanSizeLookup(maxCount: Int) : GridLayoutManager.SpanSizeLookup() {
        private var mMaxCount = maxCount

        override fun getSpanSize(position: Int): Int {
            if (headers.size != 0) {
                if (position < headers.size) return mMaxCount
            }
            if (footers.size != 0) {
                val i = position - headers.size - (mObjects?.size ?: 0)
                if (i >= 0) {
                    return mMaxCount
                }
            }
            return 1
        }

    }

    fun obtainGridSpanSizeLookUp(maxCount: Int): GridSpanSizeLookup {
        return GridSpanSizeLookup(maxCount)
    }

    private val mLock = Any()

    //不自动刷新，手动刷新
    private var mNotifyOnChange = true

    fun setNotifyOnChange(isNotifyOnChange:Boolean){
        mNotifyOnChange = isNotifyOnChange
    }

    fun stopMore() {
        if (mEventDelegate == null)
            throw NullPointerException("You should invoking setMore() first")
        mEventDelegate?.stopLoadMore()
    }

    fun pauseMore() {
        if (mEventDelegate == null)
            throw NullPointerException("You should invoking setMore() first")
        mEventDelegate?.pauseLoadMore()
    }

    fun resumeMore() {
        if (mEventDelegate == null)
            throw NullPointerException("You should invoking setMore() first")
        mEventDelegate?.resumeLoadMore()
    }

    fun addHeader(view: ItemView?) {
        if (view == null) throw NullPointerException("ItemView can't be null")
        headers.add(view)
        notifyItemInserted(headers.size - 1)
    }

    fun addFooter(view: ItemView?) {
        if (view == null) throw NullPointerException("ItemView can't be null")
        footers.add(view)
        notifyItemInserted(headers.size + getCount() + footers.size - 1)
    }

    fun removeAllHeader() {
        val count = headers.size
        headers.clear()
        notifyItemRangeRemoved(0, count)
    }

    fun removeAllFooter() {
        val count = footers.size
        footers.clear()
        notifyItemRangeRemoved(headers.size + getCount(), count)
    }

    fun getHeader(index: Int): ItemView {
        return headers[index]
    }

    fun getFooter(index: Int): ItemView {
        return footers[index]
    }

    fun getHeaderCount(): Int {
        return headers.size
    }

    fun getFooterCount(): Int {
        return footers.size
    }

    fun removeHeader(view: ItemView) {
        val position = headers.indexOf(view)
        headers.remove(view)
        notifyItemRemoved(position)
    }

    fun removeFooter(view: ItemView) {
        val position = headers.size + getCount() + footers.indexOf(view)
        footers.remove(view)
        notifyItemRemoved(position)
    }

    fun getEventDelegate(): EventDelegate {
        if (mEventDelegate == null) mEventDelegate = DefaultEventDelegate(this)
        return mEventDelegate!!
    }

    fun setMore(res: Int, listener: OnMoreListener?) {
        getEventDelegate().setMore(res, listener)
    }

    fun setMore(view: View, listener: OnMoreListener?) {
        getEventDelegate().setMore(view, listener)
    }

    fun setNoMore(res: Int) {
        getEventDelegate().setNoMore(res, null)
    }

    fun setNoMore(view: View) {
        getEventDelegate().setNoMore(view, null)
    }


    fun setError(res: Int) {
        getEventDelegate().setErrorMore(res, null)
    }

    fun setError(view: View) {
        getEventDelegate().setErrorMore(view, null)
    }

    fun setError(res: Int, listener: OnErrorListener) {
        getEventDelegate().setErrorMore(res, listener)
    }

    fun setError(view: View, listener: OnErrorListener) {
        getEventDelegate().setErrorMore(view, listener)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.mRecyclerView = recyclerView

        //增加对RecyclerArrayAdapter奇葩操作的修复措施
        registerAdapterDataObserver(FixDataObserver(mRecyclerView!!))
    }

    /**
     * 结尾添加item
     *
     * @param object .
     */
    fun add(`object`: T?) {
        if (mEventDelegate != null) mEventDelegate!!.addData(if (`object` == null) 0 else 1)
        if (`object` != null) {
            synchronized(mLock) {
                mObjects?.add(`object`)
            }
        }
        if (mNotifyOnChange) {
            notifyItemInserted(headers.size + getCount())
        }else{
            notifyDataSetChanged()
        }
    }

    fun addAll(index: Int, collection: Collection<T>?) {
        if (mEventDelegate != null)
            mEventDelegate!!.addData(collection?.size ?: 0)
        if (collection != null && collection.isNotEmpty()) {
            synchronized(mLock) {
                mObjects?.addAll(index, collection)
            }
        }
        val dataCount = (collection?.size ?: 0)
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + index, dataCount)
        }else{
            notifyDataSetChanged()
        }
    }

    fun addAll(collection: Collection<T>?) {
        if (mEventDelegate != null)
            mEventDelegate!!.addData(collection?.size ?: 0)
        if (collection != null && collection.isNotEmpty()) {
            synchronized(mLock) {
                mObjects?.addAll(collection)
            }
        }
        val dataCount = (collection?.size ?: 0)
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + getCount() - dataCount, dataCount)
        }else{
            notifyDataSetChanged()
        }

    }



    fun addAll(items: Array<T>?) {
        if (mEventDelegate != null) mEventDelegate!!.addData(items?.size ?: 0)
        if (items != null && items.isNotEmpty()) {
            synchronized(mLock) {
                Collections.addAll(mObjects, *items)
            }
        }
        val dataCount = (items?.size ?: 0)
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + getCount() - dataCount, dataCount)
        }else{
            notifyDataSetChanged()
        }
    }

    /**
     * 插入，不会触发任何事情
     *
     * @param object
     * @param index
     */
    fun insert(`object`: T, index: Int) {
        synchronized(mLock) {
            mObjects?.add(index, `object`)
        }
        if (mNotifyOnChange){
            notifyItemInserted(headers.size + index)
        }else{
            notifyDataSetChanged()
        }
    }

    /**
     * 插入数组，不会触发任何事情
     *
     * @param object
     * @param index
     */
    fun insertAll(`object`: Collection<T>?, index: Int) {
        synchronized(mLock) {
            mObjects?.addAll(index, `object`!!)
        }
        val dataCount = (`object`?.size ?: 0)
        if (mNotifyOnChange) {
            notifyItemRangeInserted(headers.size + index, dataCount)
        }else{
            notifyDataSetChanged()
        }
    }

    fun update(`object`: T, pos: Int) {
        synchronized(mLock) {
            mObjects?.set(pos, `object`)
        }
        if (mNotifyOnChange){
            notifyItemChanged(pos)
        }else{
            notifyDataSetChanged()
        }
    }

    /**
     * 删除，不会触发任何事情
     *
     * @param object
     */
    fun remove(`object`: T) {
        if (mObjects == null || mObjects!!.size == 0) return
        val position = mObjects!!.indexOf(`object`)
        synchronized(mLock) {
            if (mObjects!!.remove(`object`)) {
                if (mNotifyOnChange) {
                    notifyItemRemoved(headers.size + position)
                }else{
                    notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * 删除，不会触发任何事情
     *
     * @param position
     */
    fun remove(position: Int) {
        if (mObjects == null || mObjects!!.size == 0) return
        synchronized(mLock) {
            mObjects!!.removeAt(position)
        }
        if (mNotifyOnChange) {
            notifyItemRemoved(headers.size + position)
        }else{
            notifyDataSetChanged()
        }
    }

    fun removeAll() {
        if (mObjects == null || mObjects!!.size == 0) return
        val count = mObjects!!.size
        if (mEventDelegate != null) mEventDelegate!!.clear()
        synchronized(mLock) {
            mObjects!!.clear()
        }
        if (mNotifyOnChange) {
            notifyItemRangeRemoved(headers.size, count)
        }else{
            notifyDataSetChanged()
        }
    }

    /**
     * 触发清空
     */
    fun clear() {
        if (mObjects == null || mObjects!!.size == 0) return
        val count = mObjects!!.size
        if (mEventDelegate != null) mEventDelegate!!.clear()
        synchronized(mLock) {
            mObjects!!.clear()
        }
            notifyDataSetChanged()
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     * in this adapter.
     */
    fun sort(comparator: Comparator<in T>) {
        if (mObjects == null || mObjects!!.size == 0) return
        synchronized(mLock) {
            Collections.sort(mObjects, comparator)
        }
        notifyDataSetChanged()
    }

    fun getContext(): Context? {
        return mContext
    }

    fun setContext(ctx: Context) {
        mContext = ctx
    }

    /**
     * 这个函数包含了头部和尾部view的个数，不是真正的item个数。
     *
     * @return
     */
    override fun getItemCount(): Int {
        return (mObjects?.size ?: 0) + headers.size + footers.size
    }


    private fun createSpViewByType(parent: ViewGroup, viewType: Int): View? {
        for (headerView in headers) {
            if (headerView.hashCode() == viewType) {
                val view = headerView.onCreateView(parent)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams
                if (view.layoutParams != null)
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
                else
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.isFullSpan = true
                view.layoutParams = layoutParams
                return view
            }
        }
        for (footerview in footers) {
            if (footerview.hashCode() == viewType) {
                val view = footerview.onCreateView(parent)
                val layoutParams: StaggeredGridLayoutManager.LayoutParams
                if (view.layoutParams != null)
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(view.layoutParams)
                else
                    layoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.isFullSpan = true
                view.layoutParams = layoutParams
                return view
            }
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<T> {
        val view = createSpViewByType(parent, viewType)
        if (view != null) {
            return StateViewHolder(view)
        }

        val viewHolder = OnCreateViewHolder(parent, viewType)

        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener { v -> mItemClickListener!!.onItemClick(viewHolder.adapterPosition - headers.size, v) }
        }

        if (mItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(viewHolder.adapterPosition - headers.size) }
        }
        return viewHolder
    }

    abstract fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<T>


    override fun onBindViewHolder(holder: HAFViewHolder<T>, position: Int) {
        holder.itemView.id = position
        if (headers.size != 0 && position < headers.size) {
            headers[position].onBindView(holder.itemView)
            return
        }

        val i = position - headers.size - (mObjects?.size?:0)
        if (footers.size != 0 && i >= 0) {
            footers[i].onBindView(holder.itemView)
            return
        }
        OnBindViewHolder(holder, position - headers.size)
    }


    fun OnBindViewHolder(holder: HAFViewHolder<T>, position: Int) {
        val item = getItem(position)
        if(item !== null){
            holder.setData(item)
        }
    }


    @Deprecated("")
    override fun getItemViewType(position: Int): Int {
        if (headers.size != 0) {
            if (position < headers.size) return headers[position].hashCode()
        }
        if (footers.size != 0) {
            /*
            eg:
            0:header1
            1:header2   2
            2:object1
            3:object2
            4:object3
            5:object4
            6:footer1   6(position) - 2 - 4 = 0
            7:footer2
             */
            val i = position - headers.size - (mObjects?.size?:0)
            if (i >= 0) {
                return footers[i].hashCode()
            }
        }
        return getViewType(position - headers.size)
    }

    fun getViewType(position: Int): Int {
        return 0
    }


    fun getAllData(): List<T> {
        return ArrayList(mObjects)
    }

    fun getItem(position: Int): T?{
        return mObjects?.get(position)
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    fun getPosition(item: T): Int {
        return mObjects?.indexOf(item) ?: -1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private inner class StateViewHolder(itemView: View) : HAFViewHolder<T>(itemView)

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int): Boolean
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.mItemLongClickListener = listener
    }


    interface ItemView {
        fun onCreateView(parent: ViewGroup): View

        fun onBindView(headerView: View)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    //更多显示和点击的监听
    interface OnMoreListener {
        fun onMoreShow()

        fun onMoreClick()
    }

    //没有更多的显示和触发事件
    interface OnNoMoreListener {
        fun onNoMoreShow()

        fun onNoMoreClick()
    }

    //错误的显示和触发事件
    interface OnErrorListener {
        fun onErrorShow()

        fun onErrorClick()
    }
}