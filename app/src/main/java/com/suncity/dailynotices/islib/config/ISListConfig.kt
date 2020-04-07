package com.suncity.dailynotices.islib.config

import android.os.Environment
import com.suncity.dailynotices.utils.FileUtils

import java.io.Serializable


class ISListConfig(builder: Builder) : Serializable {

    /**
     * 是否需要裁剪
     */
    var needCrop: Boolean = false

    /**
     * 是否多选
     */
    var multiSelect = false

    /**
     * 是否记住上次的选中记录(只对多选有效)
     */
    var rememberSelected = true

    /**
     * 最多选择图片数
     */
    var maxNum = PublishDynamicConfig.imageMaxSize

    /**
     * 第一个item是否显示相机
     */
    var needCamera: Boolean = false

    var allImagesText: String? = null

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
        this.multiSelect = builder.multiSelect
        this.rememberSelected = builder.rememberSelected
        this.maxNum = builder.maxNum
        this.needCamera = builder.needCamera
        this.allImagesText = builder.allImagesText
        this.filePath = builder.filePath
        this.aspectX = builder.aspectX
        this.aspectY = builder.aspectY
        this.outputX = builder.outputX
        this.outputY = builder.outputY
    }

    class Builder : Serializable {

        var needCrop = false
        var multiSelect = true
        var rememberSelected = true
        var maxNum = PublishDynamicConfig.imageMaxSize
        var needCamera = true
        var allImagesText: String? = null
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

            allImagesText = "所有图片"

            FileUtils.createDir(filePath!!)
        }

        fun needCrop(needCrop: Boolean): Builder {
            this.needCrop = needCrop
            return this
        }

        fun multiSelect(multiSelect: Boolean): Builder {
            this.multiSelect = multiSelect
            return this
        }

        fun rememberSelected(rememberSelected: Boolean): Builder {
            this.rememberSelected = rememberSelected
            return this
        }

        fun maxNum(maxNum: Int): Builder {
            this.maxNum = maxNum
            return this
        }

        fun needCamera(needCamera: Boolean): Builder {
            this.needCamera = needCamera
            return this
        }

        fun allImagesText(allImagesText: String): Builder {
            this.allImagesText = allImagesText
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

        fun build(): ISListConfig {
            return ISListConfig(this)
        }
    }
}
