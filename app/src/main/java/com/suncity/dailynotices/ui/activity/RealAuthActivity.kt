package com.suncity.dailynotices.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.dialog.OnDismissListener
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.lcoperation.Modify
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_real_auth.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      RealAuthActivity
 * @Description:    实名认证
 * TODO 认证存在问题,切换用户导致本地的存储数据会出现异常
 */
class RealAuthActivity : BaseActivity() {

    private var isCertificateAuth = false
    private val state_unauth = Config.getString(R.string.str_unauth)
    private val state_authed = Config.getString(R.string.str_authed)
    private val REQUEST_CERTIFICATE_CODE = 0
    private val erroMsg = Config.getString(R.string.str_error_server)

    companion object {
        private const val isAuth = "isauth"

        fun start(context: Context, auth: Boolean) {
            val intent = Intent()
            intent.setClass(context, RealAuthActivity::class.java)
            intent.putExtra(isAuth, auth)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_real_auth
    }

    override fun initData() {

        tv_title_center?.text = "实名认证"
        isCertificateAuth = intent?.getBooleanExtra(isAuth, false) ?: false
        if (isCertificateAuth) {
            tv_state_auth?.text = state_authed
            val authName = PreferenceStorage.authName
            if (StringUtils.isNotEmptyAndNull(authName)) {
                et_real_name?.setText(authName)
            }
            val authCertficateNum = PreferenceStorage.authCertficateNum
            if (StringUtils.isNotEmptyAndNull(authCertficateNum)) {
                et_number_certificate?.setText(authCertficateNum)
            }
            val acthCertificatePic = PreferenceStorage.authCertficatePic
            if (StringUtils.isNotEmptyAndNull(acthCertificatePic)) {
                drawee_certificete?.setImageURI("file://$acthCertificatePic")
            }
            drawee_certificete?.visibility = View.VISIBLE
            iv_pic_certificete?.visibility = View.GONE
            et_real_name.isEnabled = false
            et_number_certificate.isEnabled = false
            tv_submit_audit?.isEnabled = false
            tv_submit_audit?.isClickable = false
        } else {
            drawee_certificete?.visibility = View.GONE
            iv_pic_certificete?.visibility = View.VISIBLE
            tv_state_auth?.text = state_unauth
            et_real_name.isEnabled = true
            et_number_certificate.isEnabled = true
            tv_submit_audit?.isEnabled = true
            tv_submit_audit?.isClickable = true
        }
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
        iv_pic_certificete?.setOnClickListener {
            if (isCertificateAuth) return@setOnClickListener
            if (PreventRepeatedUtils.isFastDoubleClick()) return@setOnClickListener
            //进入图片选择和拍照的界面
            val config = ISListConfig.Builder()
                .multiSelect(false)
                .rememberSelected(false)
                .needCrop(false)
                .build()

            ISListActivity.startForResult(this@RealAuthActivity, config, REQUEST_CERTIFICATE_CODE)
        }
        tv_submit_audit?.setOnClickListener {
            val realName = et_real_name?.text?.toString()?.trim()
            if (StringUtils.isEmptyOrNull(realName)) {
                ToastUtils.showSafeToast(this@RealAuthActivity, "请输入真实姓名")
                return@setOnClickListener
            }
            val certificateNumber = et_number_certificate?.text?.toString()?.trim()
            if (StringUtils.isEmptyOrNull(certificateNumber)) {
                ToastUtils.showSafeToast(this@RealAuthActivity, "请输入证件号码")
                return@setOnClickListener
            }
            if (StringUtils.isNotEmptyAndNull(certificatePath)) {
                //1 代表认证成功
                Modify.updateUserInfoAutonym(1) { isSuccess, e ->
                    if (e == null && isSuccess) {
                        PreferenceStorage.isAutonym = 1
                        PreferenceStorage.authName = realName
                        PreferenceStorage.authCertficateNum = certificateNumber
                        PreferenceStorage.authCertficatePic = certificatePath
                        GlobalObserverHelper.updateAutonymSuccess()
                        val tipDialog = TipDialog.show(
                            this@RealAuthActivity,
                            "提交审核成功,后台人员正在努力审核中...",
                            TipDialog.SHOW_TIME_SHORT,
                            TipDialog.TYPE_FINISH
                        )
                        tipDialog.setOnDismissListener(object : OnDismissListener {
                            override fun onDismiss() {
                                this@RealAuthActivity.finish()
                            }
                        })
                    } else {
                        ToastUtils.showSafeToast(this@RealAuthActivity, erroMsg)
                    }
                }
            } else {
                ToastUtils.showSafeToast(this@RealAuthActivity, "请上传标准的持证件照")
                return@setOnClickListener
            }

        }
    }

    private var certificatePath: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CERTIFICATE_CODE -> {
                    val pathList = data.getStringArrayListExtra("result")
                    if (pathList != null && pathList.size > 0) {
                        certificatePath = pathList[0]
                        if (StringUtils.isNotEmptyAndNull(certificatePath)) {
                            drawee_certificete?.visibility = View.VISIBLE
                            iv_pic_certificete?.visibility = View.GONE
                            drawee_certificete?.setImageURI("file://$certificatePath")
                        }
                    }
                }
            }
        }
    }


}