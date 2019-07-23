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
import com.suncity.dailynotices.islib.bean.Folder
import com.suncity.dailynotices.islib.bean.Image
import com.suncity.dailynotices.islib.common.Callback
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.common.OnFolderChangeListener
import com.suncity.dailynotices.islib.common.OnImgItemClickListener
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.ISListActivity
import com.suncity.dailynotices.ui.BaseFragment
import com.suncity.dailynotices.utils.FileUtils
import com.suncity.dailynotices.utils.DisplayUtils
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.ToastUtils
import kotlinx.android.synthetic.main.is_fragment_img_sel.*

import java.io.File
import java.util.ArrayList


@Suppress("DEPRECATION")
class ImgSelFragment : BaseFragment(), ViewPager.OnPageChangeListener {

    private var config: ISListConfig? = null
    private var callback: Callback? = null
    private val folderList = ArrayList<Folder>()
    private val imageList = ArrayList<Image>()

    private var folderPopupWindow: ListPopupWindow? = null
    private var imageListAdapter: ImageListAdapter? = null
    private var folderListAdapter: FolderListAdapter? = null
    private var previewAdapter: PreviewAdapter? = null

    private var hasFolderGened = false

    private var tempFile: File? = null

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
            imageList.add(Image())
        }

        imageListAdapter = ImageListAdapter(requireContext())
        imageListAdapter?.setShowCamera((config?.needCamera ?: false))
        imageListAdapter?.setMutiSelect((config?.multiSelect ?: false))
        rvImageList?.setHasFixedSize(true)
        rvImageList?.itemAnimator?.changeDuration = 0
        rvImageList?.adapter = imageListAdapter
        imageListAdapter?.addAll(imageList)
        folderListAdapter = FolderListAdapter(requireContext(),folderList)
        activity?.supportLoaderManager?.initLoader(LOADER_ALL, null, mLoaderCallback)
    }

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
            override fun onCheckedClick(position: Int, image: Image): Int {
                return checkedImage(position, image)
            }

            override fun onImageClick(position: Int, image: Image) {
                val needCamera = config?.needCamera ?: false
                if (needCamera && position == 0) {
                    showCameraAction()
                } else {
                    val needMultiSelect = config?.multiSelect ?: false
                    if (needMultiSelect) {

                        previewAdapter = PreviewAdapter(requireActivity(), imageList, config!!)

                        viewPager?.adapter = previewAdapter

                        previewAdapter?.setListener(object : OnImgItemClickListener {
                            override fun onCheckedClick(position: Int, image: Image): Int {
                                return checkedImage(position, image)
                            }

                            override fun onImageClick(position: Int, image: Image) {
                                hidePreview()
                            }
                        })
                        if (needCamera) {
                            callback?.onPreviewChanged(position, imageList.size - 1, true)
                        } else {
                            callback?.onPreviewChanged(position + 1, imageList.size, true)
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

        })
    }

    private val mLoaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {

        private val IMAGE_PROJECTION =
            arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID)

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return when (id) {
                LOADER_ALL -> {
                    CursorLoader(
                        requireContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, MediaStore.Images.Media.DATE_ADDED + " DESC"
                    )
                }
                LOADER_CATEGORY -> {
                    CursorLoader(
                        requireContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION
                        , IMAGE_PROJECTION[0] + " not like '%.gif%'", null, MediaStore.Images.Media.DATE_ADDED + " DESC"
                    )
                }
                else -> {
                    CursorLoader(
                        requireContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, MediaStore.Images.Media.DATE_ADDED + " DESC"
                    )
                }
            }
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            if (data != null) {
                val count = data.count
                if (count > 0) {
                    val tempImageList = ArrayList<Image>()
                    data.moveToFirst()
                    do {
                        val path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                        val name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                        val image = Image(path, name)
                        tempImageList.add(image)
                        if (!hasFolderGened) {
                            val imageFile = File(path)
                            val folderFile = imageFile.parentFile
                            if (folderFile == null || !imageFile.exists() || imageFile.length() < 10) {
                                continue
                            }
                            var parent: Folder? = null
                            for (folder in folderList) {
                                if (TextUtils.equals(folder.path, folderFile.absolutePath)) {
                                    parent = folder
                                }
                            }
                            if (parent != null) {
                                parent.images?.add(image)
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

                    imageList.clear()
                    if (config?.needCamera == true) {
                        imageList.add(Image())
                    }
                    imageList.addAll(tempImageList)
                    imageListAdapter?.clear()
                    imageListAdapter?.addAll(imageList)
                    folderListAdapter?.notifyDataSetChanged()
                    hasFolderGened = true
                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {

        }
    }


    private fun checkedImage(position: Int, image: Image?): Int {
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
            override fun onChange(position: Int, folder: Folder) {
                folderPopupWindow?.dismiss()
                if (position == 0) {
                    activity?.supportLoaderManager?.restartLoader(LOADER_ALL, null, mLoaderCallback)
                    btnAlbumSelected?.text = config?.allImagesText
                } else {
                    imageList.clear()
                    if (config?.needCamera == true) {
                        imageList.add(Image())
                    }
                    if (folder.images != null) {
                        imageList.addAll(folder.images!!)
                    }
                    imageListAdapter?.clear()
                    imageListAdapter?.addAll(imageList)
                    btnAlbumSelected?.text = folder.name
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
            LogUtils.e(tempFile?.absolutePath?:"")
            if(tempFile != null){
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
            ToastUtils.showSafeToast(requireContext(),getString(R.string.str_open_camera_failure))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                    if (callback != null) {
                        callback?.onCameraShot(tempFile!!)
                    }
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
                ToastUtils.showSafeToast(requireContext(),getString(R.string.str_permission_camera_denied))
            }
            else -> {
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (config?.needCamera == true) {
            callback?.onPreviewChanged(position + 1, imageList.size - 1, true)
        } else {
            callback?.onPreviewChanged(position + 1, imageList.size, true)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun hidePreview(): Boolean {
        return if (viewPager?.visibility == View.VISIBLE) {
            viewPager?.visibility = View.GONE
            callback?.onPreviewChanged(0, 0, false)
            imageListAdapter?.notifyDataSetChanged()
            true
        } else {
            false
        }
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
