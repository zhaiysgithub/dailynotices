package com.suncity.dailynotices.dialog

class DialogSettings {

    companion object {
        const val THEME_LIGHT = 0
        const val THEME_DARK = 1

        const val TYPE_MATERIAL = 0
        const val TYPE_KONGZUE = 1
        const val TYPE_IOS = 2

        const val STYLE_MATERIAL = 0
        const val STYLE_KZ = 1
        const val STYLE_IOS = 2

        //是否打印日志
        var DEBUGMODE = true

        //决定等待框、提示框以及iOS风格的对话框是否启用模糊背景
        var use_blur = true

        //决定等待框、提示框以及iOS风格的对话框的模糊背景透明度（50-255）
        var blur_alpha = 200

        //决定对话框的默认样式，请使用 STYLE_MATERIAL、STYLE_KZ、STYLE_IOS 赋值
        var style = 0

        //决定对话框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
        var dialog_theme = 0

        //决定对话框的默认背景色
        var dialog_background_color = -1

        //决定提示框的模式（亮色和暗色两种），请使用 THEME_LIGHT、THEME_DARK 赋值
        var tip_theme = THEME_LIGHT

        /*
         *  文字大小设定
         *  注意，此值必须大于0才生效，否则使用默认值。另外，我们使用的是dp单位，非sp单位，若有特殊需要请自行转换
         *  另外，暂时不支持Material风格对话框设定字体大小
         */

        //决定对话框标题文字样式
        var dialogTitleTextInfo = TextInfo()

        //决定对话框内容文字样式
        var dialogContentTextInfo = TextInfo()

        //决定对话框按钮文字样式
        var dialogButtonTextInfo = TextInfo()

        //决定对话框积极按钮（一般为确定按钮）文字样式，若未设置此样式则会使用 dialogButtonTextInfo 代替
        var dialogOkButtonTextInfo: TextInfo? = null

        //决定提示框文本样式
        var tipTextInfo = TextInfo()

        //决定菜单文字样式
        var menuTextInfo = TextInfo()

        //决定 Notification 默认文字样式信息
        var notificationTextInfo = TextInfo()

        //决定输入框输入文本字样大小（单位：dp），当值<=0时使用默认大小
        var dialog_input_text_size = 0

        //决定对话框组件默认是否可点击遮罩区域关闭
        var dialog_cancelable_default = false

        //此方法用于关闭所有已加载的 Dialog，防止 WindowLeaked 错误，建议将它加在你的 Activity 的 onDestroy() 里调用
        fun unloadAllDialog() {
            BaseDialog.unloadAllDialog()
        }

    }
}
