package com.suncity.dailynotices.update

import java.io.File

interface IUpdateDownloader {
    fun download(agent: IDownloadAgent, url: String, temp: File)
}