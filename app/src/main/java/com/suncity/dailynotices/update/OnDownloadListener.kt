package com.suncity.dailynotices.update

interface OnDownloadListener {

    fun onStart()

    fun onProgress(progress: Int)

    fun onFinish()
}