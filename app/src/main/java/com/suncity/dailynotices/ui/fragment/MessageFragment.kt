package com.suncity.dailynotices.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.cache.LCIMConversationItemCache
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.callback.SimpleGlobalObservable
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.ui.activity.HomeActivity
import com.suncity.dailynotices.ui.activity.LoginActivity
import com.suncity.dailynotices.ui.adapter.MessageAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.PreventRepeatedUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.fg_msg.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.fragment
 * @ClassName:      MessageFragment
 * @Description:    消息的fragments
 */

class MessageFragment : BaseFragment() {


    private var msgAdapter: MessageAdapter? = null

    companion object {
        fun getInstance(): MessageFragment {
            return MessageFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalObserverHelper.addObserver(mObservable)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LogUtils.e("MessageFragment -> hidden = $hidden")
        if (!hidden) {
            ImmersionBar.with(this)
                .statusBarColor(R.color.color_white)
                .statusBarDarkFont(true, 0f)
                .init()
            if (!isLogined()) {
                LoginActivity.start(requireContext(),HomeActivity.POS_MSG)
            } else {
                updateConversationList()
            }
        }
    }

    private fun updateConversationList(refreshLayout: RefreshLayout? = null, isLoadMore: Boolean? = false) {
        //获取对应的对话列表
        val currentUser = AVUser.getCurrentUser() ?: return
        val client = AVIMClient.getInstance(currentUser)
        client?.open(object : AVIMClientCallback() {

            override fun done(client: AVIMClient?, e: AVIMException?) {
                if (e == null) {
                    val conversationsQuery = client?.conversationsQuery
                    conversationsQuery?.orderByDescending("updatedAt")
                    conversationsQuery?.isWithLastMessagesRefreshed = true
                    if(isLoadMore == true){
                        conversationsQuery?.skip((msgAdapter?.itemCount ?: 0))
                    }
                    conversationsQuery?.findInBackground(object : AVIMConversationQueryCallback() {
                        override fun done(conversations: MutableList<AVIMConversation>?, e: AVIMException?) {
                            refreshLayout?.finishRefresh()
                            if (e == null && (conversations?.size ?: 0) > 0) {
                                msgAdapter?.clear()
                                val size = conversations?.size ?: 0
                                if (size >= 10) smartRefreshLayout?.setEnableLoadMore(true) else smartRefreshLayout?.setEnableLoadMore(
                                    false
                                )
                                msgAdapter?.addAll(conversations)
                                updateUI()
                            } else {
                                updateUI()
                            }
                        }
                    })
                } else {
                    refreshLayout?.finishRefresh()
                    updateUI()
                }
            }
        })
    }

    private fun updateUI() {
        if (msgAdapter?.itemCount == 0) {
            layout_empty?.visibility = View.VISIBLE
        } else {
            layout_empty?.visibility = View.GONE
        }
    }

    override fun setContentView(): Int {
        return R.layout.fg_msg
    }


    override fun initData() {

        fl_title_back?.visibility = View.GONE
        tv_title_center?.text = "消息"

        msgAdapter = MessageAdapter(requireContext())
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = msgAdapter
    }

    private val mObservable = object : SimpleGlobalObservable() {

        override fun onLoginSuccess() {
            LogUtils.e("onLoginSuccess")
            updateConversationList()
        }

        override fun onUpdateUserinfoSuccess() {
            LogUtils.e("onUpdateUserinfoSuccess")
            updateConversationList()
        }

        override fun onLogoutSuccess() {
            LogUtils.e("onLogoutSuccess")
            msgAdapter?.clear()
        }
    }

    override fun initListener() {
        msgAdapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if(PreventRepeatedUtils.isFastDoubleClick()) return
                val item = msgAdapter?.getItem(position) ?: return
                if (null == LCChatKit.getInstance().client){
                    startLoginChatKit(AVUser.getCurrentUser().objectId,item)
                }else{
                   startLCIM(item)
                }

            }

        })
        smartRefreshLayout?.setOnRefreshListener {
            if (!isLogined()) {
                LoginActivity.start(requireContext(),HomeActivity.POS_MSG)
            } else {
                updateConversationList(it)
            }
        }

        smartRefreshLayout?.setOnLoadMoreListener {
            if (!isLogined()) {
                LoginActivity.start(requireContext(),HomeActivity.POS_MSG)
            } else {
                updateConversationList()
            }
        }
    }

    private fun startLCIM(item:AVIMConversation){
        val intent = Intent(requireContext(),LCIMConversationActivity::class.java)
        intent.putExtra(LCIMConstants.CONVERSATION_ID,item.conversationId)
        requireContext().startActivity(intent)
    }

    private fun startLoginChatKit(objectId: String,item: AVIMConversation){
        LCChatKit.getInstance().open(objectId, object : AVIMClientCallback() {

            override fun done(client: AVIMClient?, e: AVIMException?) {
                LogUtils.e("LCChatKit.getInstance().open -> $e")
                if(e == null){
                    startLCIM(item)
                }else{
                    ToastUtils.showSafeToast(requireContext(),"登录过期请重新登录")
                    LoginActivity.start(requireContext(),HomeActivity.POS_MSG)
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if (isLogined()) {
            updateConversationList()
        }
    }

    private fun updateCacheConversationList() {
        val convIdList = LCIMConversationItemCache.getInstance().sortedConversationList
        val conversationList = ArrayList<AVIMConversation>()
        for (convId in convIdList) {
            val conversation = LCChatKit.getInstance().client?.getConversation(convId)
            if(conversation != null){
                conversationList.add(conversation)
            }
        }
        if(conversationList.size > 0){
            msgAdapter?.clear()
            msgAdapter?.addAll(conversationList)
        }


    }
}