package com.suncity.dailynotices.update

interface IUpdateChecker {
    fun check(agent: ICheckAgent, url: String)
}