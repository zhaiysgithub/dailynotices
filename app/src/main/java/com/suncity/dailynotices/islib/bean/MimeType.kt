package com.suncity.dailynotices.islib.bean

import java.util.*

/**
 * @ProjectName:    dailynotices
 * @ClassName:      MimeType
 */

enum class MimeType(val mMimeTypeName: String, val mExtensions: Set<String>) {

    JPEG(
        "image/jpeg", hashSetOf(
            "jpg",
            "jpeg"
        )
    ),
    PNG(
        "image/png", hashSetOf(
            "png"
        )
    ),
    GIF(
        "image/gif", hashSetOf(
            "gif"
        )
    ),
    MPEG(
        "video/mpeg", hashSetOf(
            "mpeg",
            "mpg"
        )
    ),
    MP4(
        "video/mp4", hashSetOf(
            "mp4",
            "m4v"
        )
    ),
    QUICKTIME(
        "video/quicktime", hashSetOf(
            "mov"
        )
    ),
    THREEGPP(
        "video/3gpp", hashSetOf(
            "3gp",
            "3gpp"
        )
    ),
    THREEGPP2(
        "video/3gpp2", hashSetOf(
            "3g2",
            "3gpp2"
        )
    ),
    MKV(
        "video/x-matroska", hashSetOf(
            "mkv"
        )
    ),
    WEBM(
        "video/webm", hashSetOf(
            "webm"
        )
    ),
    TS(
        "video/mp2ts", hashSetOf(
            "ts"
        )
    ),
    AVI(
        "video/avi", hashSetOf(
            "avi"
        )
    );

    companion object {

        fun ofAll(): EnumSet<MimeType> {
            return EnumSet.allOf(MimeType::class.java)
        }

        fun of(type: MimeType, vararg rest: MimeType): EnumSet<MimeType> {
            return EnumSet.of(type, *rest)
        }

        fun ofImage(): EnumSet<MimeType> {
            return EnumSet.of(JPEG, PNG, GIF)
        }

        fun ofStaticImage(): EnumSet<MimeType> {
            return EnumSet.allOf(MimeType::class.java).apply { remove(GIF) }
        }

        fun ofVideo(): EnumSet<MimeType> {
            return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI)
        }

        fun isImage(mimeType: String?): Boolean {
            return mimeType?.startsWith("image") ?: false
        }

        fun isVideo(mimeType: String?): Boolean {
            return mimeType?.startsWith("video") ?: false
        }

        fun isGif(mimeType: String?): Boolean {
            return if (mimeType == null) false else mimeType == GIF.toString()
        }
    }
}