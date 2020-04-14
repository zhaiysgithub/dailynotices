package com.suncity.dailynotices.islib

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.provider.MediaStore
import com.suncity.dailynotices.islib.bean.LocalMedia
import com.suncity.dailynotices.islib.bean.MediaLocalInfo
import com.suncity.dailynotices.islib.common.PublishConstant
import com.suncity.dailynotices.utils.LogUtils
import java.io.File
import java.util.HashMap

/**
 * @ProjectName:    dailynotices
 * @ClassName:      MediaLoaderTask
 */
class MediaLoaderTask(
    private val isDesc: Boolean,
    private val mediaType: Int,
    private val mediaInterface: MediaLoaderInterface?
) :
    AsyncTask<Context, Int, ArrayList<MediaLocalInfo>>() {

    private val searchURI = MediaStore.Files.getContentUri("external")
    private val projections = arrayOf(
        MediaStore.Files.FileColumns.DATA,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Video.VideoColumns.DURATION
    )


    private val filterMime = PublishConstant.mimeTypes.map { it.mMimeTypeName }.toTypedArray()
    private val pathIndexes = ArrayList<Int>()

    private val sortOrder: String
        get() = MediaStore.Files.FileColumns.DATE_MODIFIED + if (isDesc) " DESC " else " ASC "


    //处理 type 条件
    // 图片
    // 视频
    // 图片跟视频
    //处理 mime 条件
    //处理 parentIndex 条件
    private val selectionAndArgs: Pair<String, Array<String>>
        get() {
            val selection = StringBuilder()
            val args = ArrayList<String>()
            selection.append("(")
            when (mediaType) {
                PublishConstant.IMAGES -> {
                    selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=? ")
                    args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                }
                PublishConstant.VIDEOS -> {
                    selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=? ")
                    args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                }
                PublishConstant.IMAGES_AND_VIDEOS -> {
                    selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=? ")
                    args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                    selection.append("OR ")
                    selection.append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=? ")
                    args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                }
            }
            selection.append(") ")
            if (filterMime.isNotEmpty()) {
                selection.append("AND (")
                var index = 1
                for (mime in filterMime) {
                    selection.append(MediaStore.Files.FileColumns.MIME_TYPE).append("=? ")
                    if (index != filterMime.size) {
                        selection.append("OR ")
                    }
                    index++
                    args.add(mime)
                }
                selection.append(") ")
            }
            if (pathIndexes.isNotEmpty()) {
                selection.append("AND (")
                var index = 1
                for (pathIndex in pathIndexes) {
                    if (pathIndex < 0) {
                        continue
                    }

                    selection.append(MediaStore.Files.FileColumns.PARENT).append("=? ")
                    if (index != pathIndexes.size) {
                        selection.append("OR ")
                    }
                    index++
                    args.add(pathIndex.toString())
                }
                selection.append(") ")
            }

            return Pair(selection.toString(), args.toTypedArray())
        }

    override fun doInBackground(vararg params: Context): ArrayList<MediaLocalInfo>? {

        var mCursor: Cursor? = null
        try {
            val t1 = System.currentTimeMillis()
            val resolver = params[0].contentResolver
            val selectionAndArgs = selectionAndArgs
            mCursor = resolver.query(
                searchURI, projections, selectionAndArgs.first,
                selectionAndArgs.second, sortOrder + getRange(0, 0)
            )
            if (mCursor == null || isCancelled) {
                return null
            }
            val t2 = System.currentTimeMillis()
            LogUtils.e(" time-doInBackground  :" + (t2 - t1))
            return subGroupOfImage(mCursor)
        } catch (e: Exception) {
            LogUtils.e("相册读取失败:$e")
        } finally {
            mCursor?.close()
        }
        return null
    }

    override fun onPostExecute(result: ArrayList<MediaLocalInfo>?) {
        mediaInterface?.let {
            if (result != null) {
                it.onSuccess(result)
            } else {
                it.onFailure()
            }
        }
    }

    /**
     * 组装分组界面GridView的数据源，因扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     */
    private fun subGroupOfImage(cursor: Cursor): ArrayList<MediaLocalInfo>? {
        val mGroupMap = HashMap<String, ArrayList<LocalMedia>>()
        val allInfo = MediaLocalInfo()
        while (cursor.moveToNext()) {
            if (isCancelled) return arrayListOf()
            val media = LocalMedia.valueOf(cursor)
            //媒体文件
            if (media.isVideo() && media.duration < 1500) { //视频文件小于 1.5S 过滤
                continue
            }
            val file = File(media.path)
            if (file.exists() && file.isFile && file.length() <= 512) { //过滤 512byte 以下的文件
                continue
            }
            allInfo.localMedias.add(media)
            val folderName = file.parentFile.name
            if (mGroupMap.containsKey(folderName)) {
                mGroupMap[folderName]?.add(media)
            } else {
                mGroupMap[folderName] = arrayListOf(media)
            }
        }
        val t2 = System.currentTimeMillis()
        if (mGroupMap.size == 0 || isCancelled) {
            return arrayListOf()
        }
        val list = ArrayList<MediaLocalInfo>()
        val iterator = mGroupMap.entries.iterator()
        while (iterator.hasNext() && !isCancelled) {
            if (isCancelled) return arrayListOf()
            val entry = iterator.next()
            val info = MediaLocalInfo()
            val key = entry.key
            val value = entry.value
            info.parentPath = key
            info.imageCounts = value.size
            info.cover = value[0].getFileUri()//获取该组的第一张图片
            info.localMedias = value
            list.add(info)
        }
        val t3 = System.currentTimeMillis()
        LogUtils.e("   time-while2    " + (t3 - t2))
//        allInfo.imageCounts = allInfo.localMedias.size
//        allInfo.cover = allInfo.localMedias[0].getFileUri()
//        allInfo.parentPath = context.getString(R.string.str_all)
//        list.add(0, allInfo)
        return if (isCancelled) arrayListOf() else list
    }


    private fun getRange(limit: Int, offset: Int): String {
        val builder = StringBuilder()
        if (limit != 0) {
            builder.append("limit ").append(limit).append(" ")
            if (offset != 0) {
                builder.append("offset ").append(offset)
            }
        }
        return builder.toString()
    }
}