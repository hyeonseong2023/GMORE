package com.smhrd.gmore.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.R

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvMsgOpp: TextView
    val tvTimeOpp: TextView
    val tvMsgMy: TextView
    val tvTimeMy: TextView
    init {
        tvMsgOpp = itemView.findViewById(R.id.tvMsgOpp)
        tvTimeOpp = itemView.findViewById(R.id.tvTimeOpp)
        tvMsgMy = itemView.findViewById(R.id.tvMsgMy)
        tvTimeMy = itemView.findViewById(R.id.tvTimeMy)
    }
}