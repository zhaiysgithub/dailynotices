package com.suncity.dailynotices.ui.loading

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.LogUtils
import java.util.*

/**
 *
 * 正在加载loadingDialog
 * 首先得到整个View
 * 创建自定义样式的Dialog
 * 设置返回键无效
 */
@SuppressLint("InflateParams")
class LoadingDialog constructor(context: Activity, canNotFinishActivity: Boolean = true) {

    companion object {
        const val STYLE_RING = 0
        const val STYLE_LINE = 1
        const val STYLE_NO_TEXT = 2
    }

    private var mContext: Activity? = context

    private var mLoadingDialog: Dialog? = null
    private var layout: LinearLayout? = null
    private var loadingText: TextView? = null

    private var loadSuccessStr: String? = null
    private var loadFailedStr: String? = null
    private var viewList: MutableList<View> = ArrayList()

    private var interceptBack = true

    private var openSuccessAnim = true
    private var openFailedAnim = true
    private var time: Long = 1000
    private var loadStyle = STYLE_RING

    private var s: DialogStyleManager = DialogStyleManager(true, 0, Speed.SPEED_TWO, -1, -1, 1000L,
            true, "加载中...", "加载成功", "加载失败")
    private var mCircleLoadView: LoadCircleView? = null

    enum class Speed {
        SPEED_ONE,
        SPEED_TWO
    }
    init {
        val view = LayoutInflater.from(context).inflate(
                R.layout.view_loading_dialog, null)
        initView(view)
        if (canNotFinishActivity) {
            mLoadingDialog = object : Dialog(context, R.style.loading_dialog) {
                override fun onBackPressed() {
                    if (interceptBack) {
                        return
                    }
                    this@LoadingDialog.close()
                }
            }
        } else {
            mLoadingDialog = object : Dialog(context, R.style.loading_dialog) {
                override fun onBackPressed() {
                    this@LoadingDialog.close()
                    context.finish()
                }
            }
        }

        mLoadingDialog?.setCancelable(!interceptBack)
        mLoadingDialog?.setContentView(layout!!, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT))
        mLoadingDialog?.setOnDismissListener { mContext = null }
        initStyle()
    }

    private fun initView(view: View) {
        layout = view.findViewById<View>(R.id.dialog_view) as LinearLayout
        loadingText = view.findViewById<View>(R.id.tv_loadingText) as TextView
        mCircleLoadView = view.findViewById<View>(R.id.lcv_circleload) as LoadCircleView
        initData()
    }

    private fun initData() {
        viewList.clear()
        if (mCircleLoadView != null) {
            viewList.add(mCircleLoadView!!)
        }
    }

    private fun hideAll() {
        for (v in viewList) {
            if (v.visibility != View.GONE) {
                v.visibility = View.GONE
            }
        }
    }

    fun isShowing(): Boolean {
        return mLoadingDialog?.isShowing ?: false
    }

    private fun initStyle() {
        setInterceptBack(s.isInterceptBack)
        setTextSize(s.textSize.toFloat())
        setShowTime(s.showTime)
        if (!s.isOpenAnim) {
            closeFailedAnim()
            closeSuccessAnim()
        }
        setLoadingText(s.loadText)
        setSuccessText(s.successText)
        setFailedText(s.failedText)
        setLoadStyle(s.getLoadStyle())
    }

    //----------------------------------对外提供的api------------------------------//

    /**
     * 请在最后调用show，因此show返回值为void会使链式api断开
     */
    fun show() {
        hideAll()
        when (loadStyle) {
            STYLE_RING -> mCircleLoadView?.visibility = View.GONE
            STYLE_LINE -> mCircleLoadView?.visibility = View.VISIBLE
            STYLE_NO_TEXT -> {
                mCircleLoadView?.visibility = View.VISIBLE
                loadingText?.visibility = View.GONE
            }
        }
        if (mContext?.isFinishing == false){
            mLoadingDialog?.show()
        }

    }

    /**
     * 设置load的样式，目前支持转圈圈和菊花转圈圈
     * @param style
     */
    fun setLoadStyle(style: Int): LoadingDialog {
        if (style >= 3) {
            LogUtils.e("setLoadStyle -> IllegalArgumentException( Your style is wrong!)")
            this.loadStyle = STYLE_NO_TEXT
            return this
        }
        this.loadStyle = style
        return this
    }

    /**
     * 让这个dialog消失，在拦截back事件的情况下一定要调用这个方法！
     * 在调用了该方法之后如需再次使用loadingDialog，请新创建一个
     * LoadingDialog对象。
     */
    fun close() {
        viewList.clear()
        if (mContext?.isFinishing == true){
            return
        }
        val dialog = mLoadingDialog
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        mLoadingDialog = null
    }

    /**
     * 设置加载时的文字提示
     *
     * @param msg 文字
     * @return 这个对象
     */
    fun setLoadingText(msg: String?): LoadingDialog {
        if (msg != null && msg.isNotEmpty())
            loadingText?.text = msg
        return this
    }

    /**
     * 设置加载成功的文字提示
     *
     * @param msg 文字
     * @return 这个对象
     */
    fun setSuccessText(msg: String?): LoadingDialog {
        if (msg != null && msg.isNotEmpty())
            loadSuccessStr = msg
        return this
    }

    /**
     * 设置加载失败的文字提示
     *
     * @param msg 文字
     * @return 这个对象
     */
    fun setFailedText(msg: String?): LoadingDialog {
        if (msg != null && msg.isNotEmpty()) loadFailedStr = msg
        return this
    }

    /**
     * 关闭动态绘制
     */
    fun closeSuccessAnim(): LoadingDialog {
        this.openSuccessAnim = false
        return this
    }

    /**
     * 关闭动态绘制
     */
    fun closeFailedAnim(): LoadingDialog {
        this.openFailedAnim = false
        return this
    }

    /**
     * 设置是否拦截back，默认会拦截
     *
     * @param interceptBack true拦截back，false不拦截
     * @return 这个对象
     */
    fun setInterceptBack(interceptBack: Boolean): LoadingDialog {
        this.interceptBack = interceptBack
        mLoadingDialog?.setCancelable(!interceptBack)
        mLoadingDialog?.setCanceledOnTouchOutside(false)
        return this
    }

    /**
     * 设置反馈展示时间
     *
     * @param time 时间
     * @return 这个对象
     */
    fun setShowTime(time: Long): LoadingDialog {
        if (time < 0) return this
        this.time = time
        return this
    }

    /**
     * set the size of load text size
     * 设置加载字体大小
     *
     * @param size 尺寸，单位sp，来将sp转换为对应的px值
     * @return 这个对象
     */
    fun setTextSize(size: Float): LoadingDialog {
        if (size < 0) return this
        loadingText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        return this
    }


}