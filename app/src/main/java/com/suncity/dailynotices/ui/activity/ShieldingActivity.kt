package com.suncity.dailynotices.ui.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.suncity.dailynotices.R
import com.suncity.dailynotices.lcoperation.Delete
import com.suncity.dailynotices.lcoperation.Query
import com.suncity.dailynotices.model.Shield
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.ShieldAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_shield.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      ShieldingActivity
 * @Description:    屏蔽过的人
 * @UpdateDate:     5/7/2019
 */
class ShieldingActivity : BaseActivity() {

    private var shieldAdapter : ShieldAdapter? = null
    companion object {
        private val errorServer = Config.getString(R.string.str_error_server)
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true,0f)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_shield
    }


    override fun initData() {
        tv_title_center?.text = Config.getString(R.string.str_title_shield)
        shieldAdapter = ShieldAdapter(this@ShieldingActivity)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this@ShieldingActivity)
        recyclerView?.adapter = shieldAdapter
        smartRefreshLayout?.autoRefresh()
        smartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun initListener() {

        fl_title_back?.setOnClickListener {
            finish()
        }

        smartRefreshLayout?.setOnRefreshListener {
            queryShield(it)
        }

        shieldAdapter?.setOnItemClickListener(mShieldItemClick)
        shieldAdapter?.setOnShieldItemRemoveListener(mShieldRemoveListener)
    }

    private fun queryShield(it: RefreshLayout) {
        val objectId = PreferenceStorage.userObjectId
        Query.queryShield(objectId){shieldList,avException ->
            it.finishRefresh()
            if (avException == null && (shieldList?.size ?: 0) > 0) {
                // TODO 暂时不排序
                shieldAdapter?.clear()
                shieldAdapter?.addAll(shieldList)
            } else {
                if (avException != null){
                    ToastUtils.showSafeToast(this@ShieldingActivity, errorServer)
                }
            }

            if((shieldAdapter?.itemCount ?: 0) > 0){
                layout_empty?.visibility = View.GONE
            }else{
                layout_empty?.visibility = View.VISIBLE
            }

        }
    }

    private val mShieldItemClick = object : RecyclerArrayAdapter.OnItemClickListener{

        override fun onItemClick(position: Int, view: View) {
            if (PreventRepeatedUtils.isFastDoubleClick()) return
            val item = shieldAdapter?.getItem(position)
            if (item?.objectId != null){
                UserInfoActivity.start(this@ShieldingActivity,item.objectId!!)
            }
        }

    }

    private val mShieldRemoveListener = object : ShieldAdapter.OnShieldItemRemoveListener{

        override fun onItemRemoved(data: Shield) {
            //被屏蔽人的 id
            val shieldId = data.objectId
            if (StringUtils.isEmptyOrNull(shieldId)) return
            Delete.deleteShieldById(shieldId!!){ result, avException ->
                if(result != null && avException == null){
                    shieldAdapter?.remove(data)
                }else {
                    if(avException != null){
                        ToastUtils.showSafeToast(this@ShieldingActivity, errorServer)
                    }
                }
                if((shieldAdapter?.itemCount ?: 0) > 0){
                    layout_empty?.visibility = View.GONE
                }else{
                    layout_empty?.visibility = View.VISIBLE
                }
            }

        }

    }
}