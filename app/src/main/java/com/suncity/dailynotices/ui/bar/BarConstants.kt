package com.suncity.dailynotices.ui.bar

import com.suncity.dailynotices.R

class BarConstants {

    companion object {
        /**
         * android 4.4或者emui3状态栏ID识位
         */
        const val IMMERSION_ID_STATUS_BAR_VIEW = R.id.immersion_status_bar_view
        /**
         * android 4.4或者emui3导航栏ID识位
         */
        const val IMMERSION_ID_NAVIGATION_BAR_VIEW = R.id.immersion_navigation_bar_view
        /**
         * 状态栏高度标识位
         */
        const val IMMERSION_STATUS_BAR_HEIGHT = "status_bar_height"
        /**
         * 导航栏竖屏高度标识位
         */
        const val IMMERSION_NAVIGATION_BAR_HEIGHT = "navigation_bar_height"
        /**
         * 导航栏横屏高度标识位
         */
        const val IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape"
        /**
         * 导航栏宽度标识位位
         */
        const val IMMERSION_NAVIGATION_BAR_WIDTH = "navigation_bar_width"
        /**
         * MIUI导航栏显示隐藏标识位
         */
        const val IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW = "force_fsg_nav_bar"
        /**
         * EMUI导航栏显示隐藏标识位
         */
        const val IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW = "navigationbar_is_min"
        /**
         * MIUI状态栏字体黑色与白色标识位
         */
        const val IMMERSION_MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE"
        /**
         * MIUI导航栏图标黑色与白色标识位
         */
        const val IMMERSION_MIUI_NAVIGATION_BAR_DARK = "EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE"

        /**
         * 自动改变字体颜色的临界值标识位
         */
        const val IMMERSION_BOUNDARY_COLOR = -0x454546

        /**
         * 修复状态栏与布局重叠标识位，默认不修复
         */
        const val FLAG_FITS_DEFAULT = 0X00
        /**
         * 修复状态栏与布局重叠标识位，使用titleBar方法修复
         */
        const val FLAG_FITS_TITLE = 0X01
        /**
         * 修复状态栏与布局重叠标识位，使用titleBarMarginTop方法修复
         */
        const val FLAG_FITS_TITLE_MARGIN_TOP = 0X02
        /**
         * 修复状态栏与布局重叠标识位，使用StatusBarView方法修复
         */
        const val FLAG_FITS_STATUS = 0X03
        /**
         * 修复状态栏与布局重叠标识位，使用fitsSystemWindows方法修复
         */
        const val FLAG_FITS_SYSTEM_WINDOWS = 0X04

        /**
         * 系统属性
         * The constant SYSTEM_PROPERTIES.
         */
        const val SYSTEM_PROPERTIES = "android.os.SystemProperties"
        /**
         * 小米刘海
         * The constant NOTCH_XIAO_MI.
         */
        const val NOTCH_XIAO_MI = "ro.miui.notch"
        /**
         * 华为刘海
         * The constant NOTCH_HUA_WEI.
         */
        const val NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil"
        /**
         * VIVO刘海
         * The constant NOTCH_VIVO.
         */
        const val NOTCH_VIVO = "android.util.FtFeature"
        /**
         * OPPO刘海
         * The constant NOTCH_OPPO.
         */
        const val NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism"


        const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
        const val KEY_EMUI_VERSION_NAME = "ro.build.version.emui"
        const val KEY_DISPLAY = "ro.build.display.id"
    }

}
