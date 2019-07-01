package com.suncity.dailynotices.ui.loading

/**
 * 用于设置全局的loading样式
 */

class DialogStyleManager(open: Boolean, repeatTime: Int
                         , speed: LoadingDialog.Speed, contentSize: Int
                         , textSize: Int, showTime: Long
                         , interceptBack: Boolean, loadText: String
                         , successText: String, failedText: String) {

    /**
     * 是否开启绘制
     */
    var isOpenAnim = open
        private set

    /**
     * 重绘次数
     */
    var repeatTime: Int = repeatTime
        private set

    var speed: LoadingDialog.Speed = speed
        private set

    /**
     * 反馈的尺寸，单位px
     */
    var contentSize = contentSize
        private set

    /**
     * 文字的尺寸，单位px
     */
    var textSize = textSize
        private set

    /**
     * loading的反馈展示的时间，单位ms
     */
    var showTime: Long = showTime
        private set

    var isInterceptBack = interceptBack
        private set

    var loadText = loadText
        private set

    var successText = successText
        private set

    var failedText = failedText
        private set

    private var loadStyle = LoadingDialog.STYLE_RING


    fun getLoadStyle(): Int {
        return loadStyle
    }

    /**
     * 是否开启动态绘制
     *
     * @param openAnim true开启，false关闭
     * @return this
     */
    fun Anim(openAnim: Boolean): DialogStyleManager {
        this.isOpenAnim = openAnim
        return this
    }

    /**
     * 重复次数
     *
     * @param times 次数
     * @return this
     */
    fun repeatTime(times: Int): DialogStyleManager {
        this.repeatTime = times
        return this
    }

    fun speed(s: LoadingDialog.Speed): DialogStyleManager {
        this.speed = s
        return this
    }

    /**
     * 设置loading的大小
     *
     * @param size 尺寸，单位px
     * @return this
     */
    fun contentSize(size: Int): DialogStyleManager {
        this.contentSize = size
        return this
    }

    /**
     * 设置loading 文字的大小
     *
     * @param size 尺寸，单位px
     * @return this
     */
    fun textSize(size: Int): DialogStyleManager {
        this.textSize = size
        return this
    }

    /**
     * 设置展示的事件，如果开启绘制则从绘制完毕开始计算
     *
     * @param showTime 事件
     * @return this
     */
    fun showTime(showTime: Long): DialogStyleManager {
        this.showTime = showTime
        return this
    }

    /**
     * 设置是否拦截back，默认拦截
     *
     * @param interceptBack true拦截，false不拦截
     * @return this
     */
    fun intercept(interceptBack: Boolean): DialogStyleManager {
        this.isInterceptBack = interceptBack
        return this
    }

    /**
     * 设置loading时的文字
     *
     * @param text 文字
     * @return this
     */
    fun loadText(text: String): DialogStyleManager {
        this.loadText = text
        return this
    }

    /**
     * 设置success时的文字
     *
     * @param text 文字
     * @return this
     */
    fun successText(text: String): DialogStyleManager {
        this.successText = text
        return this
    }

    /**
     * 设置failed时的文字
     *
     * @param text 文字
     * @return this
     */
    fun failedText(text: String): DialogStyleManager {
        this.failedText = text
        return this
    }

}
