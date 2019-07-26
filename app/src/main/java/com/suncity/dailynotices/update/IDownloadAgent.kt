package com.suncity.dailynotices.update

interface IDownloadAgent : OnDownloadListener {
    val info: UpdateInfo

    fun setError(error: UpdateError)
}