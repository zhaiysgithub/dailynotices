package com.suncity.dailynotices.islib.common

import com.suncity.dailynotices.islib.bean.MimeType

/**
 * 发布的一些参数配置
 */
object PublishConstant {

    //保存选择的图片
    var imageList = arrayListOf<String>()

    const val IMAGES_AND_VIDEOS = 0
    const val IMAGES = 1
    const val VIDEOS = 2

    // 8 張圖片 1 個視頻
    const val imageMaxSize = 9
    const val videoMaxSize = 1

    const val maxSize = 9

    //已经选择视频的数量
    var hasSelVideoCount = 0

    var mimeTypes: Set<MimeType> = MimeType.ofAll()

}
