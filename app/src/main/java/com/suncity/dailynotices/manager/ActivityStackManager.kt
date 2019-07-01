package com.suncity.dailynotices.manager

import android.app.Activity
import java.util.*

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.manager
 * @ClassName:      ActivityStackManager
 * @Description:    Activity管理类
 * @UpdateDate:     31/5/2019
 */

object ActivityStackManager {

    @JvmStatic
    var activityStack : Stack<Activity> = Stack() //全局一个实例

    /**
     * 关闭activity
     */
    fun popActivity(activity: Activity?) {
        if (activity != null && activityStack.contains(activity)) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
            activityStack.remove(activity)
        }
    }

    /**
     * 获取当前的Activity
     */
    fun currentActivity(): Activity? {
        return if (activityStack.isEmpty()) null else activityStack.lastElement() as Activity
    }

    /**
     * 添加activity到Stack
     */
    fun pushActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * remove所有Activity
     */
    fun popAllActivity() {
        while (true) {
            if (activityStack.empty()) {
                break
            }
            val activity = currentActivity()
            popActivity(activity)
        }
    }
}