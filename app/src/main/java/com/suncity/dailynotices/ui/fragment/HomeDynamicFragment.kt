package com.suncity.dailynotices.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.OnDynamicItemMenuClick
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.dialog.BottomDialogiOSDynamic
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.lcoperation.Delete
import com.suncity.dailynotices.lcoperation.Increase
import com.suncity.dailynotices.lcoperation.Modify
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Dynamic
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.*
import com.suncity.dailynotices.ui.adapter.DynamicAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.loading.LoadingDialog
import com.suncity.dailynotices.ui.views.recyclerview.DividerDecoration
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_mine_focus.*
import kotlinx.android.synthetic.main.view_empty.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      HomeDynamicFragment
 * @Description:    动态
 */
class HomeDynamicFragment : BaseFragment() {

    private var dynamicAdapter: DynamicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalObserverHelper.addObserver(globalObservable)
    }

    private val errorServer = Config.getString(R.string.str_error_server)
    private val shieldSuccessTip = Config.getString(R.string.str_shield_success_tips)
    private val dividerMargin = DisplayUtils.dip2px(20f)
    private val dividerColor = Config.getColor(R.color.color_f3f3f3)
    private val dividerHeight = Config.getDimension(R.dimen.dp_1).toInt()
    private val IMAGETRANSITION = Config.getString(R.string.image_transition_name)

    companion object {
        fun getInstance(): HomeDynamicFragment {
            return HomeDynamicFragment()
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_home_dynamic
    }

    override fun initData() {
        tv_empty_desc?.text = Config.getString(R.string.str_no_dynamic)
        dynamicAdapter = DynamicAdapter(requireContext())
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.setItemViewCacheSize(20)
        recyclerView?.addItemDecoration(DividerDecoration(dividerColor, dividerHeight, dividerMargin, dividerMargin))
        recyclerView?.adapter = dynamicAdapter

        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {

        smartRefreshLayout?.setOnRefreshListener {
            queryDynamicData(it)
        }

        dynamicAdapter?.setOnItemClickListener(mDynamicItemClick)
        dynamicAdapter?.setOnDynamicItemMenuClick(mDynamicItemMenuClick)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_white)
                .statusBarDarkFont(true, 0f)
                .init()
        }
    }

    /**
     * 1:未登录查询所有的数据
     * 2:已经登录的了，需要过滤 shield 的数据
     * 3:关联的表有 Dynamic , user , shield ,userInfo,like
     */
    private fun queryDynamicData(it: RefreshLayout?) {
        val isLogin = isLogined()
        val userObjectId = PreferenceStorage.userObjectId
        Query.queryDynamicData(isLogin, userObjectId) { dynamicList, avException ->
            it?.finishRefresh()
            if (avException == null && (dynamicList?.size ?: 0) > 0) {
                val sortedList = arrayListOf<Dynamic>()
                sortedList.addAll(dynamicList!!.sortedWith(kotlin.Comparator { o1, o2 ->
                    val time01 = o1.createAt
                    val time02 = o2.createAt
                    val isGreater = DateUtils.compareDate(time01, time02)
                    if (isGreater) -1 else 1
                }))
                dynamicAdapter?.clear()
                dynamicAdapter?.addAll(sortedList)
            } else {
                if (avException != null) {
                    ToastUtils.showSafeToast(requireContext(), errorServer)
                }
            }

            if ((dynamicAdapter?.itemCount ?: 0) > 0) {
                layout_empty?.visibility = View.GONE
            } else {
                layout_empty?.visibility = View.VISIBLE
            }

        }
    }

    private val mDynamicItemClick = object : RecyclerArrayAdapter.OnItemClickListener {

        override fun onItemClick(position: Int, view: View) {
            val item = dynamicAdapter?.getItem(position) ?: return
            DynamicDetailActivity.start(requireContext(), item)
        }

    }

    private val mDynamicItemMenuClick = object : OnDynamicItemMenuClick {

        override fun onMoreClick(position: Int) {
            if (!isLogined()) {
                LoginActivity.start(requireContext(), HomeActivity.POS_HOME)
                return
            }
            val item = dynamicAdapter?.getItem(position)
            val currentObjectId = PreferenceStorage.userObjectId
            val isMine = (item?.idPointer == currentObjectId)
            val dynamicMoreDialog = BottomDialogiOSDynamic(requireContext(), isMine)
            dynamicMoreDialog.show()
            dynamicMoreDialog.setClickCallback(object : BottomDialogiOSDynamic.ClickCallback {

                override fun doDelPost() {
                    //刪除帖子操作
                    val dynamic = item?.objectId ?: return
                    val loadingDialog = LoadingDialog(requireActivity())
                    loadingDialog.setInterceptBack(false).setLoadStyle(LoadingDialog.STYLE_NO_TEXT)
                    loadingDialog.show()
                    Delete.delPostById(dynamic) {
                        loadingDialog.close()
                        if (it != null && !TextUtils.isEmpty(it.message)) {
                            ToastUtils.showSafeToast(requireActivity(), it.message ?: "")
                        } else {
                            dynamicAdapter?.remove(position)
                        }
                    }
                }

                override fun doShieldUserclick() {
                    val idPointer = item?.idPointer ?: return
                    Query.queryShieldUser(idPointer) {
                        if (it) {
                            Increase.createShieldToBack(idPointer) { e ->
                                if (e == null) {
                                    TipDialog.show(
                                        requireContext(), shieldSuccessTip
                                        , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH
                                    )
                                } else {
                                    TipDialog.show(
                                        requireContext(), errorServer
                                        , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                                    )
                                }
                            }
                        }
                    }
                }

                override fun doCancel() {
                }

                override fun doReport() {
                    startActivity(ReportActivity::class.java)
                }

                override fun doComplaint() {
                    ContactServiceActivity.start(requireContext(), ContactServiceActivity.TYPE_COMPLAINT)
                }

            })

        }

        override fun onImageClick(view: View, position: Int, url: String, data: Dynamic) {
            val images = data.images
            if (images == null || images.size == 0) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val option = ActivityOptions.makeSceneTransitionAnimation(activity, view, IMAGETRANSITION)
                val intent = Intent(requireContext(), ImageViewPagerActivity::class.java)
                ImageViewPagerActivity.currentPos = position
                ImageViewPagerActivity.urls = images
                startActivity(intent, option.toBundle())
            } else {
                val intent = Intent(requireContext(), ImageViewPagerActivity::class.java)
                val rect = Rect()
                view.getLocalVisibleRect(rect)
                intent.sourceBounds = rect
                ImageViewPagerActivity.currentPos = position
                ImageViewPagerActivity.urls = images
                startActivity(intent)
                activity?.overridePendingTransition(0, 0)
            }
        }

        // 点赞
        override fun onSelectLikeClick(position: Int, data: Dynamic) {
            val objectId = data.objectId ?: return
            val likeNum = data.likeNum ?: 0
            Modify.updateDynamicLikeNum(objectId, likeNum) { e ->
                LogUtils.e(e.toString())
            }
        }

        override fun onTagFlowClick(position: Int, tagPos: Int, tagString: String) {
        }

    }

    private val globalObservable = object : SimpleGlobalObservable() {

        override fun onLoginSuccess() {
            queryDynamicData(null)
        }

        override fun onLogoutSuccess() {
            queryDynamicData(null)
        }

        override fun onUploadDynamicSuccess() {
            queryDynamicData(null)
        }

        override fun notifyDelPost(postId: String) {
            val allDatas = dynamicAdapter?.getAllData() ?: return
            val delItem = allDatas.find { it.objectId == postId }
            delItem?.let {
                dynamicAdapter?.remove(delItem)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        GlobalObserverHelper.removeObserver(globalObservable)
    }
}