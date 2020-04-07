package com.suncity.dailynotices.islib

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.islib.config.ISCameraConfig
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISCameraActivity
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.utils.LogUtils


/**
 * 总线
 */
@Suppress("DEPRECATION")
class ISNav {

    /**
     * TODO 需要调试
     */
    fun displayImage(context: Context, path: String, imageView: SimpleDraweeView) {
        imageView.setImageURI(Uri.parse("file://$path"))
    }

    fun toListActivity(source: Any, config: ISListConfig, reqCode: Int) {
        if (source is Activity) {
            ISListActivity.startForResult(source, config, reqCode)
        } else if (source is Fragment) {
            ISListActivity.startForResult(source, config, reqCode)
        }
    }

    fun toCameraActivity(source: Any, config: ISCameraConfig, reqCode: Int) {
        when (source) {
            is Activity -> ISCameraActivity.startForResult(source, config, reqCode)
            is Fragment -> ISCameraActivity.startForResult(source, config, reqCode)
        }
    }

    companion object {

        private var instance: ISNav? = null

        fun getInstance(): ISNav {
            if (instance == null) {
                synchronized(ISNav::class.java) {
                    if (instance == null) {
                        instance = ISNav()
                    }
                }
            }
            return instance!!
        }
    }

}
