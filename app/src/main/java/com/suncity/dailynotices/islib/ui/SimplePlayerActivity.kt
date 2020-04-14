package com.suncity.dailynotices.islib.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.view.View
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.suncity.dailynotices.R
import com.suncity.dailynotices.callback.GlobalObserverHelper
import com.suncity.dailynotices.islib.bean.LocalMedia
import com.suncity.dailynotices.islib.common.PublishConstant
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.activity.PushDynamicActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.PreventRepeatedUtils
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
    private var videoTemp: LocalMedia? = null
    private var canSelVideo: Boolean = false
    private var orientationUtils: OrientationUtils? = null

    companion object {

        private const val bundleVideo = "bundleVideo"
        private const val bundleCanSelVideo = "canSelVideo"
//        private const val bundleVideoName = "bundleVideoName"

        fun start(context: Context, video: LocalMedia, canSelVideo: Boolean) {
            val intent = Intent(context, SimplePlayerActivity::class.java)
            intent.putExtra(bundleVideo, video)
            intent.putExtra(bundleCanSelVideo, canSelVideo)
            context.startActivity(intent)
        }

        fun start(context: Context, videoPath: String, canSelVideo: Boolean) {
            val localMedia = LocalMedia()
            localMedia.path = videoPath
            val intent = Intent(context, SimplePlayerActivity::class.java)
            intent.putExtra(bundleVideo, localMedia)
            intent.putExtra(bundleCanSelVideo, canSelVideo)
            context.startActivity(intent)
        }
    }

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_black)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_simple_player
    }

    override fun initData() {
        videoTemp = intent?.getSerializableExtra(bundleVideo) as? LocalMedia
        canSelVideo = intent?.getBooleanExtra(bundleCanSelVideo, false) ?: false
        videoPath = videoTemp?.path ?: ""
        videoName = videoTemp?.name ?: ""
        if (videoPath.isEmpty()) {
            ToastUtils.showSafeToast(this@SimplePlayerActivity, Config.getString(R.string.str_not_found_video_resource))
            this@SimplePlayerActivity.finish()
            return
        }
        if (canSelVideo) {
            videoPlayer_complete_layout?.visibility = View.VISIBLE
        } else {
            videoPlayer_complete_layout?.visibility = View.GONE
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
        videoPlayer?.fullscreenButton?.visibility = View.GONE
        videoPlayer?.fullscreenButton?.setOnClickListener(null)
        //是否可以滑动调整
        videoPlayer?.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer?.backButton?.setOnClickListener { onBackPressed() }
        videoPlayer?.startPlayLogic()

        videoPlayer_complete_layout?.setOnClickListener {
            if (PreventRepeatedUtils.isFastDoubleClick(R.id.videoPlayer_complete_layout)) return@setOnClickListener
            if (videoTemp == null) return@setOnClickListener
            if (!canSelVideo) {
                val formartStr =
                    String.format(Config.getString(R.string.str_video_maxnum), PublishConstant.videoMaxSize)
                ToastUtils.showSafeToast(this@SimplePlayerActivity, formartStr)
                return@setOnClickListener
            }
            videoTemp?.let {
                GlobalObserverHelper.onVideoSelected(it)
            }
            startActivity(PushDynamicActivity::class.java)
            this@SimplePlayerActivity.finish()
        }
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