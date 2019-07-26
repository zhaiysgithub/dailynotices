package com.suncity.dailynotices.update

interface IUpdateParser {
    @Throws(Exception::class)
    fun parse(source: String): UpdateInfo
}