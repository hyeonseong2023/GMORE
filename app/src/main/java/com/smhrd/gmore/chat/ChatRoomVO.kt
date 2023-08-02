package com.smhrd.gmore.chat

data class ChatRoomVO(var uidOne: String,var uidTwo: String,var lastChatMsg: String,var lastChatTime: String) {
    constructor(): this("", "", "", "")
}
