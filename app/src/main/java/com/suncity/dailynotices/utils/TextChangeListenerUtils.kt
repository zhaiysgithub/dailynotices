package com.suncity.dailynotices.utils

import android.widget.EditText
import android.widget.TextView
import com.suncity.dailynotices.callback.IEditTextChangeListener
import com.suncity.dailynotices.callback.TextWatcherHelper

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.callback
 * @ClassName:      TextChangeListener
 * @Description:    submitTextView 监听 editText 输入
 * @UpdateDate:     5/7/2019
 */
class TextChangeListenerUtils(button: TextView) {

    private var submitText: TextView = button
    private var mEditTexts: ArrayList<EditText> = arrayListOf()

    private var mEditTextChangeListener: IEditTextChangeListener? = null

    fun setChangeListener(listener: IEditTextChangeListener) {
        mEditTextChangeListener = listener
    }

    fun addAllEditText(editTexts: Array<out EditText>): TextChangeListenerUtils {
        mEditTexts.clear()
        editTexts.forEach {
            mEditTexts.add(it)
        }
        initEditListener(editTexts)
        return this
    }

    private fun initEditListener(editTexts: Array<out EditText>) {
        editTexts.forEach {
            it.addTextChangedListener(editTextWatchHelper)
        }

    }


    private val editTextWatchHelper = object : TextWatcherHelper {

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (checkAllEdit()){
                mEditTextChangeListener?.onTextChange(true)
                submitText.isEnabled = true
            }else{
                mEditTextChangeListener?.onTextChange(false)
                submitText.isEnabled = false
            }
        }

    }

    private fun checkAllEdit(): Boolean {
        mEditTexts.forEach {
            if(it.text.isNullOrEmpty()){
                return false
            }
        }
        return true
    }
}