package com.suncity.dailynotices.dialog

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.dialog
 * @ClassName:      BaseDialog
 * @Description:     dialog 的基类
 * @UpdateDate:     5/7/2019
 */
abstract class BaseDialog {

    companion object {

        var dialogList: ArrayList<BaseDialog> = arrayListOf()
    }
}