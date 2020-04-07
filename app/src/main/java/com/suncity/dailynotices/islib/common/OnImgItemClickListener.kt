package com.suncity.dailynotices.islib.common


import com.suncity.dailynotices.islib.bean.LocalMedia

interface OnImgItemClickListener {

    fun onCheckedClick(position: Int, image: LocalMedia): Int

    fun onImageClick(position: Int, image: LocalMedia)

    fun onVideoClick(position: Int, video: LocalMedia)
}
