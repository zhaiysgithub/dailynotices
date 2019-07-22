package com.suncity.dailynotices.islib.common


import com.suncity.dailynotices.islib.bean.Image

interface OnImgItemClickListener {

    fun onCheckedClick(position: Int, image: Image): Int

    fun onImageClick(position: Int, image: Image)
}
