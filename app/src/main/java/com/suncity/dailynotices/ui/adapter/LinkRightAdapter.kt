package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.RightBean
import com.suncity.dailynotices.ui.views.flowlayout.FlowLayout
import com.suncity.dailynotices.ui.views.flowlayout.TagAdapter
import com.suncity.dailynotices.ui.views.flowlayout.TagFlowLayout
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      LinkRightAdapter
 * @Description:     作用描述
 * @UpdateDate:     22/7/2019
 */
class LinkRightAdapter(context: Context) : RecyclerArrayAdapter<RightBean>(context) {

    private val mInflater = LayoutInflater.from(context)
    var checkContentResult:String? = null

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): HAFViewHolder<RightBean> {
        return LinkTagViewHolder(parent, R.layout.adapter_link_item_tag)
    }



    inner class LinkTagViewHolder(parent: ViewGroup, resLayoutId: Int) : HAFViewHolder<RightBean>(parent, resLayoutId) {

        private var tagLayout: TagFlowLayout? = null
        private var tag_title: TextView? = null

        init {
            tagLayout = itemView.findViewById(R.id.tagFlowLayout)
            tag_title = itemView.findViewById(R.id.tag_title)
        }

        override fun setData(data: RightBean) {
            tag_title?.text = data.titleName
            val tagList = data.names
            if (tagList != null && tagList.size > 0) {
                val tagAdapter = createTagAdapter(tagList)
                tagLayout?.mAdapter = tagAdapter
            }

            tagLayout?.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
                override fun onTagClick(view: View, position: Int, parent: FlowLayout): Boolean {
                    if (listener != null && !PreventRepeatedUtils.isFastDoubleClick()) {
                        listener?.onTagClick(adapterPosition, (tagList?.get(position) ?: ""))
                    }
                    return true
                }

            })
        }


        private fun createTagAdapter(stringList: ArrayList<String>): TagAdapter<String> {
            return object : TagAdapter<String>(stringList) {

                override fun getView(parent: FlowLayout, position: Int, t: String): View {

                    val view: View = mInflater.inflate(R.layout.tag_skill_item, parent, false)
                    val textView = view.findViewById<TextView>(R.id.tv_text)
                    textView.text = t
                    return view
                }

            }
        }
    }

    private var listener: OnTagClickListener? = null

    fun setOnTagClickListener(l: OnTagClickListener) {
        listener = l
    }

    interface OnTagClickListener {

        fun onTagClick(pos: Int, name: String)
    }
}