package com.suncity.dailynotices.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.suncity.dailynotices.manager.ActivityStackManager
import java.lang.ref.WeakReference

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.utils
 * @ClassName:      AppUtils
 * @Description:     作用描述
 * @UpdateDate:     31/5/2019
 */

object AppUtils {

    @JvmStatic
    var sApplication: Application? = null
        get() {
         if(field == null){
             LogUtils.e("AppUtils -> sApplication == null")
             return null
         }
            return field
     }
    @JvmStatic
    var sTopActivityWeakRef: WeakReference<Activity>? = null
    @JvmStatic
    val lifecycleCallback = object : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            ActivityStackManager.pushActivity(activity)
            setTopActivityWeakRef(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            setTopActivityWeakRef(activity)
        }


        override fun onActivityResumed(activity: Activity) {
            setTopActivityWeakRef(activity)
        }

        override fun onActivityPaused(activity: Activity) {
        }


        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            ActivityStackManager.pushActivity(activity)
        }
    }

    fun init(app: Application) {
        sApplication = app
        sApplication?.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    private fun setTopActivityWeakRef(activity: Activity) {
        if (sTopActivityWeakRef == null || activity != sTopActivityWeakRef?.get()) {
            sTopActivityWeakRef = WeakReference(activity)
        }
    }

}