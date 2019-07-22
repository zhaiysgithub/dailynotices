package com.suncity.dailynotices.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.RightBean
import com.suncity.dailynotices.model.SortBean
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.activity.PushDynamicActivity.Companion.TYPE_ACTING
import com.suncity.dailynotices.ui.activity.PushDynamicActivity.Companion.TYPE_SKILL
import com.suncity.dailynotices.ui.activity.PushDynamicActivity.Companion.TYPE_STYLE
import com.suncity.dailynotices.ui.adapter.LinkLeftAdapter
import com.suncity.dailynotices.ui.adapter.LinkRightAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.StringUtils
import kotlinx.android.synthetic.main.ac_acting_skill.*
import kotlinx.android.synthetic.main.view_title.*
import java.io.IOException

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SkillStyleActivity
 * @Description:     作用描述
 */
class SkillStyleActivity : BaseActivity() {

    private var mSortBean: SortBean? = null
    private var mSortAdapter: LinkLeftAdapter? = null
    private var mRightdapter: LinkRightAdapter? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    override fun setScreenManager() {

        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_acting_skill
    }

    override fun initData() {
        val type = intent.getStringExtra(TYPE_ACTING)
        tv_title_center?.text = when (type) {
            TYPE_SKILL -> "演艺技能"
            TYPE_STYLE -> "形象风格"
            else -> "形象风格"
        }
        val jsonResult = if (type == TYPE_SKILL) {
            getAssestData("skills.json")
        } else {
            getAssestData("styles.json")
        }

        val gson = Gson()
        mSortBean = gson.fromJson<SortBean>(jsonResult, SortBean::class.java)
        val categoryOneArray = mSortBean?.data
        val list = arrayListOf<String>()
        val rightBeanList = arrayListOf<RightBean>()
        categoryOneArray?.let {
            it.forEach { bean ->
                val right = RightBean()
                if (StringUtils.isNotEmptyAndNull(bean.name)) {
                    list.add(bean.name!!)
                    right.titleName = bean.name!!
                }

                val rightNameList = arrayListOf<String>()
                bean.itemdata?.let { itemDataArray ->
                    itemDataArray.forEach { itemData ->
                        rightNameList.add((itemData.name ?: ""))
                    }
                }
                right.names = rightNameList

                rightBeanList.add(right)
            }

        }

        mSortAdapter = LinkLeftAdapter(this)
        recyclerView_left?.setHasFixedSize(true)
        recyclerView_left?.layoutManager = LinearLayoutManager(this)
        recyclerView_left?.adapter = mSortAdapter
        mSortAdapter?.addAll(list)


        mRightdapter = LinkRightAdapter(this)
        recyclerView_right?.setHasFixedSize(true)
        recyclerView_right?.layoutManager = LinearLayoutManager(this)
        recyclerView_right?.adapter = mRightdapter
        mRightdapter?.addAll(rightBeanList)
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
        mSortAdapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {
                mSortAdapter?.checkedPosition = position
                mSortAdapter?.notifyDataSetChanged()
                //TODO 联动右边
            }

        })

        mRightdapter?.setOnTagClickListener(object : LinkRightAdapter.OnTagClickListener {
            override fun onTagClick(pos: Int, name: String) {
                LogUtils.e("onTagClick -> pos=$pos,name=$name")
                //TODO 联动左边
            }

        })
    }

    //从资源文件中获取分类json
    private fun getAssestData(path: String): String {
        var result = ""
        try {
            //获取输入流
            val mAssets = assets.open(path)
            //获取文件的字节数
            val lenght = mAssets.available()
            //创建byte数组
            val buffer = ByteArray(lenght)
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer)
            mAssets.close()
            result = String(buffer)
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtils.e("getAssetsData -> $e")
            return result
        }
    }


    //将当前选中的item居中
    private fun moveToCenter(position: Int) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        val childAt =
            recyclerView_left?.getChildAt(position - (mLinearLayoutManager?.findFirstVisibleItemPosition() ?: 0))
        if (childAt != null) {
            val height = recyclerView_left?.height ?: 0
            val y = childAt.top - height / 2
            recyclerView_left?.smoothScrollBy(0, y)
        }

    }
}