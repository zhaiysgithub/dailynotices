package com.suncity.dailynotices.ui.chat

import cn.leancloud.chatkit.LCChatKitUser
import cn.leancloud.chatkit.LCChatProfileProvider
import cn.leancloud.chatkit.LCChatProfilesCallBack
import com.avos.avoscloud.*
import com.suncity.dailynotices.TableConstants
import com.suncity.dailynotices.utils.LogUtils
import java.util.ArrayList

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.chat
 * @ClassName:      CustomUserProvider
 * @Description:    实现自定义用户体系
 */
class CustomUserProvider private constructor() : LCChatProfileProvider {

    companion object {
        val instance = SingletonHolder.holder

        private var chatUserList: MutableList<LCChatKitUser> = mutableListOf()

    }

    private object SingletonHolder {
        val holder = CustomUserProvider()
    }

    init {
        getChatUsers()
    }

    private fun getChatUsers(){
        val userQuery = AVQuery<AVUser>(TableConstants.TABLE_USER)
        userQuery.findInBackground(object : FindCallback<AVUser>() {
            override fun done(avObjects: MutableList<AVUser>?, avException: AVException?) {
                chatUserList.clear()
                avObjects?.forEach {
                    val objectId = it.objectId
                    val userName = it.username
                    val avataruRL = it.getAVFile<AVFile>("avatar")?.url ?: ""
                    val chatUser = LCChatKitUser(objectId, userName, avataruRL)
                    chatUserList.add(chatUser)
                }
            }
        })
    }


    override fun fetchProfiles(userIdList: MutableList<String>, profilesCallBack: LCChatProfilesCallBack) {
        val userList = ArrayList<LCChatKitUser>()
        userIdList.forEach {userId ->
            val chatUser = chatUserList.find { it.userId == userId }
            if(chatUser != null){
                userList.add(chatUser)
            }
        }
        profilesCallBack.done(userList,null)
    }

    override fun getAllUsers(): MutableList<LCChatKitUser> {
        return chatUserList
    }
}