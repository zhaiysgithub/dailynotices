package com.suncity.dailynotices.islib.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.suncity.dailynotices.R
import com.suncity.dailynotices.islib.bean.Image
import com.suncity.dailynotices.islib.config.ISCameraConfig
import com.suncity.dailynotices.utils.FileUtils
import com.suncity.dailynotices.utils.LogUtils

import java.io.File

/**
 * @author yuyh.
 * @date 2017/4/18.
 */
class ISCameraActivity : AppCompatActivity() {

    private var cropImageFile: File? = null
    private var tempPhotoFile: File? = null

    private var config: ISCameraConfig? = null

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config = intent.getSerializableExtra("config") as ISCameraConfig
        if (config == null)
            return

        camera()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun camera() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_REQUEST_CODE
            )
            return
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (cameraIntent.resolveActivity(packageManager) != null) {
            tempPhotoFile = File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg")
            LogUtils.e(tempPhotoFile?.absolutePath ?: "")
            if(tempPhotoFile != null){
                FileUtils.createFile(tempPhotoFile!!)
                val uri = FileProvider.getUriForFile(
                    this,
                    FileUtils.getApplicationId(this) + ".image_provider", tempPhotoFile!!
                )

                val resInfoList = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    grantUriPermission(
                        packageName,
                        uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) //Uri.fromFile(tempFile)
                startActivityForResult(cameraIntent, REQUEST_CAMERA)
            }
        } else {
            Toast.makeText(this, resources.getString(R.string.str_open_camera_failure), Toast.LENGTH_SHORT).show()
        }
    }

    private fun crop(imagePath: String) {
        cropImageFile = File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg")

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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropImageFile))
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)

        startActivityForResult(intent, IMAGE_CROP_CODE)
    }

    private fun getImageContentUri(imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath), null
        )

        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                cursor?.close()
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                null
            }
        }
    }

    private fun complete(image: Image?) {
        val intent = Intent()
        if (image != null) {
            intent.putExtra("result", image.path)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CROP_CODE && resultCode == Activity.RESULT_OK) {
            complete(Image(cropImageFile?.path ?: return, cropImageFile?.name ?: return))
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempPhotoFile != null) {
                    if (config?.needCrop == true) {
                        crop(tempPhotoFile?.absolutePath ?: return)
                    } else {
                        // complete(new Image(cropImageFile.getPath(), cropImageFile.getName()));
                        complete(Image(tempPhotoFile?.path ?: return, tempPhotoFile?.name ?: return))
                    }
                }
            } else {
                if (tempPhotoFile != null && tempPhotoFile!!.exists()) {
                    tempPhotoFile!!.delete()
                }
                finish()
            }
        } else {
            finish()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.size >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                camera()
            } else {
                Toast.makeText(this, resources.getString(R.string.str_permission_camera_denied), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
            }
        }
    }

    companion object {

        fun startForResult(activity: Activity, config: ISCameraConfig, requestCode: Int) {
            val intent = Intent(activity, ISCameraActivity::class.java)
            intent.putExtra("config", config)
            activity.startActivityForResult(intent, requestCode)
        }

        fun startForResult(fragment: Fragment, config: ISCameraConfig, requestCode: Int) {
            val intent = Intent(fragment.activity, ISCameraActivity::class.java)
            intent.putExtra("config", config)
            fragment.startActivityForResult(intent, requestCode)
        }

        private const val REQUEST_CAMERA = 5
        private const val IMAGE_CROP_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }
}
