package com.suncity.dailynotices.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.suncity.dailynotices.R
import com.suncity.dailynotices.model.Notice
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_notice_detail.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      NoticeDetailActivity
 * @Description:     通告详情页
 * @UpdateDate:     16/7/2019
 */
class NoticeDetailActivity : BaseActivity() {

    private var notice: Notice? = null

    companion object {
        private const val BUNDLE_NOTICE = "bundle_notice"
        fun start(context: Context, notice: Notice) {
            val intent = Intent()
            intent.setClass(context, NoticeDetailActivity::class.java)
            intent.putExtra(BUNDLE_NOTICE, notice)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true,0f)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_notice_detail
    }


    override fun initData() {
        notice = intent?.getSerializableExtra(BUNDLE_NOTICE) as Notice
        tv_title_center?.text = "通告详情"

        title_notice_detail?.text = notice?.title
        date_notice_detail?.text = "${notice?.beginTime}发布 | ${notice?.endTime}截止"
        sales_notice_detail?.text = "${notice?.payment}元/人"
        worktime_notice_detail?.text = "工作时间 ${notice?.workTime}"
        workaddress_notice_detail?.text = notice?.workPlace
        tv_notice_detail?.text = notice?.contents
        val image = notice?.image
        if(StringUtils.isNotEmptyAndNull(image)){
            draweeView_discovery_big?.setImageURI(image)
        }else{
            draweeView_discovery_big?.visibility = View.GONE
        }
        val avatarUrl = notice?.userAvatar
        if(StringUtils.isNotEmptyAndNull(avatarUrl)){
            draweeView_notice_detail?.setImageURI(avatarUrl)
        }else{
            draweeView_notice_detail?.visibility = View.GONE
        }
        username_notice_detail?.text = notice?.userName
        val userCreateAt = notice?.userCreateAt
        if(userCreateAt != null){
            userdate_notice_detail?.text = DateUtils.formatDateToM(userCreateAt)
        }else{
            userdate_notice_detail?.visibility = View.GONE
        }
        val endTime = notice?.endTime
        if(endTime != null){
            val isPast = DateUtils.compareDate(endTime)
            if(isPast){
                commun_notice_detail?.text = "已过期"
                commun_notice_detail?.setTextColor(Config.getColor(R.color.color_888))
                commun_notice_detail?.setBackgroundResource(R.drawable.shape_mark_past_notice_detail)
            }else{
                commun_notice_detail?.text = "立即沟通"
                commun_notice_detail?.setTextColor(Config.getColor(R.color.color_222))
                commun_notice_detail?.setBackgroundResource(R.drawable.shape_submit_checked_bg)
            }
        }


    }

    override fun initListener() {
        layout_title?.setOnClickListener {
            finish()
        }
        layout_user_notice_detail?.setOnClickListener {
            val userId = notice?.userId ?: return@setOnClickListener
            UserInfoActivity.start(this@NoticeDetailActivity,userId)
        }

        commun_notice_detail?.setOnClickListener {
            val userId = notice?.userId ?: return@setOnClickListener
            startOpenLCIM(userId)
        }
    }

    private fun startOpenLCIM(objectId: String){
        if (null == LCChatKit.getInstance().client){
            startLoginChatKit(objectId)
        }else{
            startLCIM(objectId)
        }
    }

    private fun startLoginChatKit(objectId: String){
        LCChatKit.getInstance().open(objectId, object : AVIMClientCallback() {

            override fun done(client: AVIMClient?, e: AVIMException?) {
                LogUtils.e("LCChatKit.getInstance().open -> $e")
                if(e == null){
                    startLCIM(objectId)
                }else{
                    ToastUtils.showSafeToast(this@NoticeDetailActivity,"登录过期请重新登录")
                    LoginActivity.start(this@NoticeDetailActivity,-1)
                }
            }

        })
    }

    private fun startLCIM(bjectId: String){
        val intent = Intent(this, LCIMConversationActivity::class.java)
        intent.putExtra(LCIMConstants.PEER_ID,bjectId)
        startActivity(intent)
    }
}