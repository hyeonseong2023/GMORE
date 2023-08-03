package com.smhrd.gmore.board

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.R

class BoardCategoryViewHolder(itemView: View, val categoryClickEvent: CategoryClickEvent?) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var categoryTitle:TextView
    var categoryNick:TextView
    var categoryDate:TextView
    var categoryLikeCnt:TextView

    init {
        categoryTitle = itemView.findViewById(R.id.tvCategoryTitle)
        categoryNick = itemView.findViewById(R.id.tvCategoryNick)
        categoryDate = itemView.findViewById(R.id.tvCategoryDate)
        categoryLikeCnt = itemView.findViewById(R.id.tvCategoryLikeCnt)
        itemView.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION && view != null) {
            categoryClickEvent?.onItemClick(position)
        }
    }

}