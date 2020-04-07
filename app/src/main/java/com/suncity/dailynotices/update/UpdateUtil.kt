package com.suncity.dailynotices.update

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import android.text.TextUtils
import android.util.Log

import java.io.*
import java.math.BigInteger
import java.security.MessageDigest

object UpdateUtil {
    private const val TAG = "notice.update"
    private const val PREFS = "notice.update.prefs"
    private const val KEY_IGNORE = "notice.update.prefs.ignore"
    private const val KEY_UPDATE = "notice.update.prefs.update"

    var DEBUG = true

    fun log(content: String) {
        if (DEBUG) {
            Log.i(TAG, content)
        }
    }

    fun clean(context: Context) {
        val sp = context.getSharedPreferences(PREFS, 0)
        val updateStr = sp.getString(KEY_UPDATE, "") ?: return

        val file = File(context.externalCacheDir, "$updateStr.apk")
        UpdateUtil.log("apk ==> $file")
        if (file.exists()) {
            file.delete()
        }
        sp.edit().clear().apply()
    }

    fun setUpdate(context: Context, md5: String) {
        if (TextUtils.isEmpty(md5)) {
            return
        }
        val sp = context.getSharedPreferences(PREFS, 0)
        val old = sp.getString(KEY_UPDATE, "")
        if (md5 == old) {
            UpdateUtil.log("same md5")
            return
        }
        val oldFile = File(context.externalCacheDir, old)
        if (oldFile.exists()) {
            oldFile.delete()
        }
        sp.edit().putString(KEY_UPDATE, md5).apply()
        val file = File(context.externalCacheDir, md5)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun ensureExternalCacheDir(context: Context) {
        var file = context.externalCacheDir
        if (file == null) {
            file = File(context.getExternalFilesDir("")?.parentFile, "cache")
        }
        file.mkdirs()
    }

    fun setIgnore(context: Context, md5: String) {
        context.getSharedPreferences(PREFS, 0).edit().putString(KEY_IGNORE, md5).apply()
    }

    fun isIgnore(context: Context, md5: String): Boolean {
        return !TextUtils.isEmpty(md5) && md5 == context.getSharedPreferences(PREFS, 0).getString(KEY_IGNORE, "")
    }

    fun install(context: Context, force: Boolean) {
        val md5 = context.getSharedPreferences(PREFS, 0).getString(KEY_UPDATE, "") ?: return
        val apk = File(context.externalCacheDir, md5 + ".apk")
        if (UpdateUtil.verify(apk, md5)) {
            install(context, apk, force)
        }
    }

    fun install(context: Context, file: File, force: Boolean) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        } else {
            val uri = FileProvider.getUriForFile(context, context.packageName + ".updatefileprovider", file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (force) {
            System.exit(0)
        }
    }

    fun verify(apk: File, md5: String?): Boolean {
        if (!apk.exists()) {
            return false
        }
        val apkMd5 = md5(apk)
        if (TextUtils.isEmpty(apkMd5)) {
            return false
        }
        val result = apkMd5 != null && apkMd5.equals(md5 ?: "", ignoreCase = true)
        if (!result) {
            apk.delete()
        }
        return result
    }


    fun toCheckUrl(context: Context, url: String, channel: String): String {
        val builder = StringBuilder()
        builder.append(url)
        builder.append(if (url.indexOf("?") < 0) "?" else "&")
        builder.append("package=")
        builder.append(context.packageName)
        builder.append("&version=")
        builder.append(getVersionCode(context))
        builder.append("&channel=")
        builder.append(channel)
        return builder.toString()
    }

    @Suppress("DEPRECATION")
    fun getVersionCode(context: Context): Int {
        return try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA)
            info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }

    }

    fun checkWifi(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
        val info = connectivity.activeNetworkInfo
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    fun checkNetwork(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
        val info = connectivity.activeNetworkInfo
        return info != null && info.isConnected
    }

    fun isDebuggable(context: Context): Boolean {
        try {
            return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {

        }

        return false
    }

    private fun md5(file: File): String? {
        val digest: MessageDigest?
        val fis: FileInputStream?
        val buffer = ByteArray(1024)

        try {
            if (!file.isFile) {
                return ""
            }

            digest = MessageDigest.getInstance("MD5")
            fis = FileInputStream(file)

            while (true) {
                val len: Int = fis.read(buffer, 0, 1024)
                if (len == -1) {
                    fis.close()
                    break
                }

                digest?.update(buffer, 0, len)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val var5 = BigInteger(1, digest?.digest())
        return String.format("%1$032x", var5)
    }


    @Throws(IOException::class)
    fun readString(input: InputStream): String {
        val output = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(4096)
            var n = input.read(buffer)
            while (-1 != n) {
                output.write(buffer, 0, n)
                n = input.read(buffer)
            }
            output.flush()
        } finally {
            close(input)
            close(output)
        }
        return output.toString("UTF-8")
    }

    fun close(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}