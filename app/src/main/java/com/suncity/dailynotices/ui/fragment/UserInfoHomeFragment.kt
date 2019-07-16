package com.suncity.dailynotices.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.avos.avoscloud.AVObject
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.UserInfoRecord
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.DynamicDetailActivity
import com.suncity.dailynotices.ui.activity.UserInfoActivity
import com.suncity.dailynotices.ui.adapter.DynamicAdapter
import com.suncity.dailynotices.ui.adapter.RecordAdapter
import com.suncity.dailynotices.ui.views.flowlayout.FlowLayout
import com.suncity.dailynotices.ui.views.flowlayout.TagAdapter
import com.suncity.dailynotices.ui.views.flowlayout.TagFlowLayout
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.ProgressUtil
import com.suncity.dailynotices.utils.StringUtils
import kotlinx.android.synthetic.main.fragment_userinfo.*
import java.lang.Exception

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      UserInfoFragment
 * @Description:    用户中心的主页fragment
 * @UpdateDate:     12/7/2019
 */

class UserInfoHomeFragment : BaseFragment() {

    private val recordSecret = "保密"

    companion object {
        private const val ARGUMENT_TAG = "argument_tag"
        fun getInstance(userInfo: AVObject?): UserInfoHomeFragment {
            val userInfoHomeFragment = UserInfoHomeFragment()
            if (userInfo != null) {
                val bundle = Bundle()
                bundle.putParcelable(ARGUMENT_TAG, userInfo)
                userInfoHomeFragment.arguments = bundle
            }
            return userInfoHomeFragment
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_userinfo
    }

    /**
     * <!--标签-->   dynamic
     * <!--个人档案--> userInfo
     * <!--形象风格--> dynamic - style
     * <!--兴趣特长--> userInfo
     * <!--演绎动态-->  dynamic
     */
    @SuppressLint("SetTextI18n")
    override fun initData() {
        val userInfo = arguments?.getParcelable<AVObject>(ARGUMENT_TAG)
        val userObjcetId = userInfo?.getString("user") ?: return
        if (activity is UserInfoActivity && activity != null && activity?.isFinishing == false) {
            ProgressUtil.showProgress(activity!!, "", "请稍等")
        }
        //查询动态
        Query.queryUserDynamicById(userObjcetId) { dynamicList, _ ->
            ProgressUtil.hideProgress()
            //标签,演绎动态内容
            if (dynamicList != null && dynamicList.size > 0) {
                val tags = arrayListOf<String>()
                val stylesList = arrayListOf<String>()
                dynamicList.forEach {
                    val skillTag = it.skill
                    if (StringUtils.isNotEmptyAndNull(skillTag) && !tags.contains(skillTag)) {
                        tags.add(skillTag!!)
                    }
                    val styleTag = it.style
                    if (StringUtils.isNotEmptyAndNull(styleTag) && !tags.contains(styleTag)) {
                        tags.add(styleTag!!)
                    }
                    if (StringUtils.isNotEmptyAndNull(styleTag) && !stylesList.contains(styleTag)) {
                        stylesList.add(styleTag!!)
                    }
                }
                //设置 tag
                val tagSize = tags.size
                tagVisiable(tagSize > 0)
                if (tagSize > 0) {
                    tv_tag_num?.text = "·$tagSize"
                    setTags(tagFlowLayout_tag, tags)
                }
                // 设置 style
                val styleSize = stylesList.size
                styleVisiable(styleSize > 0)
                if (styleSize > 0) {
                    tv_style_num?.text = "·$styleSize"
                    setTags(tagFlowLayout_style, stylesList)
                }
                //设置动态
                val dynamicAdapter = DynamicAdapter(requireContext())
                recyclerView_userinfo_dynamic?.setHasFixedSize(true)
                recyclerView_userinfo_dynamic?.isNestedScrollingEnabled = false
                recyclerView_userinfo_dynamic?.layoutManager = LinearLayoutManager(requireContext())
                recyclerView_userinfo_dynamic?.adapter = dynamicAdapter
                dynamicAdapter.addAll(dynamicList)
                dynamicAdapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int, view: View) {
                        val item = dynamicAdapter?.getItem(position) ?: return
                        DynamicDetailActivity.start(requireContext(), item)
                    }

                })
            } else {
                tagVisiable(false)
                styleVisiable(false)
                dynamicVisiable(false)
            }
        }
        //设置个人档案和兴趣特长
        val recordAdapter = RecordAdapter(requireContext())
        recyclerView_record?.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 7 || position == 8) {
                    return 2
                }
                return 1
            }
        }
        recyclerView_record?.layoutManager = gridLayoutManager
        recyclerView_record?.isNestedScrollingEnabled = false
        recyclerView_record?.adapter = recordAdapter
        setRecordData(recordAdapter, userInfo)

        val interestJson = userInfo.getJSONArray("interest")
        if (interestJson == null) {
            interestVisiable(false)
        } else {
            val interestList = JSON.parseArray(interestJson.toString(), String::class.javaObjectType)
            val interestSize = interestList.size
            val preInterestList = mutableListOf<String>()
            interestVisiable(interestSize > 0)
            if (interestSize > 0) {
                tv_interest_num?.text = "·$interestSize"
                interestList.forEach {
                    val preInterest = "# $it"
                    preInterestList.add(preInterest)
                }
                setTags(tagFlowLayout_interest, preInterestList)
            }
        }

    }

    /**
     * 设置个人档案数据
     */
    private fun setRecordData(recordAdapter: RecordAdapter, userInfo: AVObject) {
        val recordList = arrayListOf<UserInfoRecord>()
        //身高
        var height = userInfo.getString("height")
        height = if (StringUtils.isEmptyOrNull(height)) {
            recordSecret
        } else {
            "$height cm"
        }
        //体重
        var weight = userInfo.getString("weight")
        weight = if (StringUtils.isEmptyOrNull(weight)) {
            recordSecret
        } else {
            "$weight kg"
        }
        //鞋码
        var shoes = userInfo.getString("shoeSize")
        shoes = if (StringUtils.isEmptyOrNull(shoes)) {
            recordSecret
        } else {
            "$shoes cm"
        }
        //三围
        val bwh: String
        bwh = try {
            val jsonArray = userInfo.getJSONArray("bwh")
            if (jsonArray == null) {
                recordSecret
            } else {
                val arrList: MutableList<String> =
                    JSONObject.parseArray(jsonArray.toString(), String::class.javaObjectType) ?: mutableListOf()
                if (arrList.contains("0") || arrList.size != 3) {
                    recordSecret
                } else {
                    "${arrList[0]}cm  ${arrList[1]}cm  ${arrList[2]}cm"
                }
            }
        } catch (e: Exception) {
            recordSecret
        }

        //国籍
        var nationality = userInfo.getString("nationality")
        if (StringUtils.isEmptyOrNull(nationality)) {
            nationality = recordSecret
        }
        //民族
        var nation = userInfo.getString("nation")
        if (StringUtils.isEmptyOrNull(nation)) {
            nation = recordSecret
        }
        //籍贯
        var native = userInfo.getString("native")
        if (StringUtils.isEmptyOrNull(native)) {
            native = recordSecret
        }
        //毕业院校
        var graduation = userInfo.getString("graduation")
        if (StringUtils.isEmptyOrNull(graduation)) {
            graduation = recordSecret
        }
        //生日
        var birthday = userInfo.getString("birthday")
        if (StringUtils.isEmptyOrNull(birthday)) {
            birthday = recordSecret
        }

        recordList.add(UserInfoRecord("身高:", height))
        recordList.add(UserInfoRecord("体重:", weight))
        recordList.add(UserInfoRecord("鞋码:", shoes))
        recordList.add(UserInfoRecord("国籍:", nationality))
        recordList.add(UserInfoRecord("民族:", nation))
        recordList.add(UserInfoRecord("籍贯:", native))
        recordList.add(UserInfoRecord("毕业院校:", graduation))
        recordList.add(UserInfoRecord("生日:", birthday))
        recordList.add(UserInfoRecord("三围:", bwh))
        recordAdapter.addAll(recordList)
    }

    override fun initListener() {


    }


    private fun tagVisiable(isVisible: Boolean) {
        layout_tag?.visibility = if (isVisible) View.VISIBLE else View.GONE
        tagFlowLayout_tag?.visibility = if (isVisible) View.VISIBLE else View.GONE
        view_line_tag?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun styleVisiable(isVisible: Boolean) {
        layout_style?.visibility = if (isVisible) View.VISIBLE else View.GONE
        tagFlowLayout_style?.visibility = if (isVisible) View.VISIBLE else View.GONE
        view_line_style?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun interestVisiable(isVisible: Boolean) {
        layout_interest?.visibility = if (isVisible) View.VISIBLE else View.GONE
        tagFlowLayout_interest?.visibility = if (isVisible) View.VISIBLE else View.GONE
        view_line_interest?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun dynamicVisiable(isVisible: Boolean) {
        recyclerView_userinfo_dynamic?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setTags(tagLayout: TagFlowLayout?, tags: MutableList<String>) {
        if (tags.size > 0) {
            val tagAdapter = createTagAdapter(tags)
            tagLayout?.mAdapter = tagAdapter
        }
    }


    private fun createTagAdapter(stringList: MutableList<String>): TagAdapter<String> {
        return object : TagAdapter<String>(stringList) {

            override fun getView(parent: FlowLayout, position: Int, t: String): View {

                val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.tag_item, parent, false)
                val textView = view.findViewById<TextView>(com.suncity.dailynotices.R.id.tv_text)
                textView.text = t
                return view
            }

        }
    }


}