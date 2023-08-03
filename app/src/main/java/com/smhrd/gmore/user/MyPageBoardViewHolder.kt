package com.smhrd.gmore.user

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.R
import com.smhrd.gmore.board.CategoryClickEvent

class MyPageBoardViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    var imgMyBoard : ImageView
    var tvMyTitle : TextView
    var tvMyDate: TextView
    var clickidx = 0
    init {
        imgMyBoard = itemView.findViewById(R.id.imgMyBoard)
        tvMyTitle = itemView.findViewById(R.id.tvTitleMyBoard)
        tvMyDate = itemView.findViewById(R.id.tvDateMyBoard)



    }


}