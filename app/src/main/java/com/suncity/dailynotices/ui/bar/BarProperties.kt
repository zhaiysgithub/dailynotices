package com.suncity.dailynotices.ui.bar

/**
 * Bar相关信息
 */
class BarProperties {

    /**
     * 是否是竖屏
     */
    var isPortrait: Boolean = false
    /**
     * 是否是左横屏
     */
    var isLandscapeLeft: Boolean = false
    /**
     * 是否是右横屏
     */
    var isLandscapeRight: Boolean = false
    /**
     * 是否是刘海屏
     */
    var isNotchScreen: Boolean = false
    /**
     * 是否有导航栏
     */
    private var hasNavigationBar: Boolean = false
    /**
     * 状态栏高度，刘海屏横竖屏有可能状态栏高度不一样
     */
    var statusBarHeight: Int = 0
    /**
     * 导航栏高度
     */
    var navigationBarHeight: Int = 0
    /**
     * 导航栏宽度
     */
    var navigationBarWidth: Int = 0
    /**
     * 刘海屏高度
     */
    var notchHeight: Int = 0
    /**
     * ActionBar高度
     */
    var actionBarHeight: Int = 0

    fun hasNavigationBar(): Boolean {
        return hasNavigationBar
    }

    fun setNavigationBar(hasNavigationBar: Boolean) {
        this.hasNavigationBar = hasNavigationBar
    }
}
