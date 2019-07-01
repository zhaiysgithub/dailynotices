@file:Suppress("DEPRECATION")

package com.suncity.dailynotices.ui.bar

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle

class RequestManagerFragment : Fragment() {

    private var mDelegate: ImmersionDelegate? = null

    operator fun get(o: Any): ImmersionBar {
        if (mDelegate == null) {
            mDelegate = ImmersionDelegate(o)
        }
        return mDelegate!!.get()
    }

    operator fun get(activity: Activity, dialog: Dialog): ImmersionBar {
        if (mDelegate == null) {
            mDelegate = ImmersionDelegate(activity, dialog)
        }
        return mDelegate!!.get()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mDelegate != null) {
            mDelegate!!.onActivityCreated(resources.configuration)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mDelegate != null) {
            mDelegate!!.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDelegate != null) {
            mDelegate!!.onDestroy()
            mDelegate = null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (mDelegate != null) {
            mDelegate!!.onConfigurationChanged(newConfig)
        }
    }
}
