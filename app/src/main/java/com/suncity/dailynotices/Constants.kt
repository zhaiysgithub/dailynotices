package com.suncity.dailynotices


/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices
 * @ClassName:      Constants
 * @Description:    项目中的实体类
 */

class Constants{

    companion object {

        val SDK_INT = android.os.Build.VERSION.SDK_INT
        const val SP_NAME = "dailynotices"
        const val SP_KEY_USER = "spkeyuser"
        const val SP_KEY_USERINFO = "spkeyuserinfo"
        const val SP_KEY_FIRE = "spkeyfire"

        const val ISLOGINED = "islogined"
        const val USEROBJECTID = "userObjectId"
        const val USERNAME = "username"
        const val USERPHONENUM = "userphonenum"
        const val USERAVATAR = "userAvatar"
        const val USERISAUTONYM = "userisautonym"
        const val USERAUTHNAME = "userauthname"
        const val USERAUTHCERTFICATENUM = "userauthcertficatenum"
        const val USERAUTHCERTFICATEPIC = "userauthcertficatepic"


        val needClearTableValue = arrayOf(SP_KEY_USER,SP_KEY_USERINFO,SP_KEY_FIRE)

    }
}