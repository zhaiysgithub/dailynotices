package com.suncity.dailynotices.islib.common


import com.suncity.dailynotices.islib.bean.Folder

interface OnFolderChangeListener {

    fun onChange(position: Int, folder: Folder)
}
