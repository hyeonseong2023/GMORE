package com.smhrd.gmore.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.smhrd.gmore.R
import com.smhrd.gmore.utils.FBAuth

class ChatAdapter(val context: Context, val data: ArrayList<ChatVO>): RecyclerView.Adapter<ChatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
//    var template_View : View = LayoutInflater.from(parent.context).inflate(R.layout.chat_msg_temp,parent,false)
//        var VH : ChatViewHolder = ChatViewHolder(template_View)
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_msg_temp, parent, false)
        )
    }


    override fun onBindViewHolder(holder:ChatViewHolder, position: Int) {
//        var time = FBAuth.myTime(data[position].time)

        if (data[position].uid == FBAuth.getUid()) {
            holder.tvMsgMy.text = data.get(position).msg
//            holder.tvTimeMy.text = data.get(position).time
            holder.tvMsgOpp.isVisible = false
            holder.tvTimeOpp.isVisible = false
            holder.tvMsgMy.isVisible = true
            holder.tvTimeMy.isVisible = true

        } else {
            holder.tvMsgOpp.text = data.get(position).msg
//            holder.tvTimeOpp.text = data.get(position).time
            holder.tvMsgMy.isVisible = false
            holder.tvTimeMy.isVisible = false
            holder.tvMsgOpp.isVisible = true
            holder.tvTimeOpp.isVisible = true
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }
}