package com.suncity.dailynotices

import android.content.Context
import android.content.pm.PackageManager
import com.suncity.dailynotices.utils.AppUtils
import com.suncity.dailynotices.utils.LogUtils

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices
 * @ClassName:      AppEnviroment
 * @Description:    全局环境配置获取
 */
class AppEnviroment private constructor(
   var protocol: String,
   var host: String,
   var tcp: String,
   var avosAppId: String,
   var avosAppKey: String,
   var applicationId: String
) {


    companion object {
        private var environment : AppEnviroment? = null

        fun getEnv(): AppEnviroment? {
            val sApplication = AppUtils.sApplication ?: return null
            if (null == environment) {
                environment = createEnv(sApplication)
            }
            return environment
        }

        private fun createEnv(context: Context): AppEnviroment? {
            return try {
                val appInfo = context.packageManager.getApplicationInfo(context.packageName,PackageManager.GET_META_DATA)
                val host = appInfo.metaData.getString("ENV_HOST") ?: ""
                val tcp = appInfo.metaData.getString("ENV_TCP") ?: ""
                val protocol = appInfo.metaData.getString("ENV_PROTOCOL") ?: ""
                val avosAppId = appInfo.metaData.getString("ENV_AVOS_APP_ID") ?: ""
                val avosAppKey = appInfo.metaData.getString("ENV_AVOS_APP_KEY") ?: ""
                val applicationId = appInfo.metaData.getString("APPLICATION_ID") ?: ""
                AppEnviroment(protocol, host, tcp, avosAppId, avosAppKey, applicationId)
            } catch (e: Exception) {
                LogUtils.e("createEnv -> error -> $e")
                null
            }

        }
    }
}