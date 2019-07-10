package com.suncity.dailynotices.localModel

import java.io.Serializable

/**
 * @ProjectName: dailynotices
 * @Package: com.suncity.dailynotices.LocalModel
 * @ClassName: LCInfo
 * @Description: 作用描述
 * @UpdateDate: 9/7/2019
 */
class LCInfo : Serializable {

    /**
     * className : UserInfo
     * fetchWhenSave : false
     * instanceData : {"className":"UserInfo"}
     * isDataReady : false
     * objectId : 5cc5801443e78cb53fe985cf
     * operationQueue : {}
     * requestStatistic : true
     * serverData : {"className":"UserInfo"}
     * tempDataForServerSaving : {}
     */

    var isDataReady: Boolean = false
    var className: String? = null
    var operationQueue: LCOperationQueue? = null
    var tempDataForServerSaving: LCTempDataForServerSaving? = null
    var fetchWhenSave: Boolean = false
    var requestStatistic: Boolean = false
    var objectId: String? = null
    var serverData: LCServerData? = null
    var instanceData: LCInstanceData? = null
}
