package com.suncity.dailynotices.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.StringUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.ac_web.*
import kotlinx.android.synthetic.main.view_title.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      WebActivity
 * @Description:    加载网页的activity
 * @UpdateDate:     16/7/2019
 */
class WebActivity : BaseActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var htmlStr: String? = null
    private var htmlUrl: String? = null
    private var htmlTitle: String? = null

    companion object {
        private const val HTMLSTR = "htmlstr"
        private const val HTMLURL = "htmlurl"
        private const val HTMLTITLE = "htmltitle"

        fun start(context: Context, htmlStr: String?, url: String?,title:String?) {
            val intent = Intent()
            intent.setClass(context, WebActivity::class.java)
            if (StringUtils.isNotEmptyAndNull(htmlStr)) {
                intent.putExtra(HTMLSTR, htmlStr)
            }
            if (StringUtils.isNotEmptyAndNull(url)) {
                intent.putExtra(HTMLURL, url)
            }
            if (StringUtils.isNotEmptyAndNull(title)) {
                intent.putExtra(HTMLTITLE, title)
            }
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
        return R.layout.ac_web
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {

        htmlStr = intent?.getStringExtra(HTMLSTR)
        htmlUrl = intent?.getStringExtra(HTMLURL)
        htmlTitle = intent?.getStringExtra(HTMLTITLE)

        tv_title_center.text = htmlTitle

        webview?.overScrollMode = View.OVER_SCROLL_NEVER
        webview?.isVerticalScrollBarEnabled = false //垂直不显示

        val settings = webview?.settings
        settings?.javaScriptEnabled = true   // 启用支持javascript
        settings?.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings?.domStorageEnabled = true
        settings?.builtInZoomControls = false
        settings?.setSupportZoom(true)//设定支持缩放
        settings?.allowFileAccess = true   //设置可以访问文件
        settings?.displayZoomControls = false
        settings?.setAppCacheEnabled(true)// 是否使用缓存
        settings?.javaScriptCanOpenWindowsAutomatically = true
        settings?.useWideViewPort = true//关键点
        settings?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        settings?.loadWithOverviewMode = true
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        if(StringUtils.isNotEmptyAndNull(htmlStr)){
            webview?.loadDataWithBaseURL(null,htmlStr,"text/html", "utf-8", null)
        }
    }

    @Suppress("OverridingDeprecatedMember")
    override fun initListener() {

        fl_title_back?.setOnClickListener {
            if (webview?.canGoBack() == true) {
                webview?.goBack()
            } else {
                onBackPressed()
            }
        }

        webview?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    webview_rogressBar?.visibility = View.GONE
                } else {
                    if (View.VISIBLE != webview_rogressBar.visibility) {
                        webview_rogressBar.visibility = View.VISIBLE
                    }
                }
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)
                if(StringUtils.isEmptyOrNull(htmlTitle)){
                    tv_title_center.text = s
                }
            }
        }

        webview?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                webview_rogressBar?.visibility = View.GONE

            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                webview_rogressBar?.visibility = View.GONE
                ToastUtils.showSafeToast(this@WebActivity, "加载失败")
            }
        }
        if (StringUtils.isNotEmptyAndNull(htmlUrl)){
            webview?.loadUrl(htmlUrl)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview?.canGoBack() == true) {
                webview?.goBack()
            } else {
                this@WebActivity.finish()
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun onFinish() {
        handler.post(run)
    }

    private val run = Runnable {
        webview?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webview?.clearHistory()

        (webview?.parent as ViewGroup).removeView(webview)
        webview?.destroy()
        this@WebActivity.finish()
    }

    override fun onDestroy() {
        onFinish()
        super.onDestroy()
    }
}