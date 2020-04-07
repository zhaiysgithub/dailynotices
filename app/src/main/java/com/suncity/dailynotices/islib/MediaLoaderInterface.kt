package com.suncity.dailynotices.islib

import com.suncity.dailynotices.islib.bean.MediaLocalInfo

/**
 * @ProjectName:    dailynotices
 * @ClassName:      MediaLoaderInterface
 */

interface MediaLoaderInterface {

    fun onSuccess(localMedias: ArrayList<MediaLocalInfo>)

    fun onFailure()
}