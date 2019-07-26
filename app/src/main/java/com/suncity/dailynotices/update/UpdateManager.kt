package com.suncity.dailynotices.update

import android.content.Context
import android.text.TextUtils

import java.nio.charset.Charset


object UpdateManager {

    private var sUrl: String? = null
    private var sChannel: String? = null
    // 非wifi网络不检查更新
    private var sIsWifiOnly = true

    fun setWifiOnly(wifiOnly: Boolean) {
        sIsWifiOnly = wifiOnly
    }

    fun setUrl(url: String, channel: String) {
        sUrl = url
        sChannel = channel
    }

    fun setDebuggable(debuggable: Boolean) {
        UpdateUtil.DEBUG = debuggable
    }

    fun install(context: Context) {

        UpdateUtil.install(context, true)
    }

    fun check(context: Context) {
        create(context).check()
    }

    fun checkManual(context: Context) {
        create(context).setManual(true).check()
    }

    fun create(context: Context): Builder {
        UpdateUtil.ensureExternalCacheDir(context)
        return Builder(context).setWifiOnly(sIsWifiOnly)
    }

    class Builder(private val mContext: Context) {
        private var mUrl: String? = null
        private var mPostData: ByteArray? = null
        private var mIsManual: Boolean = false
        private var mIsWifiOnly: Boolean = false
        private var mNotifyId = 0

        private var mOnNotificationDownloadListener: OnDownloadListener? = null
        private var mOnDownloadListener: OnDownloadListener? = null
        private var mPrompter: IUpdatePrompter? = null
        private var mOnFailureListener: OnFailureListener? = null

        private var mParser: IUpdateParser? = null
        private var mChecker: IUpdateChecker? = null
        private var mDownloader: IUpdateDownloader? = null

        fun setUrl(url: String): Builder {
            mUrl = url
            return this
        }

        fun setPostData(data: ByteArray): Builder {
            mPostData = data
            return this
        }

        fun setPostData(data: String): Builder {
            mPostData = data.toByteArray(Charset.forName("UTF-8"))
            return this
        }

        fun setNotifyId(notifyId: Int): Builder {
            mNotifyId = notifyId
            return this
        }

        fun setManual(isManual: Boolean): Builder {
            mIsManual = isManual
            return this
        }

        fun setWifiOnly(isWifiOnly: Boolean): Builder {
            mIsWifiOnly = isWifiOnly
            return this
        }

        fun setParser(parser: IUpdateParser): Builder {
            mParser = parser
            return this
        }

        fun setChecker(checker: IUpdateChecker): Builder {
            mChecker = checker
            return this
        }

        fun setDownloader(downloader: IUpdateDownloader): Builder {
            mDownloader = downloader
            return this
        }

        fun setPrompter(prompter: IUpdatePrompter): Builder {
            mPrompter = prompter
            return this
        }

        fun setOnNotificationDownloadListener(listener: OnDownloadListener): Builder {
            mOnNotificationDownloadListener = listener
            return this
        }

        fun setOnDownloadListener(listener: OnDownloadListener): Builder {
            mOnDownloadListener = listener
            return this
        }

        fun setOnFailureListener(listener: OnFailureListener): Builder {
            mOnFailureListener = listener
            return this
        }

        fun check() {
            val now = System.currentTimeMillis()
            if (now - sLastTime < 3000) {
                return
            }
            sLastTime = now

            if (TextUtils.isEmpty(mUrl) && sUrl != null && sChannel != null) {
                mUrl = UpdateUtil.toCheckUrl(mContext, sUrl!!, sChannel!!)
            }

            val agent = UpdateAgent(mContext, mUrl, mIsManual, mIsWifiOnly, mNotifyId)
            if (mOnNotificationDownloadListener != null) {
                agent.setOnNotificationDownloadListener(mOnNotificationDownloadListener)
            }
            if (mOnDownloadListener != null) {
                agent.setOnDownloadListener(mOnDownloadListener)
            }
            if (mOnFailureListener != null) {
                agent.setOnFailureListener(mOnFailureListener)
            }
            if (mChecker != null) {
                agent.setChecker(mChecker)
            } else {
                if(mPostData != null){
                    agent.setChecker(UpdateChecker(mPostData!!))
                }
            }
            if (mParser != null) {
                agent.setParser(mParser)
            }
            if (mDownloader != null) {
                agent.setDownloader(mDownloader)
            }
            if (mPrompter != null) {
                agent.setPrompter(mPrompter)
            }
            agent.check()
        }

        companion object {

            private var sLastTime: Long = 0
        }
    }

}