package com.suncity.dailynotices.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.IEditTextChangeListener
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.TextChangeListenerUtils
import kotlinx.android.synthetic.main.ac_contact_service.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      ContactServiceActivity
 * @Description:    联系客服
 * @UpdateDate:     5/7/2019
 */
class ContactServiceActivity : BaseActivity() {

    companion object {
        const val TYPE_CONTACTSERVICE = 0
        const val TYPE_COMPLAINT = 1
        private const val TYPE_NAME = "type"
        private val STR_CONTACTSERVICE = Config.getString(R.string.str_service)
        private val STR_COMPLAINT = Config.getString(R.string.str_complain)

        fun start(context: Context, type: Int) {

            val intent = Intent()
            intent.setClass(context, ContactServiceActivity::class.java)
            intent.putExtra(TYPE_NAME, type)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_contact_service
    }

    override fun initData() {
        val type = intent.getIntExtra(TYPE_NAME, TYPE_CONTACTSERVICE)
        tv_title_center?.text = if (type == TYPE_CONTACTSERVICE) {
            STR_CONTACTSERVICE
        } else {
            STR_COMPLAINT
        }
        val submitText = findViewById<TextView>(R.id.tv_submit)
        val editTextSuggestion = findViewById<EditText>(R.id.et_suggestion)
        val textChangeListener = TextChangeListenerUtils(submitText)
        textChangeListener.addAllEditText(arrayOf(editTextSuggestion))
        textChangeListener.setChangeListener(editTextChangeListener)
    }

    override fun initListener() {
        layout_title?.setOnClickListener {
            finish()
        }
        tv_submit?.setOnClickListener {

        }
    }

    private val editTextChangeListener = object : IEditTextChangeListener {

        override fun onTextChange(hasContent: Boolean) {
            if (hasContent) {
                tv_submit?.setTextColor(Config.getColor(R.color.color_222))
                tv_submit?.background = Config.getDrawable(R.drawable.shape_submit_checked_bg)
            } else {
                tv_submit?.setTextColor(Config.getColor(R.color.color_999))
                tv_submit?.background = Config.getDrawable(R.drawable.shape_submit_unchecked_bg)
            }
        }

    }
}