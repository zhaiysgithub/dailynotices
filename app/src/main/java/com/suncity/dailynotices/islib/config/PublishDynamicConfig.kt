package com.suncity.dailynotices.islib.config

import com.suncity.dailynotices.islib.bean.MimeType

/**
 * @ProjectName:    dailynotices
 * @ClassName:      PublishDynamicConfig
 * @Description:    动态发布的配置
 */
class PublishDynamicConfig {

    companion object {

        const val IMAGES_AND_VIDEOS = 0
        const val IMAGES = 1
        const val VIDEOS = 2

        // 8 張圖片 1 個視頻
        const val imageMaxSize = 8
        const val videoMaxSize = 1

        var mimeTypes: Set<MimeType> = MimeType.ofAll()

    }
}