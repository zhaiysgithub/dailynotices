package com.suncity.dailynotices.islib.config

import android.os.Environment
import com.suncity.dailynotices.utils.FileUtils

import java.io.Serializable

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
class ISCameraConfig(builder: Builder) : Serializable {

    /**
     * 是否需要裁剪
     */
    var needCrop: Boolean = false

    /**
     * 拍照存储路径
     */
    var filePath: String? = null

    /**
     * 裁剪输出大小
     */
    var aspectX = 1
    var aspectY = 1
    var outputX = 500
    var outputY = 500

    init {
        this.needCrop = builder.needCrop
        this.filePath = builder.filePath
        this.aspectX = builder.aspectX
        this.aspectY = builder.aspectY
        this.outputX = builder.outputX
        this.outputY = builder.outputY
    }

    class Builder : Serializable {

        var needCrop = false
        var filePath: String? = null

        var aspectX = 1
        var aspectY = 1
        var outputX = 400
        var outputY = 400

        init {
            filePath = if (FileUtils.isSdCardAvailable)
                Environment.getExternalStorageDirectory().absolutePath + "/Camera"
            else
                Environment.getRootDirectory().absolutePath + "/Camera"
            if (filePath != null){
                FileUtils.createDir(filePath!!)
            }
        }

        fun needCrop(needCrop: Boolean): Builder {
            this.needCrop = needCrop
            return this
        }

        private fun filePath(filePath: String): Builder {
            this.filePath = filePath
            return this
        }

        fun cropSize(aspectX: Int, aspectY: Int, outputX: Int, outputY: Int): Builder {
            this.aspectX = aspectX
            this.aspectY = aspectY
            this.outputX = outputX
            this.outputY = outputY
            return this
        }

        fun build(): ISCameraConfig {
            return ISCameraConfig(this)
        }
    }
}
