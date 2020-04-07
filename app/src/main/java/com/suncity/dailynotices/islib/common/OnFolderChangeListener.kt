package com.suncity.dailynotices.islib.common


import com.suncity.dailynotices.islib.bean.MediaLocalInfo

interface OnFolderChangeListener {

    fun onChange(position: Int, folder: MediaLocalInfo)
}
