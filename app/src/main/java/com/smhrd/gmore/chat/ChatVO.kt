package com.smhrd.gmore.chat

data class ChatVO(var msg: String,var uid: String,var time: String, )

{
    constructor() : this("", "", "")
}