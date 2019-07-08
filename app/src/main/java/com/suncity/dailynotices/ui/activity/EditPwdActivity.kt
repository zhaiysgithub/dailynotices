package com.suncity.dailynotices.ui.activity

import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import kotlinx.android.synthetic.main.ac_edit_pwd.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      EditPwdActivity
 * @Description:    修改密码页面
 */
class EditPwdActivity : BaseActivity() {

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .titleBar(R.id.fl_editpwd_del,false)
            .statusBarDarkFont(true,0f)
            .transparentBar()
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_edit_pwd
    }

    override fun initListener() {
        fl_editpwd_del?.setOnClickListener {
            finish()
        }
    }


}