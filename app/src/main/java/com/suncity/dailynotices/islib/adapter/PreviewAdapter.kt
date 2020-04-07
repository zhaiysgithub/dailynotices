package com.suncity.dailynotices.islib.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.bean.LocalMedia
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnImgItemClickListener
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.utils.LogUtils

class PreviewAdapter(
    private val activity: Activity,
    private val images: List<LocalMedia>,
    private val config: ISListConfig
) : PagerAdapter() {
    private var listenerImg: OnImgItemClickListener? = null

    override fun getCount(): Int {
        return if (config.needCamera)
            images.size - 1
        else
            images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val root = View.inflate(activity, R.layout.is_item_pager_img_sel, null)
        val photoView = root.findViewById<SimpleDraweeView>(R.id.ivImage)
        val ivChecked = root.findViewById<ImageView>(R.id.ivPhotoCheaked)

        if (config.multiSelect) {

            ivChecked.visibility = View.VISIBLE
            val image = images[if (config.needCamera) position + 1 else position]
            if (Constant.imageList.contains(image.path)) {
                ivChecked.setImageResource(R.drawable.ic_checked)
            } else {
                ivChecked.setImageResource(R.drawable.ic_uncheck)
            }

            ivChecked.setOnClickListener {
                if (listenerImg != null) {
                    val ret = listenerImg?.onCheckedClick(position, image)
                    if (ret == 1) { // 局部刷新
                        if (Constant.imageList.contains(image.path)) {
                            ivChecked.setImageResource(R.drawable.ic_checked)
                        } else {
                            ivChecked.setImageResource(R.drawable.ic_uncheck)
                        }
                    }
                }
            }

            photoView.setOnClickListener {
                if (listenerImg != null) {
                    listenerImg?.onImageClick(position, images[position])
                }
            }
        } else {
            ivChecked.visibility = View.GONE
        }

        container.addView(
            root, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val path = images[if (config.needCamera) position + 1 else position].path
        LogUtils.e("@@@path =$path")
        displayImage(photoView, path)
        return root
    }

    private fun displayImage(photoView: SimpleDraweeView, path: String) {
        ISNav.getInstance().displayImage(activity, path, photoView)
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setListener(listenerImg: OnImgItemClickListener) {
        this.listenerImg = listenerImg
    }
}
