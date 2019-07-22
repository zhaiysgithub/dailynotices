package com.suncity.dailynotices.model

import java.io.Serializable

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.model
 * @ClassName:      SortBean
 * @Description:     作用描述
 * @UpdateDate:     22/7/2019
 */

class SortBean : Serializable {

    var name: String? = null
    var title: String? = null
    var data: ArrayList<Data>? = null

    class Data : Serializable {
        var name: String? = null
        var imgsrc: String? = null
        var cacode: String? = null
        var itemdata: ArrayList<ItemData>? = null


    }

    class ItemData : Serializable {
        var name: String? = null
        var imgsrc: String? = null
        var cacode: String? = null

    }

}