package com.suncity.dailynotices.islib.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.bean.Image
import com.suncity.dailynotices.islib.ISNav
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnItemClickListener
import com.suncity.dailynotices.islib.config.ISListConfig

/**
 * @author yuyh.
 * @date 2016/9/28.
 */
class PreviewAdapter(
    private val activity: Activity,
    private val images: List<Image>,
    private val config: ISListConfig
) : PagerAdapter() {
    private var listener: OnItemClickListener? = null

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
                if (listener != null) {
                    val ret = listener?.onCheckedClick(position, image)
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
                if (listener != null) {
                    listener?.onImageClick(position, images[position])
                }
            }
        } else {
            ivChecked.visibility = View.GONE
        }

        container.addView(
            root, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        displayImage(photoView, images[if (config.needCamera) position + 1 else position].path)

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

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
