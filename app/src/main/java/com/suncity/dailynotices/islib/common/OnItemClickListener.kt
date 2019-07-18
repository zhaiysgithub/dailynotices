package com.suncity.dailynotices.islib.common


import com.suncity.dailynotices.islib.bean.Image

/**
 * @author yuyh.
 * @date 2016/8/5.
 */
interface OnItemClickListener {

    fun onCheckedClick(position: Int, image: Image): Int

    fun onImageClick(position: Int, image: Image)
}
