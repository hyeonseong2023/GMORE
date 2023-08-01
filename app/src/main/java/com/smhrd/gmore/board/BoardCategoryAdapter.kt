package com.smhrd.gmore.board

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.R
import com.smhrd.gmore.vo.BoardCategoryVO

class BoardCategoryAdapter(var context: Context, var datas:ArrayList<BoardCategoryVO>) :
    RecyclerView.Adapter<BoardCategoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardCategoryViewHolder {
        return BoardCategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.board_category_item,parent,false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: BoardCategoryViewHolder, position: Int) {
        val board = datas[position]

        holder.categoryTitle.text = board.categoryTitle
        holder.categoryNick.text = board.categoryNick
        holder.categoryDate.text = board.categoryDate
        holder.categoryLikeCnt.text = board.categoryLikeCnt.toString()
    }
}