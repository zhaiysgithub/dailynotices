

package com.suncity.dailynotices.ui.bar

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

import java.util.HashMap

class RequestManagerRetriever private constructor() : Handler.Callback {

    private val mTag = ImmersionBar::class.java.name

    private var mHandler: Handler = Handler(Looper.getMainLooper(), this)

    @Suppress("DEPRECATION")
    private val mPendingFragments = HashMap<android.app.FragmentManager, RequestManagerFragment>()
    private val mPendingSupportFragments = HashMap<FragmentManager, SupportRequestManagerFragment>()

    private object Holder {
        val INSTANCE = RequestManagerRetriever()
    }

    @Suppress("DEPRECATION")
    operator fun get(activity: Activity): ImmersionBar {
        checkNotNull(activity, "activity is null")
        return if (activity is FragmentActivity) {
            getSupportFragment(activity.supportFragmentManager, mTag + activity.toString())!!.get(
                activity
            )
        } else {
            getFragment(activity.fragmentManager, mTag + activity.toString())!![activity]
        }
    }

    operator fun get(fragment: Fragment): ImmersionBar {
        checkNotNull(fragment, "fragment is null")
        checkNotNull(fragment.activity, "fragment.getActivity() is null")
        if (fragment is DialogFragment) {
            checkNotNull(fragment.dialog, "fragment.getDialog() is null")
        }
        return getSupportFragment(fragment.childFragmentManager, mTag + fragment.toString())!!.get(fragment)
    }

    @Suppress("DEPRECATION")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    operator fun get(fragment: android.app.Fragment): ImmersionBar {
        checkNotNull(fragment, "fragment is null")
        checkNotNull(fragment.activity, "fragment.getActivity() is null")
        if (fragment is android.app.DialogFragment) {
            checkNotNull(fragment.dialog, "fragment.getDialog() is null")
        }
        return getFragment(fragment.childFragmentManager, mTag + fragment.toString())!![fragment]
    }

    @Suppress("DEPRECATION")
    operator fun get(activity: Activity, dialog: Dialog): ImmersionBar {
        checkNotNull(activity, "activity is null")
        checkNotNull(dialog, "dialog is null")
        return if (activity is FragmentActivity) {
            getSupportFragment(activity.supportFragmentManager, mTag + dialog.toString())!!.get(
                activity,
                dialog
            )
        } else {
            getFragment(activity.fragmentManager, mTag + dialog.toString())!![activity, dialog]
        }
    }

    @Suppress("DEPRECATION")
    fun destroy(activity: Activity?, dialog: Dialog?) {
        if (activity == null || dialog == null) {
            return
        }
        if (activity is FragmentActivity) {
            val fragment = getSupportFragment(activity.supportFragmentManager, mTag + dialog.toString(), true)
            fragment?.get(activity, dialog)?.destroy()
        } else {
            val fragment = getFragment(activity.fragmentManager, mTag + dialog.toString(), true)
            if (fragment != null) {
                fragment[activity, dialog].destroy()
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun handleMessage(msg: Message): Boolean {
        var handled = true
        when (msg.what) {
            ID_REMOVE_FRAGMENT_MANAGER -> {
                val fm = msg.obj as android.app.FragmentManager
                mPendingFragments.remove(fm)
            }
            ID_REMOVE_SUPPORT_FRAGMENT_MANAGER -> {
                val supportFm = msg.obj as FragmentManager
                mPendingSupportFragments.remove(supportFm)
            }
            else -> handled = false
        }
        return handled
    }

    @Suppress("DEPRECATION")
    private fun getFragment(
        fm: android.app.FragmentManager,
        tag: String,
        destroy: Boolean = false
    ): RequestManagerFragment? {
        var fragment: RequestManagerFragment? = fm.findFragmentByTag(tag) as RequestManagerFragment
        if (fragment == null) {
            fragment = mPendingFragments[fm]
            if (fragment == null) {
                if (destroy) {
                    return null
                }
                fragment = RequestManagerFragment()
                mPendingFragments[fm] = fragment
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget()
            }
        }
        if (destroy) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return null
        }
        return fragment
    }

    private fun getSupportFragment(
        fm: FragmentManager,
        tag: String,
        destroy: Boolean = false
    ): SupportRequestManagerFragment? {
        var fragment = fm.findFragmentByTag(tag) as SupportRequestManagerFragment?
        if (fragment == null) {
            fragment = mPendingSupportFragments[fm]
            if (fragment == null) {
                if (destroy) {
                    return null
                }
                fragment = SupportRequestManagerFragment()
                mPendingSupportFragments[fm] = fragment
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget()
            }
        }
        if (destroy) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return null
        }
        return fragment
    }

    companion object {

        private const val ID_REMOVE_FRAGMENT_MANAGER = 1
        private const val ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2

        val instance: RequestManagerRetriever
            get() = Holder.INSTANCE

        private fun <T> checkNotNull(arg: T?, message: String) {
            if (arg == null) {
                throw NullPointerException(message)
            }
        }
    }
}
