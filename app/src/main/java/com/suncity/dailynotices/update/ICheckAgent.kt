package com.suncity.dailynotices.update

interface ICheckAgent {
    fun setInfo(info: String)

    fun setError(error: UpdateError)
}