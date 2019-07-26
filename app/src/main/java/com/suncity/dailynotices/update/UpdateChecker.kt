package com.suncity.dailynotices.update

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UpdateChecker : IUpdateChecker {

    private val mPostData: ByteArray?

    constructor() {
        mPostData = null
    }

    constructor(data: ByteArray) {
        mPostData = data
    }

    override fun check(agent: ICheckAgent, url: String) {
        var connection: HttpURLConnection? = null
        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("Accept", "application/json")

            if (mPostData == null) {
                connection.requestMethod = "GET"
                connection.connect()
            } else {
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.instanceFollowRedirects = false
                connection.useCaches = false
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                connection.setRequestProperty("Content-Length", Integer.toString(mPostData.size))
                connection.outputStream.write(mPostData)
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                agent.setInfo(UpdateUtil.readString(connection.inputStream))
            } else {
                agent.setError(UpdateError(UpdateError.CHECK_HTTP_STATUS, "" + connection.responseCode))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            agent.setError(UpdateError(UpdateError.CHECK_NETWORK_IO))
        } finally {
            connection?.disconnect()
        }
    }
}