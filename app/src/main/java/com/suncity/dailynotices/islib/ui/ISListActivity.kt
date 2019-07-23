package com.suncity.dailynotices.islib.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.*
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.common.Callback
import com.suncity.dailynotices.islib.common.Constant
import com.suncity.dailynotices.islib.config.ISListConfig
import com.suncity.dailynotices.islib.ui.fragment.ImgSelFragment
import com.suncity.dailynotices.ui.BaseActivity
import com.suncity.dailynotices.utils.FileUtils
import com.suncity.dailynotices.ui.bar.ImmersionBar
import com.suncity.dailynotices.utils.Config
import kotlinx.android.synthetic.main.is_activity_img_sel.*

import java.io.File
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.islib.ui
 * @ClassName:      ISListActivity
 * @Description:    发布动态的页面
 */
class ISListActivity : BaseActivity(), Callback {


    override fun getActivityLayoutId(): Int {
        return R.layout.is_activity_img_sel
    }

    private var STR_CONFIRM = Config.getString(R.string.str_confirm)

    var config: ISListConfig? = null

    private var cropImagePath: String? = null

    private var fragment: ImgSelFragment? = null

    private val result = ArrayList<String>()

    override fun setScreenManager() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_white)
            .statusBarDarkFont(true, 0f)
            .init()
    }

    override fun initData() {
        config = intent.getSerializableExtra("config") as ISListConfig
        checkStoragePermission()
        if (!FileUtils.isSdCardAvailable) {
            Toast.makeText(this, getString(R.string.str_sd_disable), Toast.LENGTH_SHORT).show()
        }

        if (config != null) {
            if (config?.multiSelect == true) {
                if (config?.rememberSelected == false) {
                    Constant.imageList.clear()
                }
                formartConfirm()
            } else {
                Constant.imageList.clear()
                tvConfirm?.visibility = View.GONE
            }
        }
    }

    private fun checkStoragePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_REQUEST_CODE)
        } else {
            fragment = ImgSelFragment.instance()
            if (fragment != null) {
                supportFragmentManager.beginTransaction().add(R.id.fmImageList, fragment!!, null).commit()
            }
        }
        return true
    }

    override fun initListener() {
        tvConfirm?.setOnClickListener {
            if (!Constant.imageList.isEmpty()) {
                exit()
            } else {
                Toast.makeText(this, getString(R.string.str_minnum), Toast.LENGTH_SHORT).show()
            }
        }
        ivBack?.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onSingleImageSelected(path: String) {
        if (config?.needCrop == true) {
            crop(path)
        } else {
            Constant.imageList.add(path)
            exit()
        }
    }

    override fun onImageSelected(path: String) {
        formartConfirm()
    }

    override fun onImageUnselected(path: String) {
        formartConfirm()
    }

    private fun formartConfirm(){
        tvConfirm?.text = String.format(
            getString(R.string.str_confirm_format),
            STR_CONFIRM,
            Constant.imageList.size,
            (config?.maxNum ?: 9)
        )

    }

    override fun onCameraShot(imageFile: File) {
        if (config?.needCrop == true) {
            crop(imageFile.absolutePath)
        } else {
            Constant.imageList.add(imageFile.absolutePath)
            config?.multiSelect = false // 多选点击拍照，强制更改为单选
            exit()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onPreviewChanged(select: Int, sum: Int, visible: Boolean) {
        if (visible) {
            tvTitle?.text = "$select/$sum"
        }
    }

    private fun crop(imagePath: String) {
        val file = File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg")

        cropImagePath = file.absolutePath
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(getImageContentUri(File(imagePath)), "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", config?.aspectX)
        intent.putExtra("aspectY", config?.aspectY)
        intent.putExtra("outputX", config?.outputX)
        intent.putExtra("outputY", config?.outputY)
        intent.putExtra("scale", true)
        intent.putExtra("scaleUpIfNeeded", true)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)
        startActivityForResult(intent, IMAGE_CROP_CODE)
    }

    @SuppressLint("Recycle")
    private fun getImageContentUri(imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(
                cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID)
            )
            val baseUri = Uri.parse("content://media/external/images/media")
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            return if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_CROP_CODE && resultCode == Activity.RESULT_OK) {
            if(cropImagePath != null){
                Constant.imageList.add(cropImagePath!!)
            }
            config?.multiSelect = false // 多选点击拍照，强制更改为单选
            exit()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun exit() {
        val intent = Intent()
        result.clear()
        result.addAll(Constant.imageList)
        intent.putStringArrayListExtra(INTENT_RESULT, result)
        setResult(Activity.RESULT_OK, intent)

        if (config?.multiSelect == false) {
            Constant.imageList.clear()
        }

        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fmImageList, ImgSelFragment.instance(), null)
                    .commitAllowingStateLoss()
            } else {
                Toast.makeText(this, getString(R.string.str_permission_storage_denied), Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("config", config)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        config = savedInstanceState.getSerializable("config") as ISListConfig
    }

    override fun onBackPressed() {
        if (fragment == null || (fragment?.hidePreview() == false)) {
            Constant.imageList.clear()
            super.onBackPressed()
        }
    }


    companion object {

        const val INTENT_RESULT = "result"
        const val IMAGE_CROP_CODE = 1
        const val STORAGE_REQUEST_CODE = 1

        fun startForResult(activity: Activity, config: ISListConfig, RequestCode: Int) {
            val intent = Intent(activity, ISListActivity::class.java)
            intent.putExtra("config", config)
            activity.startActivityForResult(intent, RequestCode)
        }

        fun startForResult(fragment: Fragment, config: ISListConfig, RequestCode: Int) {
            val intent = Intent(fragment.activity, ISListActivity::class.java)
            intent.putExtra("config", config)
            fragment.startActivityForResult(intent, RequestCode)
        }
    }
}
