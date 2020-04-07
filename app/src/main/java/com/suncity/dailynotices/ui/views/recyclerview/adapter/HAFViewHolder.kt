package com.suncity.dailynotices.ui.views.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * M为这个itemView对应的model。
 * 使用RecyclerArrayAdapter就一定要用这个ViewHolder。
 * 这个ViewHolder将ItemView与Adapter解耦。
 * 推荐子类继承第二个构造函数。并将子类的构造函数设为一个ViewGroup parent。
 * 然后这个ViewHolder就完全独立。adapter在new的时候只需将parentView传进来。View的生成与管理由ViewHolder执行。
 * 实现setData来实现UI修改。Adapter会在onCreateViewHolder里自动调用。
 * <p>
 * 在一些特殊情况下，只能在setData里设置监听。
 */

abstract class HAFViewHolder<M> : RecyclerView.ViewHolder{

    constructor(itemView:View) : super(itemView)

    constructor(parent:ViewGroup,res:Int):super(LayoutInflater.from(parent.context).inflate(res,parent,false))

    open fun setData(data:M){

    }

    fun getContext():Context{
        return itemView.context
    }

    fun getDataPosition() : Int{
        val adapter = getOwnerAdapter<RecyclerView.Adapter<RecyclerView.ViewHolder>>()
        if (adapter != null && adapter is RecyclerArrayAdapter<*>){
            return adapterPosition - adapter.getHeaderCount()
        }
        return adapterPosition
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : RecyclerView.Adapter<RecyclerView.ViewHolder>> getOwnerAdapter():T?{
        val recyclerView = getOwnerRecyclerView()
        return if (recyclerView == null) null else recyclerView.adapter as T
    }

    private fun getOwnerRecyclerView():RecyclerView?{
        try {
            val field = RecyclerView.ViewHolder::class.java.getDeclaredField("mOwnerRecyclerView")
            field.isAccessible = true
            return field.get(this@HAFViewHolder) as RecyclerView
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return null
    }
}