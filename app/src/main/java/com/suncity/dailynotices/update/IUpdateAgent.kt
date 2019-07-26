package com.suncity.dailynotices.update

interface IUpdateAgent {

    fun getInfo():UpdateInfo

    fun update()

    fun ignore()
}