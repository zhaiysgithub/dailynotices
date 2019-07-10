package com.suncity.dailynotices.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.avos.avoscloud.AVUser
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.dialog.ActionSheetDialog
import com.suncity.dailynotices.dialog.AlertDialog
import com.suncity.dailynotices.manager.UserManager
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.SettingAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.model.SettingModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_setting.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SettingActivity
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */
class SettingActivity : BaseActivity() {

    companion object {

        private val STR_ACCOUNTMANAGER = Config.getString(R.string.str_accountmanager)
        private val STR_CACHECLEAR = Config.getString(R.string.str_cacheclear)
        private val STR_LOGOUT = Config.getString(R.string.str_exit)
        private val STR_TIP = Config.getString(R.string.str_tip)
        private val STR_POSITIVE = Config.getString(R.string.str_positive)
        private val STR_CANCEL = Config.getString(R.string.str_cancel)
        private val msg = "确认清除缓存?"
    }

    private var adapter: SettingAdapter? = null
    private val dataList = arrayListOf<SettingModel>()

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_setting
    }

    override fun initData() {
        tv_title_center.text = Config.getString(R.string.str_setting)

        val datas = setAdapterData()
        adapter = SettingAdapter(this@SettingActivity)
        recyclerView_setting?.setHasFixedSize(true)
        recyclerView_setting?.layoutManager = LinearLayoutManager(this)
        recyclerView_setting?.adapter = adapter
        adapter?.clear()
        adapter?.addAll(datas)

    }

    private fun setAdapterData(): List<SettingModel> {
        dataList.clear()
        val accountManager = SettingModel(STR_ACCOUNTMANAGER, "")
        val cacheClear = SettingModel(STR_CACHECLEAR, "")
        dataList.add(accountManager)
        dataList.add(cacheClear)
        return dataList
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }

        adapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (position in 0 until dataList.size){
                    when (position) {
                        0 -> {
                            LogUtils.e(STR_ACCOUNTMANAGER)
                            startActivity(SettingEditActivity::class.java)
                        }
                        1 -> {
//                            val value = dataList[1].value_set
//                            if (StringUtils.isNotEmptyAndNull(value)){
//
//                            }
                                AlertDialog(this@SettingActivity).builder().setTitle(STR_TIP)
                                    .setMsg(msg)
                                    .setCancelable(false)
                                    .setPositiveButton(STR_POSITIVE, View.OnClickListener {
                                        ToastUtils.showSafeToast(this@SettingActivity,msg)
                                    }).setNegativeButton(STR_CANCEL,View.OnClickListener {}).show()
                        }
                    }
                }

            }
        })

        tv_logout?.setOnClickListener {
            ActionSheetDialog(this@SettingActivity).builder().setCancelable(true)
                .setCancelable(true)
                .addSheetItem(STR_LOGOUT, R.color.color_037bff
                    , object : ActionSheetDialog.OnSheetItemClickListener {
                        override fun onClick(which: Int) {
                            doLogout()
                        }
                    }).show()
        }
    }

    private fun doLogout() {
//        val currentUser = AVUser.getCurrentUser()
        AVUser.logOut()
        UserManager.removeUserInfo()
//        val user = AVUser.getCurrentUser()
        finish()
    }
}