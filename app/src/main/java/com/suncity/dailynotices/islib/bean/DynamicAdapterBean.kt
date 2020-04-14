package com.suncity.dailynotices.islib.bean

/**
 * @ProjectName:    dailynotices
 * @author:         ZYS
 * @ClassName:      DynamicAdapterBean
 */
data class DynamicAdapterBean(

    var path: String,
    var type: MediaType = MediaType.TYPE_IMAGE  // 1：Image,2:Video,3:Gif
)

enum class MediaType {
    TYPE_IMAGE,
    TYPE_VIDEO,
    TYPE_GIF
}