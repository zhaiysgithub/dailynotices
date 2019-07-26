package com.suncity.dailynotices.update


import android.util.SparseArray

class UpdateError @JvmOverloads constructor(private val code: Int, message: String? = null) : Throwable(make(code, message)) {
    val isError: Boolean
        get() = code >= 2000

    override fun toString(): String {
        return if (isError) {
            "[$code]$message"
        } else (message ?: "")

    }

    companion object {

        private fun make(code: Int, message: String?): String? {
            val m = messages.get(code) ?: return message
            return if (message == null) {
                m
            } else "$m($message)"
        }


        const val UPDATE_IGNORED = 1001
        const val UPDATE_NO_NEWER = 1002

        const val CHECK_UNKNOWN = 2001
        const val CHECK_NO_WIFI = 2002
        const val CHECK_NO_NETWORK = 2003
        const val CHECK_NETWORK_IO = 2004
        const val CHECK_HTTP_STATUS = 2005
        const val CHECK_PARSE = 2006


        const val DOWNLOAD_UNKNOWN = 3001
        const val DOWNLOAD_CANCELLED = 3002
        const val DOWNLOAD_DISK_NO_SPACE = 3003
        const val DOWNLOAD_DISK_IO = 3004
        const val DOWNLOAD_NETWORK_IO = 3005
        const val DOWNLOAD_NETWORK_BLOCKED = 3006
        const val DOWNLOAD_NETWORK_TIMEOUT = 3007
        const val DOWNLOAD_HTTP_STATUS = 3008
        const val DOWNLOAD_INCOMPLETE = 3009
        const val DOWNLOAD_VERIFY = 3010

        private val messages = SparseArray<String>()

        init {

            messages.append(UPDATE_IGNORED, "该版本已经忽略")
            messages.append(UPDATE_NO_NEWER, "已经是最新版了")

            messages.append(CHECK_UNKNOWN, "查询更新失败：未知错误")
            messages.append(CHECK_NO_WIFI, "查询更新失败：没有 WIFI")
            messages.append(CHECK_NO_NETWORK, "查询更新失败：没有网络")
            messages.append(CHECK_NETWORK_IO, "查询更新失败：网络异常")
            messages.append(CHECK_HTTP_STATUS, "查询更新失败：错误的HTTP状态")
            messages.append(CHECK_PARSE, "查询更新失败：解析错误")

            messages.append(DOWNLOAD_UNKNOWN, "下载失败：未知错误")
            messages.append(DOWNLOAD_CANCELLED, "下载失败：下载被取消")
            messages.append(DOWNLOAD_DISK_NO_SPACE, "下载失败：磁盘空间不足")
            messages.append(DOWNLOAD_DISK_IO, "下载失败：磁盘读写错误")
            messages.append(DOWNLOAD_NETWORK_IO, "下载失败：网络异常")
            messages.append(DOWNLOAD_NETWORK_BLOCKED, "下载失败：网络中断")
            messages.append(DOWNLOAD_NETWORK_TIMEOUT, "下载失败：网络超时")
            messages.append(DOWNLOAD_HTTP_STATUS, "下载失败：错误的HTTP状态")
            messages.append(DOWNLOAD_INCOMPLETE, "下载失败：下载不完整")
            messages.append(DOWNLOAD_VERIFY, "下载失败：校验错误")
        }
    }
}
