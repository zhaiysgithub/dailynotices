package com.suncity.dailynotices.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.dialog.PictureDialog
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.entity.LocalMediaFolder
import com.luck.picture.lib.model.LocalMediaLoader
import com.luck.picture.lib.observable.ImagesObservable
import com.luck.picture.lib.permissions.RxPermissions
import com.luck.picture.lib.tools.PictureFileUtils
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.ui.adapter.PicFolderAdapter
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.ui.views.recyclerview.DividerDecoration
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.ac_pic_folder.*
import kotlinx.android.synthetic.main.view_title.*
import java.io.File
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.activity
 * @ClassName:      PicSelectorActivity
 * @Description:     图片选择页面
 * @UpdateDate:     18/7/2019
 */
open class PicFolderActivity : BaseActivity() {

    private val STRADDDYNAMIC = Config.getString(R.string.str_add_dynamic)
    private val PIC_JURISDICTION = Config.getString(R.string.str_picture_jurisdiction)
    private val PIC_ALL_AUDIO = Config.getString(R.string.str_picture_all_audio)
    private val PIC_CAMERA_ROLL = Config.getString(R.string.str_picture_camera_roll)
    private val color_divider = Config.getColor(R.color.color_f3f3f3)
    private val height_divider = Config.getDimension(R.dimen.px_1).toInt()
    private val padding_divider = Config.getDimension(R.dimen.dp_20).toInt()
    private var rxPermission: RxPermissions? = null
    private var dialog: PictureDialog? = null
    private var mediaLoader: LocalMediaLoader? = null
    private var config: PictureSelectionConfig? = null
    private var mAdapter: PicFolderAdapter? = null
    private var folderList : MutableList<LocalMediaFolder> = mutableListOf()
    private var images: MutableList<LocalMedia> = ArrayList()
    private var cameraPath = ""

    companion object {
        private const val SHOW_DIALOG = 0
        private const val DISMISS_DIALOG = 1
        private const val EXTRA_CONFIG = "PictureConfig"
    }

    override fun onCreateSaveInstance(savedInstanceState: Bundle?) {
        config = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(EXTRA_CONFIG)
        } else {
            PictureSelectionConfig.getInstance()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(EXTRA_CONFIG, config)
    }

    override fun setScreenManager() {

        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0f)
            .statusBarColor(R.color.color_white)
            .fitsSystemWindows(true)
            .init()
    }

    override fun getActivityLayoutId(): Int {
        return R.layout.ac_pic_folder
    }

    override fun initData() {
        tv_title_center?.text = STRADDDYNAMIC
        mAdapter = PicFolderAdapter(this)
        recyclerView_pic_folder?.setHasFixedSize(true)
        recyclerView_pic_folder?.layoutManager = LinearLayoutManager(this)
        recyclerView_pic_folder?.addItemDecoration(DividerDecoration(color_divider,height_divider,padding_divider,padding_divider))
        recyclerView_pic_folder.adapter = mAdapter
        requestPicPermission()

    }

    private fun bindAdapter(folders:MutableList<LocalMediaFolder>){
        mAdapter?.addAll(folders)
    }

    override fun initListener() {
        fl_title_back?.setOnClickListener {
            finish()
        }
    }
    private fun requestPicPermission() {
        mediaLoader = LocalMediaLoader(
            this,
            (config?.mimeType ?: 1),
            (config?.isGif ?: false),
            (Long.MAX_VALUE),
            0L
        )
        rxPermission = RxPermissions(this)
        rxPermission?.request(Manifest.permission.READ_EXTERNAL_STORAGE)?.subscribe(object : Observer<Boolean> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: Boolean?) {
                if (t == true) {
                    mHandler.sendEmptyMessage(SHOW_DIALOG)
                    readLocalMedia()
                } else {
                    ToastUtils.showSafeToast(this@PicFolderActivity, PIC_JURISDICTION)
                }
            }

            override fun onError(e: Throwable?) {
            }

        })
    }


    /**
     * 读取本地的media类型
     */
    private fun readLocalMedia() {
        mediaLoader?.loadAllMedia {
            if(it.size > 0){
                folderList = it
                val folder = it[0]
                folder.isChecked = true
                val localImgs = folder.images
                // 如果查询出来的图片大于或等于当前adapter集合的图片则取更新后的，否则就取本地的
                if(localImgs.size >= images.size){
                    images = localImgs
                    //绑定数据
                    bindAdapter(it)
                }
            }
        }
    }

    private val mHandler = Handler {
        when (it.what) {
            SHOW_DIALOG -> {
                showPleaseDialog()
            }
            DISMISS_DIALOG -> {
                dismissDialog()
            }
            else -> {
            }
        }
        true
    }

    /**
     * 加载 dialog
     */
    private fun showPleaseDialog() {
        if (!isFinishing) {
            dismissDialog()
            dialog = PictureDialog(this)
            dialog?.show()
        }
    }

    /**
     * 取消 dialog
     */
    private fun dismissDialog() {
        try {
            if (dialog != null && dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                PictureConfig.REQUEST_CAMERA -> {

                    if (config?.mimeType == PictureMimeType.ofAudio()) {
                        cameraPath = getAudioPath(data) ?: ""
                    }
                    val file = File(cameraPath)
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
                    val toType = PictureMimeType.fileToType(file)
                    if (config?.mimeType != PictureMimeType.ofAudio()) {
                        val degree = PictureFileUtils.readPictureDegree(file.absolutePath)
                        rotateImage(degree, file)
                    }
                    val media = LocalMedia()
                    media.path = cameraPath
                    val eqVideo = toType.startsWith(PictureConfig.VIDEO)
                    var duration = if (eqVideo) PictureMimeType.getLocalVideoDuration(cameraPath) else 0
                    val pictureType: String
                    if (config?.mimeType == PictureMimeType.ofAudio()) {
                        pictureType = "audio/mpeg"
                        duration = PictureMimeType.getLocalVideoDuration(cameraPath)
                    } else {
                        pictureType = if (eqVideo)
                            PictureMimeType.createVideoType(cameraPath)
                        else
                            PictureMimeType.createImageType(cameraPath)
                    }
                    media.pictureType = pictureType
                    media.duration = duration.toLong()
                    media.mimeType = config?.mimeType ?: PictureConfig.TYPE_IMAGE
                    // 解决部分手机拍照完Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                    // 不及时刷新问题手动添加
                    manualSaveFolder(media)
                }
            }
        }
    }

    /**
     * 判断拍照 图片是否旋转
     *
     * @param degree
     * @param file
     */
    private fun rotateImage(degree: Int, file: File) {
        if (degree > 0) {
            // 针对相片有旋转问题的处理方式
            try {
                val opts = BitmapFactory.Options()//获取缩略图显示到屏幕上
                opts.inSampleSize = 2
                val bitmap = BitmapFactory.decodeFile(file.absolutePath, opts)
                val bmp = PictureFileUtils.rotaingImageView(degree, bitmap)
                PictureFileUtils.saveBitmapFile(bmp, file)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    /**
     * 录音
     *
     * @param data
     */
    private fun getAudioPath(data: Intent?): String? {
        val compare_SDK_19 = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT
        if (data != null && config?.mimeType == PictureMimeType.ofAudio()) {
            try {
                val uri = data.data
                val audioPath: String?
                audioPath = if (compare_SDK_19) {
                    uri?.path
                } else {
                    if(uri != null){
                        getAudioFilePathFromUri(uri)
                    }else{
                        ""
                    }
                }
                return audioPath

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return ""
    }

    /**
     * 获取刚录取的音频文件
     *
     * @param uri
     * @return
     */
    @SuppressLint("Recycle")
    private fun getAudioFilePathFromUri(uri: Uri): String {
        var path = ""
        try {
            val cursor = contentResolver
                .query(uri, null, null, null, null)
            cursor!!.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
            path = cursor.getString(index)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return path
    }

    /**
     * 手动添加拍照后的相片到图片列表，并设为选中
     *
     * @param media
     */
    private fun manualSaveFolder(media: LocalMedia) {
        try {
            createNewFolder(folderList)
            val folder = getImageFolder(media.path, folderList)
            val cameraFolder = if (folderList.size > 0) folderList.get(0) else null
            if (cameraFolder != null) {
                // 相机胶卷
                cameraFolder.firstImagePath = media.path
                cameraFolder.images = images
                cameraFolder.imageNum = cameraFolder.imageNum + 1
                // 拍照相册
                val num = folder.imageNum + 1
                folder.imageNum = num
                folder.images.add(0, media)
                folder.firstImagePath = cameraPath
                bindAdapter(folderList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 如果没有任何相册，先创建一个最近相册出来
     *
     * @param folders
     */
    private fun createNewFolder(folders: MutableList<LocalMediaFolder>) {
        if (folders.size == 0) {
            // 没有相册 先创建一个最近相册出来
            val newFolder = LocalMediaFolder()
            val folderName = if (config?.mimeType == PictureMimeType.ofAudio())
                PIC_ALL_AUDIO
            else
                PIC_CAMERA_ROLL
            newFolder.name = folderName
            newFolder.path = ""
            newFolder.firstImagePath = ""
            folders.add(newFolder)
        }
    }

    /**
     * 将图片插入到相机文件夹中
     *
     * @param path
     * @param imageFolders
     * @return
     */
    private fun getImageFolder(path: String, imageFolders: MutableList<LocalMediaFolder>): LocalMediaFolder {
        val imageFile = File(path)
        val folderFile = imageFile.parentFile

        for (folder in imageFolders) {
            if (folder.name == folderFile.name) {
                return folder
            }
        }
        val newFolder = LocalMediaFolder()
        newFolder.name = folderFile.name
        newFolder.path = folderFile.absolutePath
        newFolder.firstImagePath = path
        imageFolders.add(newFolder)
        return newFolder
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
        ImagesObservable.getInstance().clearLocalMedia()
    }
}