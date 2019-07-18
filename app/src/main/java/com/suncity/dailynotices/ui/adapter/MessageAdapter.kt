package com.suncity.dailynotices.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.leancloud.chatkit.LCChatMessageInterface
import cn.leancloud.chatkit.utils.LCIMConversationUtils
import com.alibaba.fastjson.JSON
import com.avos.avoscloud.AVCallback
import com.avos.avoscloud.AVException
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.facebook.drawee.view.SimpleDraweeView
import com.suncity.dailynotices.R
import com.suncity.dailynotices.ui.views.recyclerview.adapter.HAFViewHolder
import com.suncity.dailynotices.ui.views.recyclerview.adapter.RecyclerArrayAdapter
import com.suncity.dailynotices.utils.Config
import com.suncity.dailynotices.utils.LogUtils
import com.suncity.dailynotices.utils.StringUtils
import com.suncity.dailynotices.utils.imPostDate
import java.lang.Exception

/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.adapter
 * @ClassName:      MessageAdapter
 * @Description:     作用描述
 * @UpdateDate:     17/7/2019
 */
class MessageAdapter(context: Context) : RecyclerArrayAdapter<AVIMConversation>(context) {

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        return MessageViewHolder(parent, R.layout.adapter_item_message)
    }


    class MessageViewHolder(parent: ViewGroup, resLayoutId: Int) :
        HAFViewHolder<AVIMConversation>(parent, resLayoutId) {

        private var avatar: SimpleDraweeView? = null
        private var username: TextView? = null
        private var lastmsg: TextView? = null
        private var date: TextView? = null
        private var msgView: TextView? = null

        init {
            avatar = itemView.findViewById(R.id.avatar_message)
            username = itemView.findViewById(R.id.username_message)
            lastmsg = itemView.findViewById(R.id.lastmsg_message)
            date = itemView.findViewById(R.id.date_message)
            msgView = itemView.findViewById(R.id.tv_msg_count)
        }

        override fun setData(data: AVIMConversation) {
            val createdAt = data.createdAt
            LogUtils.e("createAt = $createdAt")
            if (null == createdAt) {
                data.fetchInfoInBackground(object : AVIMConversationCallback() {
                    override fun done(e: AVIMException?) {
                        if (e == null) {
                            updateName(data)
                            updateIcon(data)
                        } else {
                            LogUtils.e("MessageViewHolder - pos = $adapterPosition,e = $e")
                        }
                    }

                })
            } else {
                updateName(data)
                updateIcon(data)
            }

            updateUnreadCount(data)
            updateLastMessage(data.lastMessage)
        }

        private fun updateLastMessage(lastMessage: AVIMMessage?) {
            if (lastMessage != null) {
                lastmsg?.text = getMessageeShorthand(lastMessage)
                val timeStamp = lastMessage.timestamp
                val lastMsgDate = timeStamp.imPostDate()
                date?.text = lastMsgDate
            }else{
                lastmsg?.text = Config.getString(R.string.lcim_message_shorthand_unknown)
            }
        }

        private fun getMessageeShorthand(message: AVIMMessage): CharSequence? {

            if (message is AVIMTypedMessage) {
                val type = AVIMReservedMessageType.getAVIMReservedMessageType(
                    message.messageType
                )
                when (type) {
                    AVIMReservedMessageType.TextMessageType -> return (message as AVIMTextMessage).text
                    AVIMReservedMessageType.ImageMessageType -> return Config.getString(R.string.lcim_message_shorthand_image)
                    AVIMReservedMessageType.LocationMessageType -> return Config.getString(R.string.lcim_message_shorthand_location)
                    AVIMReservedMessageType.AudioMessageType -> return Config.getString(R.string.lcim_message_shorthand_audio)
                    else -> {
                        var shortHand: CharSequence = ""
                        if (message is LCChatMessageInterface) {
                            val messageInterface = message as LCChatMessageInterface
                            shortHand = messageInterface.shorthand
                        }
                        if (StringUtils.isEmptyOrNull(shortHand?.toString())) {
                            shortHand = Config.getString(R.string.lcim_message_shorthand_unknown)
                        }
                        return shortHand
                    }
                }
            } else {
                return try {
                    val content = message.content
                    val jsonObject = JSON.parseObject(content)
                    val lastMsgStr = jsonObject.getString("_lctext")
                    return lastMsgStr ?: Config.getString(R.string.lcim_message_shorthand_unknown)
                }catch (e : Exception){
                    Config.getString(R.string.lcim_message_shorthand_unknown)
                }
            }
        }

        private fun updateUnreadCount(data: AVIMConversation) {
            val num = data.unreadMessagesCount
            msgView?.text = "$num"
            msgView?.visibility = if (num > 0) View.VISIBLE else View.GONE
        }

        private fun updateName(data: AVIMConversation) {
            LCIMConversationUtils.getConversationName(data, object : AVCallback<String>() {

                override fun internalDone0(t: String?, avException: AVException?) {
                    if (avException != null) {
                        LogUtils.e("updateName - pos = $adapterPosition,e = $avException")
                    } else {
                        username?.text = t
                    }
                }

            })
        }

        private fun updateIcon(data: AVIMConversation) {
            if (data.isTransient || data.members.size > 2) {
                avatar?.setActualImageResource(R.drawable.ico_im_group)
            } else {
                LCIMConversationUtils.getConversationPeerIcon(data, object : AVCallback<String>() {

                    override fun internalDone0(t: String?, avException: AVException?) {
                        if (avException != null) {
                            LogUtils.e("updateIcon - pos = $adapterPosition,e = $avException")
                        } else {
                            avatar?.setImageURI(t)
                        }
                    }

                })
            }
        }
    }
}