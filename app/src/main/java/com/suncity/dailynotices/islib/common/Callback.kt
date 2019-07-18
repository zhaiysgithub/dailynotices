package com.suncity.dailynotices.islib.common

import java.io.File
import java.io.Serializable

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
interface Callback : Serializable {

    fun onSingleImageSelected(path: String)

    fun onImageSelected(path: String)

    fun onImageUnselected(path: String)

    fun onCameraShot(imageFile: File)

    fun onPreviewChanged(select: Int, sum: Int, visible: Boolean)
}
