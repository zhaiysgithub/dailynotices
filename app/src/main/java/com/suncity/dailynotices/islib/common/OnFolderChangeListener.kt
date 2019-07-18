package com.suncity.dailynotices.islib.common


import com.suncity.dailynotices.islib.bean.Folder

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
interface OnFolderChangeListener {

    fun onChange(position: Int, folder: Folder)
}
