package com.suncity.dailynotices.utils


object PreventRepeatedUtils {

    private var lastClickTime: Long = 0
    private val DIFF: Long = 1000
    private var lastButtonId: Int = -1

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    fun isFastDoubleClick(): Boolean {
        return isFastDoubleClick(-1, DIFF)
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    fun isFastDoubleClick(buttonId: Int): Boolean {
        return isFastDoubleClick(buttonId, DIFF)
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @author ZYS
     * @date 2019/06/24
     * @version PROD > 1.3.77
     * @desc 优化代码执行效率
     * @param diff
     * @return
     */
    fun isFastDoubleClick(buttonId: Int, diff: Long): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        val isDoubleClick = (Math.abs(timeD) < diff)
        if (isDoubleClick && lastButtonId == buttonId && lastClickTime > 0) {
            return true
        }
        lastClickTime = time
        lastButtonId = buttonId
        return false
    }
}