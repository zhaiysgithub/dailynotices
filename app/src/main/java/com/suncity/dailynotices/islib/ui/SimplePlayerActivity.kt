package com.suncity.dailynotices.islib.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.View
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.ac_simple_player.*

/**
 * @ProjectName:    dailynotices
 * @ClassName:      SimplePlayerActivity
 * @Description:    視頻播放頁面
 */
class SimplePlayerActivity : BaseActivity() {

    private var videoPath: String = ""
    private var videoName: String = ""
    private var orientationUtils: OrientationUtils? = null

    companion object {

        private const val bundleVideoPath = "bundleVideoPath"
        private const val bundleVideoName = "bundleVideoName"

        fun start(context: Context, videoPath: String, videoName: String) {
            val intent = Intent(context, SimplePlayerActivity::class.java)
            intent.putExtra(bundleVideoPath, videoPath)
            intent.putExtra(bundleVideoName, videoName)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_simple_player
    }

    override fun initData() {
        videoPath = intent?.getStringExtra(bundleVideoPath) ?: ""
        videoName = intent?.getStringExtra(bundleVideoName) ?: ""
        Log.e("@@@", "videoPath=$videoPath,videoName=$videoName")
        if (videoPath.isEmpty()) {
            ToastUtils.showSafeToast(this@SimplePlayerActivity, Config.getString(R.string.str_not_found_video_resource))
            this@SimplePlayerActivity.finish()
            return
        }
        setVideoParams()

    }

    private fun setVideoParams() {
        videoPlayer?.setUp(videoPath, true, videoName)
        //增加title
        videoPlayer?.titleTextView?.visibility = View.VISIBLE
        //设置返回键
        videoPlayer?.backButton?.visibility = View.VISIBLE
        //设置旋转
        orientationUtils = OrientationUtils(this@SimplePlayerActivity, videoPlayer)
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer?.fullscreenButton?.setOnClickListener {
            orientationUtils?.resolveByClick()
        }
        //是否可以滑动调整
        videoPlayer?.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer?.backButton?.setOnClickListener { onBackPressed() }
        videoPlayer?.startPlayLogic()
    }


    override fun onResume() {
        super.onResume()
        videoPlayer?.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer?.onVideoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        if (orientationUtils != null) {
            orientationUtils?.releaseListener()
        }
    }


    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer?.fullscreenButton?.performClick()
            return
        }
        //释放资源
        videoPlayer?.setVideoAllCallBack(null)
        super.onBackPressed()
    }
}