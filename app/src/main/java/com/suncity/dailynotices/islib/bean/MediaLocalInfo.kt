package com.suncity.dailynotices.islib.bean

import android.database.Cursor
import android.provider.MediaStore
import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @ClassName:      MediaLocalInfo
 */
class MediaLocalInfo : Serializable {

    var parentPath: String = ""
    var imageCounts: Int = 0
    var cover: String = ""
    var localMedias = arrayListOf<LocalMedia>()

    override fun toString(): String {
        return "MediaLocalInfo(parentPath='$parentPath', imageCounts=$imageCounts, cover='$cover', localMedias=$localMedias)"
    }


}


class LocalMedia : Serializable {

    var path: String = ""
    var name: String = ""
    var mime: String = ""
    var duration: Long = 0L

    constructor()

    constructor(uri: String, name: String, mime: String, duration: Long) {
        this.path = uri
        this.name = name
        this.mime = mime
        this.duration = duration
    }

    fun getFileUri(): String {
        return "file://$path"
    }

    fun isVideo(): Boolean {
        return mime.isNotEmpty() && mime.contains("video")
    }

    fun isGif(): Boolean {
        return mime.isNotEmpty() && mime.contains(".gif")
    }

    override fun toString(): String {
        return "LocalMedia(uri='$path', name=$name, mime='$mime', duration=$duration)"
    }


    companion object {

        fun valueOf(cursor: Cursor): LocalMedia {

            val localMedia = LocalMedia()
            localMedia.path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
            localMedia.mime = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE))
            localMedia.name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
            if (localMedia.isVideo()) {
                localMedia.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            }
            return localMedia
        }
    }
}