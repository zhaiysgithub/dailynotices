package com.suncity.dailynotices.ui.activity

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import cn.leancloud.chatkit.LCChatKit
import com.avos.avoscloud.*
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.IEditTextChangeListener
import com.suncity.dailynotices.callback.TextWatcherHelper
import com.suncity.dailynotices.dialog.OnDismissListener
import com.suncity.dailynotices.dialog.TipDialog
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.activity.HomeActivity.Companion.POS_MINE
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.*
import kotlinx.android.synthetic.main.ac_login.*
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.suncity.dailynotices.Constants
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.lcoperation.Increase
import com.suncity.dailynotices.lcoperation.Modify


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      LoginActivity
 * @Description:    用户没有登录进入此页面执行登录的逻辑
 * 参考文档 https://leancloud.cn/docs/leanstorage-started-android.html#hash-814093878
 */
class LoginActivity : BaseActivity() {

    private val loadingText = Config.getString(R.string.str_loading)
    private val loginSuccessText = Config.getString(R.string.str_login_success)
    private val loginErrorText = Config.getString(R.string.str_login_error)
    private var avUser: AVUser? = null

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .titleBar(R.id.fl_login_del, false)
            .statusBarDarkFont(true, 0f)
            .transparentBar()
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_login
    }

    override fun initData() {
        //设置使用协议的字体大小和颜色
        val dealSize = DisplayUtils.sp2px(14f).toInt()
        val dealColor = Config.getColor(R.color.color_black)
        val aggrementStr = Config.getString(R.string.str_login_agreement_tip)
        val spannableStr = SpannableString(aggrementStr)
        spannableStr.setSpan(AbsoluteSizeSpan(dealSize), 12, aggrementStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStr.setSpan(ForegroundColorSpan(dealColor), 12, aggrementStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_login_agreement_tip?.text = spannableStr

        val etPhoneNum = findViewById<EditText>(R.id.et_phoneNumber)
        val etPwd = findViewById<EditText>(R.id.et_pwd)
        val submitText = findViewById<TextView>(R.id.tv_login)
        val textChangeListener = TextChangeListenerUtils(submitText)
        textChangeListener.addAllEditText(arrayOf(etPhoneNum, etPwd))
        textChangeListener.setChangeListener(iEditTextChangeListener)
    }

    override fun initListener() {

        tv_login_agreement_tip?.setOnClickListener {
            startActivity(UserAgreementActivity::class.java, false)

        }
        tv_forget_pwd?.setOnClickListener {
            startActivity(EditPwdActivity::class.java)
        }

        tv_login?.setOnClickListener {
            startLogin()
        }

        et_phoneNumber?.addTextChangedListener(object : TextWatcherHelper {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                if (length > 0) {
                    iv_del_phone?.visibility = View.VISIBLE
                } else {
                    iv_del_phone?.visibility = View.GONE
                }
            }
        })
        et_pwd?.addTextChangedListener(object : TextWatcherHelper {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                if (length > 0) {
                    iv_del_pwd?.visibility = View.VISIBLE
                } else {
                    iv_del_pwd?.visibility = View.GONE
                }
            }
        })

        iv_del_phone?.setOnClickListener {
            et_phoneNumber?.setText("")
        }
        iv_del_pwd?.setOnClickListener {
            et_pwd?.setText("")
        }
    }

    private fun startLogin() {
        val phoneNumber = et_phoneNumber?.text.toString().trim()
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showToast("请输入手机号码")
            return
        }
        if (!UIUtils.isPhoneNumber(this, phoneNumber)) {
            ToastUtils.showToast("手机号码有误，请重新输入...")
            return
        }
        val pwd = et_pwd.text.toString().trim()
        if (UIUtils.isPhoneNumber(this, phoneNumber)) {
            if (TextUtils.isEmpty(pwd) || pwd.length < 6) {
                ToastUtils.showToast("请重新输入密码,且密码大于等于 6 位数")
            } else {
                ProgressUtil.showProgress(this)
                requestLoginWithLeancloud(phoneNumber, pwd)
            }
        }
    }

    /**
     * 使用leanCloud登录
     * 1、验证手机号码
     * 2、有效执行登录逻辑，无效执行注册逻辑
     */
    private fun requestLoginWithLeancloud(phoneNumber: String, pwd: String) {
        //用户查询 - 验证手机号码
        val userQuery = AVQuery<AVObject>(TableConstants.TABLE_USER)
        val avQuery = userQuery.whereEqualTo(TableConstants.USER_MOBILEPHONENUMBER, phoneNumber)
        avQuery.getFirstInBackground(object : GetCallback<AVObject>() {
            override fun done(o: AVObject?, e: AVException?) {
                if (o == null) {
                    //执行注册逻辑-用户名和密码注册
                    startRegisterbyMobile(phoneNumber, pwd)
                } else {
                    //执行登录逻辑
                    loginByMobile(phoneNumber, pwd)
                }
            }

        })
    }

    /**
     * 执行注册逻辑-用户名和密码注册
     */
    private fun startRegisterbyMobile(phoneNumber: String, pwd: String) {
        val user = AVUser()// 新建 AVUser 对象实例
        user.username = StringUtils.getRandomString(6)// 设置用户名
        user.setPassword(pwd)// 设置密码
        user.mobilePhoneNumber = phoneNumber
        user.signUpInBackground(object : SignUpCallback() {
            override fun done(e: AVException?) {
                if (e == null) {
                    // 注册成功 - 执行登录逻辑
                    loginByMobile(phoneNumber, pwd)
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    val errorMsg: String?
                    val code = e.code
                    errorMsg = when (code) {
                        127 -> {
                            "手机号码无效"
                        }
                        else -> {
                            "抱歉，服务器开小差了"
                        }
                    }
                    TipDialog.show(
                        this@LoginActivity, errorMsg
                        , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                    )
                }
            }
        })
    }

    /**
     * 通过objectId 查询是否自动创建了userInfo的关联
     */
    private fun queryUserInfo(objectId: String) {
        val query = AVQuery<AVObject>(TableConstants.TABLE_USERINFO)
        query.whereEqualTo(TableConstants.USER, objectId).getFirstInBackground(object : GetCallback<AVObject>() {
            override fun done(userInfo: AVObject?, e: AVException?) {
                if (userInfo == null) {
                    Increase.createUserInfoToBack(objectId) { userinfoId, exception ->
                        ProgressUtil.hideProgress()
                        if (userinfoId == null) {
                            TipDialog.show(
                                this@LoginActivity, exception?.message ?: loginErrorText
                                , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                            )
                        } else {
                            Modify.updateUserToBack(objectId, userinfoId) { updateUserException ->
                                if (updateUserException == null) { // 保存成功
                                    saveUser()
                                } else {
                                    TipDialog.show(
                                        this@LoginActivity, exception?.message ?: loginErrorText
                                        , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                                    )
                                }
                            }
                        }

                    }
                } else {
                    ProgressUtil.hideProgress()
                    PreferenceStorage.isAutonym = userInfo.getInt("autonym")
                    saveUser()
                }
            }

        })
    }

    /**
     * 执行登录逻辑
     */
    private fun loginByMobile(phoneNumber: String, pwd: String) {
        AVUser.loginByMobilePhoneNumberInBackground(phoneNumber, pwd, object : LogInCallback<AVUser>() {
            override fun done(user: AVUser?, e: AVException?) {
                if (user != null && e == null) {
                    avUser = user
                    val objectId = user.objectId
                    PreferenceStorage.userObjectId = objectId
                    queryUserInfo(objectId)
                } else {
                    ProgressUtil.hideProgress()
                    if (e != null) {
                        val errorMsg: String?
                        val errorCode = e.code
                        errorMsg = when (errorCode) {
                            127 -> {
                                "手机号码无效"
                            }
                            210 -> {
                                "登录密码错误，请重试"
                            }
                            214 -> {
                                "手机号码已经被注册"
                            }
                            219 -> {
                                "登录失败次数超过显示,请15分钟后尝试,\n 或者通过忘记密码重设密码"
                            }
                            else -> {
                                Config.getString(R.string.str_error_server)
                            }
                        }
                        TipDialog.show(
                            this@LoginActivity, errorMsg
                            , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                        )
                    } else {
                        TipDialog.show(
                            this@LoginActivity, loginErrorText
                            , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                        )
                    }
                }
            }

        })
    }


    private fun saveUser() {
        if (avUser == null) return
        PreferenceStorage.isLogin = true
        PreferenceStorage.userName = avUser?.username ?: ""
        PreferenceStorage.userPhoneNum = avUser?.getString("mobilePhoneNumber") ?: ""
        PreferenceStorage.userAvatar = avUser?.getAVFile<AVFile>("avatar")?.url ?: ""
        //开启聊天功能
        val client = AVIMClient.getInstance(avUser)
        client.open(object : AVIMClientCallback() {

            override fun done(client: AVIMClient?, e: AVIMException?) {
                LogUtils.e("开启聊天功能 -> $e")
                if (e == null) {
                    GlobalObserverHelper.loginSuccess()

                    startLoginChatKit(avUser!!.objectId)
                } else {
                    TipDialog.show(
                        this@LoginActivity, e.message ?: loginErrorText
                        , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR
                    )
                }
            }

        })


    }

    private fun startLoginChatKit(objectId: String?) {
        LCChatKit.getInstance().open(objectId, object : AVIMClientCallback() {

            override fun done(client: AVIMClient?, e: AVIMException?) {
                LogUtils.e("LCChatKit.getInstance().open -> $e")
                val tipDialog = TipDialog.show(
                    this@LoginActivity, loginSuccessText
                    , TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH
                )
                tipDialog.setOnDismissListener(object : OnDismissListener {
                    override fun onDismiss() {
                        HomeActivity.start(this@LoginActivity, POS_MINE)
                        this@LoginActivity.finish()
                    }
                })
            }

        })
    }


    private val iEditTextChangeListener = object : IEditTextChangeListener {
        override fun onTextChange(hasContent: Boolean) {
            if (hasContent) {
                tv_login?.setTextColor(Config.getColor(R.color.color_222))
                tv_login?.background = Config.getDrawable(R.drawable.shape_submit_checked_bg)
            } else {
                tv_login?.setTextColor(Config.getColor(R.color.color_999))
                tv_login?.background = Config.getDrawable(R.drawable.shape_submit_unchecked_bg)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}