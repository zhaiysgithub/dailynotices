package com.suncity.dailynotices.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_userinfo.*
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      UserInfoFragment
 * @Description:     作用描述
 * @UpdateDate:     12/7/2019
 */

class UserInfoFragment : BaseFragment() {

    private var mList: MutableList<String> = ArrayList()
    private var mAdapter: DemoAdapter? = null

    override fun setContentView(): Int {
        return R.layout.fragment_userinfo
    }

    override fun initData() {
        for (i in 0..99) {
            val item = "HearSilent $i"
            mList.add(item)
        }

        mAdapter = DemoAdapter()
        recyclerView?.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mAdapter
    }

    override fun initListener() {


    }


    inner class DemoAdapter : RecyclerView.Adapter<DemoAdapter.DemoViewHolder>() {

       inner class DemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            var textView: TextView = view.findViewById<View>(R.id.textView) as TextView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_demo, parent, false)
            return DemoViewHolder(view)
        }

        override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
            holder.textView.text = mList[position]
        }

        override fun getItemCount(): Int {
            return mList.size
        }
    }
}