package com.suncity.dailynotices.utils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      SystemUtils
 * @Description:     与系统相关的工具类
 */
object SystemUtils {

    /**
     * @return 当前应用的版本名称
     */
    val packageName: String
        @Synchronized get() {
            return try {
                val context = Config.getApplicationContext()
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(
                    context.packageName, 0
                )
                packageInfo.packageName
            } catch (e: Exception) {
                ""
            }
        }

    /**
     * 获取版本名称
     *
     * @return 当前 app 的版本名称
     */
    val versionName: String
        get() {
            val context = Config.getApplicationContext()
            val packageManager = context.packageManager
            return try {
                val packInfo = packageManager.getPackageInfo(context.packageName, 0)
                packInfo.versionName
            } catch (e: Exception) {
                ""
            }

        }
}