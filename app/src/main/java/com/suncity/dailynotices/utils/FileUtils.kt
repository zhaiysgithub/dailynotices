package com.suncity.dailynotices.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Environment

import java.io.File

object FileUtils {

    val isSdCardAvailable: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    /**
     * 创建根缓存目录
     *
     * @return
     */
    fun createRootPath(context: Context): String {
        return if (isSdCardAvailable) {
            // /sdcard/Android/data/<application package>/cache
            context.externalCacheDir?.path ?: ""
        } else {
            // /data/data/<application package>/cache
            context.cacheDir.path
        }
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    fun createDir(dirPath: String): String {
        try {
            val file = File(dirPath)
            if (file.parentFile.exists()) {
                LogUtils.e("----- 创建文件夹" + file.absolutePath)
                file.mkdir()
                return file.absolutePath
            } else {
                createDir(file.parentFile.absolutePath)
                LogUtils.e("----- 创建文件夹" + file.absolutePath)
                file.mkdir()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dirPath
    }

    /**
     * 递归创建文件夹
     *
     * @param file
     * @return 创建失败返回""
     */
    fun createFile(file: File): String {
        try {
            if (file.parentFile.exists()) {
                LogUtils.e("----- 创建文件" + file.absolutePath)
                file.createNewFile()
                return file.absolutePath
            } else {
                createDir(file.parentFile.absolutePath)
                file.createNewFile()
                LogUtils.e("----- 创建文件" + file.absolutePath)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    @Throws(IllegalArgumentException::class)
    fun getApplicationId(appContext: Context): String {
        val applicationInfo: ApplicationInfo?
        return try {
            applicationInfo =
                appContext.packageManager.getApplicationInfo(appContext.packageName, PackageManager.GET_META_DATA)
            if (applicationInfo == null) {
                throw IllegalArgumentException(" get application info = null, has no meta data! ")
            }
            LogUtils.e(appContext.packageName + " " + applicationInfo.metaData.getString("APP_ID"))
            (applicationInfo.metaData.getString("APP_ID") ?: "")
        } catch (e: PackageManager.NameNotFoundException) {
    //            throw IllegalArgumentException(" get application info error! ", e)
            ""
        }

    }
}
