package com.suncity.dailynotices.islib.ui.fragment


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.FileProvider
import android.support.v4.content.Loader
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.adapter.FolderListAdapter
import com.suncity.dailynotices.islib.adapter.ImageListAdapter
import com.suncity.dailynotices.islib.adapter.PreviewAdapter
import com.suncity.dailynotices.islib.bean.Image
import com.suncity.dailynotices.islib.common.Callback
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnFolderChangeListener
import com.suncity.dailynotices.islib.common.OnImgItemClickListener
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.ui.BaseFragment
import kotlinx.android.synthetic.main.is_fragment_img_sel.*
import java.io.File
import java.lang.Exception
import com.suncity.dailynotices.islib.MediaLoaderInterface
import com.suncity.dailynotices.islib.MediaLoaderTask
import com.suncity.dailynotices.islib.bean.LocalMedia
import com.suncity.dailynotices.islib.bean.MediaLocalInfo
import com.suncity.dailynotices.islib.config.PublishDynamicConfig
import com.suncity.dailynotices.islib.ui.SimplePlayerActivity
import com.suncity.dailynotices.utils.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class ImgSelFragment : BaseFragment(), ViewPager.OnPageChangeListener {

    private var config: ISListConfig? = null
    private var callback: Callback? = null
    //包含搜索出媒体的文件集合
    private val folderList = ArrayList<MediaLocalInfo>()
    //所有媒体的数据集合
    private val mediaList = ArrayList<LocalMedia>()

    private var folderPopupWindow: ListPopupWindow? = null
    private var imageListAdapter: ImageListAdapter? = null
    private var folderListAdapter: FolderListAdapter? = null
    private var previewAdapter: PreviewAdapter? = null

    private var tempFile: File? = null

    private var mediaType = PublishDynamicConfig.IMAGES_AND_VIDEOS
    private var mediaLoaderTask: MediaLoaderTask? = null

    override fun setContentView(): Int {
        return R.layout.is_fragment_img_sel
    }

    override fun initData() {
        viewPager?.offscreenPageLimit = 1
        viewPager?.addOnPageChangeListener(this)
        if (activity is ISListActivity) {
            config = (activity as ISListActivity).config
            callback = activity as ISListActivity
        } else {
            activity?.finish()
        }
        if (config == null) {
            LogUtils.e("config 参数不能为空")
            activity?.finish()
            return
        }
        btnAlbumSelected?.text = config?.allImagesText

        rvImageList?.layoutManager = GridLayoutManager(requireContext(), 3)

        rvImageList?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            var spacing = DisplayUtils.dip2px(6f)
            var halfSpacing = spacing shr 1
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = halfSpacing
                outRect.right = halfSpacing
                outRect.top = halfSpacing
                outRect.bottom = halfSpacing
            }
        })

        if (config?.needCamera == true) {
            mediaList.add(LocalMedia())
        }

        imageListAdapter = ImageListAdapter(requireContext())
        imageListAdapter?.setShowCamera((config?.needCamera ?: false))
        imageListAdapter?.setMutiSelect((config?.multiSelect ?: false))
        rvImageList?.setHasFixedSize(true)
        rvImageList?.itemAnimator?.changeDuration = 0
        rvImageList?.adapter = imageListAdapter
        imageListAdapter?.addAll(mediaList)
        folderListAdapter = FolderListAdapter(requireContext(), folderList)
        loadContentResource()
    }

    /**
     * 加载所需要的资源
     */
    private fun loadContentResource(folder: MediaLocalInfo? = null) {
        if (folder != null) {
            val filterMedias = folderList.find { it.parentPath == folder.parentPath }?.localMedias
            if (filterMedias != null && filterMedias.size > 0) {
                mediaList.clear()
                if (config?.needCamera == true) {
                    mediaList.add(LocalMedia())
                }
                mediaList.addAll(filterMedias)
                imageListAdapter?.clear()
                imageListAdapter?.addAll(mediaList)
            }
            return
        }
        mediaLoaderTask = MediaLoaderTask(true, mediaType, object : MediaLoaderInterface {
            override fun onSuccess(localMedias: ArrayList<MediaLocalInfo>) {
                if (localMedias.size == 0) {
                    return
                }
                folderList.clear()
                folderList.addAll(localMedias)
                localMedias.forEach {
                    val medias = it.localMedias
                    if (medias.size > 0) {
                        mediaList.addAll(medias)
                    }
                }
                imageListAdapter?.clear()
                imageListAdapter?.addAll(mediaList)
                folderListAdapter?.notifyDataSetChanged()

            }

            override fun onFailure() {
                ToastUtils.showSafeToast(requireActivity(), Config.getString(R.string.str_album_failed_to_read))
            }

        })

        mediaLoaderTask?.execute(requireActivity())

        //方案二
//        loadPhotos(isRestart)
//        loadVideos(isRestart)
    }

    /**
     * 异步加载图片
     */
    /*private fun loadVideos(isRestart: Boolean) {
        if (isRestart) {
            activity?.supportLoaderManager?.restartLoader(LOADER_ALL, null, mVideoLoaderCallback)
        } else {
            activity?.supportLoaderManager?.initLoader(LOADER_ALL, null, mVideoLoaderCallback)
        }
    }*/

    /**
     * 异步加载图片
     */
    /*private fun loadPhotos(isRestart: Boolean) {
        if (isRestart) {
            activity?.supportLoaderManager?.restartLoader(LOADER_ALL, null, mImgLoaderCallback)
        } else {
            activity?.supportLoaderManager?.initLoader(LOADER_ALL, null, mImgLoaderCallback)
        }
    }*/

    @Suppress("DEPRECATION")
    override fun initListener() {
        btnAlbumSelected?.setOnClickListener {
            val widthSize = DisplayUtils.getScreenWidth(requireContext())
            val size = widthSize / 3 * 2
            if (folderPopupWindow == null) {
                createPopupFolderList(widthSize)
            }
            if (folderPopupWindow?.isShowing == true) {
                folderPopupWindow?.dismiss()
            } else {
                folderPopupWindow?.show()
                val folerListView = folderPopupWindow?.listView
                if (folerListView != null) {
                    folerListView.divider = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.color_a000))
                }
                var index = folderListAdapter?.selectIndex ?: 0
                index = if (index == 0) index else (index - 1)
                folerListView?.setSelection(index)

                folerListView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    @SuppressLint("ObsoleteSdkInt")
                    override fun onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            folerListView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                        } else {
                            folerListView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                        val h = folerListView.measuredHeight
                        if (h > size) {
                            folderPopupWindow?.height = size
                            folderPopupWindow?.show()
                        }
                    }
                })
                setBackgroundAlpha(0.6f)
            }
        }
        imageListAdapter?.setOnImgItemClickListener(object : OnImgItemClickListener {
            override fun onCheckedClick(position: Int, image: LocalMedia): Int {
                return checkedImage(position, image)
            }

            override fun onImageClick(position: Int, image: LocalMedia) {
                val configTemp = config ?: return
                val needCamera = configTemp.needCamera
                if (needCamera && position == 0) {
                    showCameraAction()
                } else {
                    val needMultiSelect = configTemp.multiSelect
                    if (needMultiSelect) {

                        previewAdapter = PreviewAdapter(requireActivity(), mediaList, configTemp)

                        viewPager?.adapter = previewAdapter

                        previewAdapter?.setListener(object : OnImgItemClickListener {
                            override fun onCheckedClick(position: Int, image: LocalMedia): Int {
                                return checkedImage(position, image)
                            }

                            override fun onImageClick(position: Int, image: LocalMedia) {
                                hidePreview()
                            }

                            override fun onVideoClick(position: Int, video: LocalMedia) {
                                // 图片有预览，视频没有预览
                            }
                        })
                        if (needCamera) {
                            callback?.onPreviewChanged(position, mediaList.size - 1, true)
                        } else {
                            callback?.onPreviewChanged(position + 1, mediaList.size, true)
                        }
                        viewPager?.currentItem = if (needCamera) (position - 1) else position
                        viewPager?.visibility = View.VISIBLE
                    } else {
                        if (callback != null) {
                            callback?.onSingleImageSelected(image.path)
                        }
                    }
                }
            }

            override fun onVideoClick(position: Int, video: LocalMedia) {
                // 視頻播放
                val videoPath = video.path
                val videoName = video.name
                if (videoPath.isEmpty()) {
                    ToastUtils.showSafeToast(requireActivity(), Config.getString(R.string.str_not_found_video_resource))
                    return
                }
                SimplePlayerActivity.start(requireContext(), videoPath, videoName)
            }

        })
    }

    /* private val mVideoLoaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {

         private val MEDIA_PROJECT = arrayOf(
             MediaStore.Video.Media._ID
             , MediaStore.Video.Media.DATA
             , MediaStore.Video.Media.DISPLAY_NAME
             , MediaStore.Video.Media.DURATION
             , MediaStore.MediaColumns.SIZE
             , MediaStore.Video.Media.DATE_MODIFIED
         )

         private val MEDIA_THUMB_PROJECT = arrayOf(
             MediaStore.Video.Thumbnails._ID
             , MediaStore.Video.Thumbnails.DATA
         )

         override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
             return when (id) {
                 LOADER_ALL -> {
                     CursorLoader(
                         requireContext(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MEDIA_PROJECT,
                         null, arrayOf("video/mp4"), MediaStore.Video.Media.DATE_MODIFIED + " DESC"
                     )
                 }
                 LOADER_CATEGORY -> {
                     CursorLoader(
                         requireContext(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MEDIA_PROJECT
                         , MEDIA_PROJECT[0], arrayOf("video/mp4"), MediaStore.Video.Media.DATE_MODIFIED + " DESC"
                     )

                 }
                 else -> {
                     CursorLoader(
                         requireContext(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MEDIA_PROJECT,
                         null, arrayOf("video/mp4"), MediaStore.Video.Media.DATE_MODIFIED + " DESC"
                     )

                 }
             }
         }

         override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
             try {
                 data?.let {
                     val count = it.count
                     if (count > 0) {
                         val tempImageList = ArrayList<Image>()
                         data.moveToFirst()
                         do {
                             val videoId = data.getLong(data.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                             val path = data.getString(data.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                             val displayName =
                                 data.getString(data.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                             val duration = data.getLong(data.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                             var size =
                                 data.getLong(data.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)) / 1024  //单位kb
                             if (size < 0) {
                                 size = File(path).length() / 1024
                             }
                             val modifyTime =
                                 data.getLong(data.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED))

                             //缩略图
                             MediaStore.Video.Thumbnails.getThumbnail(
                                 requireActivity().contentResolver,
                                 videoId,
                                 MediaStore.Video.Thumbnails.MICRO_KIND,
                                 null
                             )

                             val thumbCursor = requireActivity().contentResolver.query(
                                 MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
                                 , MEDIA_THUMB_PROJECT
                                 , MediaStore.Video.Thumbnails.VIDEO_ID + "=?"
                                 , arrayOf("$videoId"), null
                             )
                             var thumbPath = ""
                             thumbCursor?.let { cursor ->
                                 while (cursor.moveToNext()) {
                                     thumbPath =
                                         cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA))
                                 }
                             }
                             thumbCursor?.close()

                         } while (data.moveToNext())
                     }
                 }
             } catch (e: Exception) {
                 LogUtils.e("LoaderManager.LoaderVideoCallbacks：onLoadFinishedError:$e")
             }

         }

         override fun onLoaderReset(loader: Loader<Cursor>) {

         }

     }

     private val mImgLoaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {

         //本地图片查询条件
         private val IMAGE_PROJECTION =
             arrayOf(
                 MediaStore.Images.Media._ID
                 , MediaStore.Images.Media.DATA
                 , MediaStore.Images.Media.DISPLAY_NAME
                 , MediaStore.Images.Media.DISPLAY_NAME
                 , MediaStore.Images.Media.SIZE
                 , MediaStore.Images.Media.DATE_MODIFIED
             )


         override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
             return when (id) {
                 LOADER_ALL -> {
                     CursorLoader(
                         requireContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                         null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC"
                     )
                 }
                 LOADER_CATEGORY -> {
                     CursorLoader(
                         requireContext(),
                         MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                         IMAGE_PROJECTION
                         ,
                         IMAGE_PROJECTION[1] + " not like '%.gif%'",
                         null,
                         MediaStore.Images.Media.DATE_MODIFIED + " DESC"
                     )

                 }
                 else -> {
                     CursorLoader(
                         requireContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                         null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC"
                     )

                 }
             }
         }

         override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
             *//*try {
                if (data != null) {
                    val count = data.count
                    if (count > 0) {
                        val tempImageList = ArrayList<Image>()
                        data.moveToFirst()
                        do {
                            val imageId = data.getInt(data.getColumnIndexOrThrow(_ID))
                            val size = data.getLong(data.getColumnIndexOrThrow(SIZE))
                            val imagePath = data.getString(data.getColumnIndexOrThrow(DATA))
                            val imageName = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME))
                            val modify = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED))

                            val image = Image(imagePath, imageName)
                            tempImageList.add(image)
                            if (!hasFolderGened) {
                                val imageFile = File(imagePath)
                                val folderFile = imageFile.parentFile
                                if (folderFile == null || !imageFile.exists() || imageFile.length() < 10) {
                                    continue
                                }
                                var parent: MediaLocalInfo? = null
                                for (folder in folderList) {
                                    if (TextUtils.equals(folder.parentPath, folderFile.absolutePath)) {
                                        parent = folder
                                    }
                                }
                                if (parent != null) {
                                    parent.localMedias.add(image)
                                } else {
                                    parent = Folder()
                                    parent.name = folderFile.name
                                    parent.path = folderFile.absolutePath
                                    parent.cover = image

                                    val imageList = ArrayList<Image>()
                                    imageList.add(image)
                                    parent.images = imageList
                                    folderList.add(parent)
                                }
                            }
                        } while (data.moveToNext())

                        mediaList.clear()
                        if (config?.needCamera == true) {
                            mediaList.add(LocalMedia())
                        }
                        mediaList.addAll(tempImageList)
                        imageListAdapter?.clear()
                        imageListAdapter?.addAll(mediaList)
                        folderListAdapter?.notifyDataSetChanged()
                        hasFolderGened = true
                    }
                }
            } catch (e: Exception) {
                LogUtils.e("LoaderManager.LoaderCallbacks：onLoadFinishedError:$e")
            }*//*

        }

        override fun onLoaderReset(loader: Loader<Cursor>) {

        }
    }
*/

    private fun checkedImage(position: Int, image: LocalMedia?): Int {
        if (image != null) {
            if (Constant.imageList.contains(image.path)) {
                Constant.imageList.remove(image.path)
                if (callback != null) {
                    callback?.onImageUnselected(image.path)
                }
            } else {
                if ((config?.maxNum ?: 0) <= Constant.imageList.size) {
                    ToastUtils.showSafeToast(
                        requireActivity(),
                        String.format(getString(R.string.str_maxnum), (config?.maxNum ?: 0))
                    )
                    return 0
                }
                Constant.imageList.add(image.path)
                if (callback != null) {
                    callback?.onImageSelected(image.path)
                }
            }
            return 1
        }
        return 0
    }

    private fun createPopupFolderList(width: Int) {
        folderPopupWindow = ListPopupWindow(requireContext())
        folderPopupWindow?.animationStyle = R.style.IS_PopupAnimBottom
        folderPopupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        folderPopupWindow?.setAdapter(folderListAdapter)
        folderPopupWindow?.setContentWidth(width)
        folderPopupWindow?.width = width
        folderPopupWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        folderPopupWindow?.anchorView = rlBottom
        folderPopupWindow?.isModal = true
        folderListAdapter?.setOnFloderChangeListener(object : OnFolderChangeListener {
            override fun onChange(position: Int, folder: MediaLocalInfo) {
                folderPopupWindow?.dismiss()
                if (position == 0) {
                    //TODO 更改数据
                    loadContentResource(folder)
                    btnAlbumSelected?.text = config?.allImagesText
                } else {
                    mediaList.clear()
                    if (config?.needCamera == true) {
                        mediaList.add(LocalMedia())
                    }
                    mediaList.addAll(folder.localMedias)
                    imageListAdapter?.clear()
                    imageListAdapter?.addAll(mediaList)
                    btnAlbumSelected?.text = folder.parentPath
                }
            }
        })
        folderPopupWindow?.setOnDismissListener { setBackgroundAlpha(1.0f) }
    }

    private fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = activity?.window?.attributes
        lp?.alpha = bgAlpha
        activity?.window?.attributes = lp
    }


    private fun showCameraAction() {
        val maxNum = config?.maxNum ?: 0
        if (maxNum <= Constant.imageList.size) {
            ToastUtils.showSafeToast(requireActivity(), String.format(getString(R.string.str_maxnum), maxNum))
            return
        }

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            return
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            tempFile = File(FileUtils.createRootPath(requireContext()) + "/" + System.currentTimeMillis() + ".jpg")
            LogUtils.e(tempFile?.absolutePath ?: "")
            if (tempFile != null) {
                FileUtils.createFile(tempFile!!)
            }

            val uri = FileProvider.getUriForFile(
                requireContext(),
                FileUtils.getApplicationId(requireContext()) + ".image_provider", tempFile!!
            )

            val resInfoList = requireActivity().packageManager
                .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireActivity().grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) //Uri.fromFile(tempFile)
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        } else {
            ToastUtils.showSafeToast(requireContext(), getString(R.string.str_open_camera_failure))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                tempFile?.let {
                    callback?.onCameraShot(it)
                }
            } else {
                if (tempFile != null && (tempFile?.exists() == true)) {
                    tempFile?.delete()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction()
            } else {
                ToastUtils.showSafeToast(requireContext(), getString(R.string.str_permission_camera_denied))
            }
            else -> {
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (config?.needCamera == true) {
            callback?.onPreviewChanged(position + 1, mediaList.size - 1, true)
        } else {
            callback?.onPreviewChanged(position + 1, mediaList.size, true)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun hidePreview(): Boolean {
        val isVisiable = viewPager?.visibility == View.VISIBLE
        return if (isVisiable) {
            viewPager?.visibility = View.GONE
            callback?.onPreviewChanged(0, 0, false)
            imageListAdapter?.notifyDataSetChanged()
            true
        } else {
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.supportLoaderManager?.destroyLoader(LOADER_ALL)
    }

    companion object {

        private const val LOADER_ALL = 0
        private const val LOADER_CATEGORY = 1
        private const val REQUEST_CAMERA = 5

        private const val CAMERA_REQUEST_CODE = 1

        fun instance(): ImgSelFragment {
            val fragment = ImgSelFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}
