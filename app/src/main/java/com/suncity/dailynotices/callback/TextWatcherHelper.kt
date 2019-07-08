package com.suncity.dailynotices.callback

import android.text.Editable
import android.text.TextWatcher

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      TextWatcherHelper
 * @Description:    TextWatcher 监听帮助类
 * @UpdateDate:     5/7/2019
 */
interface TextWatcherHelper : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}