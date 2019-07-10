package com.suncity.dailynotices.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.SettingAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.model.SettingModel
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreferenceStorage
import com.suncity.dailynotices.utils.UIUtils
import kotlinx.android.synthetic.main.ac_setting_edit.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      SettingEditActivity
 * @Description:     作用描述
 * @UpdateDate:     10/7/2019
 */
class SettingEditActivity : BaseActivity() {

    companion object {
        private val STR_YET_BINDPHONNUM = Config.getString(R.string.str_yet_bind_phonenum)
        private val STR_PWD_EDIT = Config.getString(R.string.str_pwd_edit)
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .init()
    }

    private var adapter: SettingAdapter? = null
    private val dataList = arrayListOf<SettingModel>()

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_setting_edit
    }

    override fun initData() {
        tv_title_center.text = Config.getString(R.string.str_setting)

        val datas = setAdapterData()
        adapter = SettingAdapter(this@SettingEditActivity)
        recyclerView_setting_edit?.setHasFixedSize(true)
        recyclerView_setting_edit?.layoutManager = LinearLayoutManager(this)
        recyclerView_setting_edit?.adapter = adapter
        adapter?.clear()
        adapter?.addAll(datas)
    }

    private fun setAdapterData(): List<SettingModel> {
        dataList.clear()
        val encryPhone = UIUtils.getEncryPhone(PreferenceStorage.userPhoneNum)
        val yetBindPhoneNum = SettingModel(STR_YET_BINDPHONNUM, encryPhone)
        val pwdEdit = SettingModel(STR_PWD_EDIT, "")
        dataList.add(yetBindPhoneNum)
        dataList.add(pwdEdit)
        return dataList
    }

    override fun initListener() {

        fl_title_back?.setOnClickListener {
            finish()
        }

        adapter?.setOnItemClickListener(object : RecyclerArrayAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                if (position in 0 until dataList.size) {
                    when (position) {
                        0 -> {
                            startActivity(SettingEditPhoneActivity::class.java)
                        }
                        1 -> {
                            startActivity(SettingEditPwdActivity::class.java)
                        }
                    }
                }

            }
        })
    }
}