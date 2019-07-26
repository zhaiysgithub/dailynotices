package com.suncity.dailynotices.update

import org.json.JSONException
import org.json.JSONObject

class UpdateInfo {
    // 是否有新版本
    var hasUpdate = false
    // 是否静默下载：有新版本时不提示直接下载
    var isSilent = false
    // 是否强制安装：不安装无法使用app
    var isForce = false
    // 是否下载完成后自动安装
    var isAutoInstall = true
    // 是否可忽略该版本
    var isIgnorable = true
    // 一天内最大提示次数，<1时不限
    var maxTimes = 0

    var versionCode: Int = 0
    var versionName: String = ""

    var updateContent: String = ""

    var url: String = ""
    var md5: String = ""
    var size: Long = 0

    companion object {

        @Throws(JSONException::class)
        fun parse(s: String): UpdateInfo {
            val o = JSONObject(s)
            return parse(if (o.has("data")) o.getJSONObject("data") else o)
        }

        private fun parse(o: JSONObject?): UpdateInfo {
            val info = UpdateInfo()
            if (o == null) {
                return info
            }
            info.hasUpdate = o.optBoolean("hasUpdate", false)
            if (!info.hasUpdate) {
                return info
            }
            info.isSilent = o.optBoolean("isSilent", false)
            info.isForce = o.optBoolean("isForce", false)
            info.isAutoInstall = o.optBoolean("isAutoInstall", !info.isSilent)
            info.isIgnorable = o.optBoolean("isIgnorable", true)

            info.versionCode = o.optInt("versionCode", 0)
            info.versionName = o.optString("versionName")
            info.updateContent = o.optString("updateContent")

            info.url = o.optString("url")
            info.md5 = o.optString("md5")
            info.size = o.optLong("size", 0)

            return info
        }
    }
}