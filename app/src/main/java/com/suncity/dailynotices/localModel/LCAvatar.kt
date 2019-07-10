package com.suncity.dailynotices.localModel

import java.io.Serializable

/**
 * @ProjectName: dailynotices
 * @Package: com.suncity.dailynotices.LocalModel
 * @ClassName: LCAvatar
 * @Description: 作用描述
 * @UpdateDate: 9/7/2019
 */
class LCAvatar : Serializable {

    /**
     * bucket : 4xblPFNl
     * dirty : false
     * metaData : {"owner":"5c989fc767f35600620c58ff","size":376603,"_name":"7G8rIVJF5cHfMQn8CjaNLfD"}
     * name : 5cd9281343e78c00698119d9
     * objectId : 5cd9281343e78c00698119d9
     * url : http://lc-4xblPFNl.cn-n1.lcfile.com/gTllzBIZjYONZEoHm6GhQeB
     */

    var bucket: String? = null
    var dirty: Boolean = false
    var metaData: LCMetaData? = null
    var name: String? = null
    var objectId: String? = null
    var url: String? = null
}
