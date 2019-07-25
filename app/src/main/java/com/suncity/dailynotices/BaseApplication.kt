package com.suncity.dailynotices

import android.app.Application
import android.content.Context
import cn.leancloud.chatkit.LCChatKit
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMClient
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.suncity.dailynotices.ui.activity.HomeActivity
import com.suncity.dailynotices.ui.chat.CustomUserProvider
import com.suncity.dailynotices.utils.AppUtils
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.LogUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices
 * @ClassName:      BaseApplication
 * @Description:     作用描述
 * @UpdateDate:     31/5/2019
 */

class BaseApplication : Application() {

    init {
        //优先级比较低
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {

            override fun createRefreshHeader(context: Context, layout: RefreshLayout): RefreshHeader {
                //设置全局主题颜色
                val materialHeader = MaterialHeader(context)
                materialHeader.setColorSchemeColors(Config.getColor(R.color.color_ffde00), Config.getColor(R.color.color_037bff))
                return materialHeader
            }
        })

        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {

            override fun createRefreshFooter(context: Context, layout: RefreshLayout): RefreshFooter {
                //指定为经典Footer
                return ClassicsFooter(context).setDrawableSize(20f)
            }

        })
    }

    private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory().toInt()
    private val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4
    private val MAX_DISK_CACHE_SIZE = 20L * ByteConstants.MB

    override fun onCreate() {
        super.onCreate()
        Config.setApplicationContext(this)
        AppUtils.init(this)
        initAvos()
        initFrescoConfig()

    }

    private fun initFrescoConfig() {

        val pipelineConfig = ImagePipelineConfig.newBuilder(this)
            .setBitmapMemoryCacheParamsSupplier {
                MemoryCacheParams(
                    MAX_MEMORY_CACHE_SIZE,
                    Int.MAX_VALUE,
                    MAX_MEMORY_CACHE_SIZE,
                    Int.MAX_VALUE,
                    Int.MAX_VALUE)
            }
            .setMainDiskCacheConfig(
                DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(cacheDir)
                .setBaseDirectoryName("notice")
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                .build())
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this,pipelineConfig)
    }


    // 程序终止的时候执行
    override fun onTerminate() {
        LogUtils.e("BaseApplication -> onTerminate")
        super.onTerminate()
    }

    //低内存的时候执行
    override fun onLowMemory() {
        LogUtils.e("BaseApplication -> onLowMemory")
        super.onLowMemory()
    }

    //程序在内存清理的时候执行
    override fun onTrimMemory(level: Int) {
        LogUtils.e("BaseApplication -> onTrimMemory")
        super.onTrimMemory(level)
    }

    private fun initAvos() {
        val avosAppKey = AppEnviroment.getEnv()?.avosAppKey
        val avosAppId = AppEnviroment.getEnv()?.avosAppId
        LogUtils.e("initAvos -> avosAppKey=$avosAppKey,avosAppId=$avosAppId ")
        AVOSCloud.initialize(this, avosAppId, avosAppKey)
        AVOSCloud.setDebugLogEnabled(true) //打开可调式的开关
        LCChatKit.getInstance().profileProvider = CustomUserProvider.instance
        LCChatKit.getInstance().init(this, avosAppId, avosAppKey)
        AVIMClient.setAutoOpen(true)
        PushService.setDefaultPushCallback(this, HomeActivity::class.java)
        PushService.setAutoWakeUp(true)
        PushService.setDefaultChannelId(this, "default")
        AVInstallation.getCurrentInstallation().saveInBackground(object : SaveCallback() {
            override fun done(e: AVException?) {
                if (e == null) {
                    // 保存成功
                    val installationId = AVInstallation.getCurrentInstallation().installationId
                    LogUtils.e("---  $installationId")
                } else {
                    // 保存失败，输出错误信息
                    LogUtils.e("failed to save installation. = $e")
                }
            }
        })
    }
}