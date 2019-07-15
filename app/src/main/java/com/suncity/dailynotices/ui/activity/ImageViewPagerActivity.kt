package com.suncity.dailynotices.ui.activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.bar.ImmersionBar
import kotlinx.android.synthetic.main.ac_image_viewpager.*
import me.relex.photodraweeview.PhotoDraweeView

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      ImageViewPagerActivity
 * @Description:    大图预览的界面
 * @UpdateDate:     14/7/2019
 */
class ImageViewPagerActivity : BaseActivity() {

    companion object {
        var currentPos = 0
        var urls = mutableListOf<String>()
    }

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .fullScreen(true)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_image_viewpager
    }

    override fun initData() {
        view_pager?.adapter = DraweePagerAdapter()
        indicator?.setViewPager(view_pager)
        view_pager?.currentItem = currentPos
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
        fl_share?.setOnClickListener {
            val currentItem = view_pager?.currentItem ?: 0
            if(currentItem in 0 until urls.size){
                val url = urls[currentItem]
            }
        }
    }



    inner class DraweePagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return urls.size
        }

        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view == o
        }

        override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
            if (o is View) {
                container.removeView(o)
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val isAvailable = (position in 0 until urls.size)
            val uri = Uri.parse(urls[position])
            val photoDraweeView = PhotoDraweeView(container.context)
            val controller = Fresco.newDraweeControllerBuilder()
            controller.oldController = photoDraweeView.controller
            controller.controllerListener = object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    if (imageInfo == null) {
                        return
                    }
                    photoDraweeView.update(imageInfo.width, imageInfo.height)
                }
            }
            photoDraweeView.controller = controller.build()
            try {
                if (isAvailable) {
                    photoDraweeView.setPhotoUri(uri)
                }
                container.addView(
                    photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }


            return photoDraweeView
        }


    }
}