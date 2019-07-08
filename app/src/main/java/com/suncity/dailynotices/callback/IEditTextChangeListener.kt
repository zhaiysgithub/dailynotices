package com.suncity.dailynotices.callback

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      IEditTextChangeListener
 * @Description:     监听 EditText的文本输入内容接口
 * @UpdateDate:     5/7/2019
 */
interface IEditTextChangeListener {

    /**
     * 是否 editText 内都有内容了
     */
    fun onTextChange(hasContent: Boolean)
}