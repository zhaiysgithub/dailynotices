package com.suncity.dailynotices.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.avos.avoscloud.AVObject
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.lcoperation.Modify
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.ImageViewPagerActivity
import com.suncity.dailynotices.ui.adapter.UserInfoPicAdapter
import com.suncity.dailynotices.ui.dialog.NormalDialogUtils
import com.suncity.dailynotices.ui.views.recyclerview.RecycleGridDivider
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.DisplayUtils
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_userinfo_pic.*
import kotlinx.android.synthetic.main.view_empty.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      UserInfoPicFragment
 * @Description:     作用描述
 * @UpdateDate:     15/7/2019
 */
class UserInfoPicFragment : BaseFragment() {

    private var mAdapter: UserInfoPicAdapter? = null
    private var imgs: ArrayList<String>? = null
    private var space_dimen = DisplayUtils.dip2px(2f)
    private var space_color = Config.getColor(android.R.color.transparent)

    companion object {
        private const val ARGUMENT_TAG = "argument_tag"
        private val IMAGETRANSITION = Config.getString(R.string.image_transition_name)
        fun getInstance(userInfo: AVObject?): UserInfoPicFragment {
            val userInfoPicFragment = UserInfoPicFragment()
            if (userInfo != null) {
                val bundle = Bundle()
                bundle.putParcelable(ARGUMENT_TAG, userInfo)
                userInfoPicFragment.arguments = bundle
            }
            return userInfoPicFragment
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_userinfo_pic
    }

    override fun initData() {
        GlobalObserverHelper.addObserver(picObservable)
        mAdapter = UserInfoPicAdapter(requireContext())
        recyclerView_userinfo_pic?.setHasFixedSize(true)
        recyclerView_userinfo_pic?.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView_userinfo_pic?.addItemDecoration(RecycleGridDivider(space_dimen, space_color))
        recyclerView_userinfo_pic?.adapter = mAdapter

        tv_empty_desc?.text = Config.getString(R.string.str_userinfopic_empty)
        iv_empty?.setImageResource(R.mipmap.ico_empty_holder)

        val userInfo = arguments?.getParcelable<AVObject>(ARGUMENT_TAG) ?: return
        val userId = userInfo.getString("user")
        Query.queryHomeImagesByUserid(userId) { imgList, _ ->

            if (imgList != null && imgList.size > 0) {
                Log.e("@@@", "imgList = $imgList")
                imgs = imgList
                mAdapter?.addAll(imgList)
                recyclerView_userinfo_pic?.visibility = View.VISIBLE
                layout_empty?.visibility = View.GONE
            } else {
                if (mAdapter?.itemCount == 0) {
                    recyclerView_userinfo_pic?.visibility = View.GONE
                    layout_empty?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun initListener() {
        mAdapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {

            override fun onItemClick(position: Int, view: View) {
                if (imgs == null || imgs?.size == 0) return
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val option =
                        ActivityOptions.makeSceneTransitionAnimation(activity, view, IMAGETRANSITION)
                    val intent = Intent(activity, ImageViewPagerActivity::class.java)
                    ImageViewPagerActivity.currentPos = position
                    ImageViewPagerActivity.urls = imgs!!.toMutableList()
                    startActivity(intent, option.toBundle())
                } else {
                    val intent = Intent(activity, ImageViewPagerActivity::class.java)
                    val rect = Rect()
                    view.getLocalVisibleRect(rect)
                    intent.sourceBounds = rect
                    ImageViewPagerActivity.currentPos = position
                    ImageViewPagerActivity.urls = imgs!!.toMutableList()
                    startActivity(intent)
                    activity?.overridePendingTransition(0, 0)
                }
            }

        })

    }

    private val picObservable = object : SimpleGlobalObservable() {

        override fun onUserPicUpdateListener(picLocalPaths: ArrayList<String>) {
            NormalDialogUtils.showTextDialog(requireContext(), "正在上传中...")
            Modify.updateHomeImageList(PreferenceStorage.userObjectId, picLocalPaths) { urlList, e ->
                NormalDialogUtils.dismissNormalDialog()
                if (e == null) {
                    ToastUtils.showSafeToast(requireContext(), "上传成功")
                } else {
                    ToastUtils.showSafeToast(requireContext(), Constants.ERROR_MSG)
                }
                mAdapter?.addAll(urlList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        GlobalObserverHelper.removeObserver(picObservable)
    }
}