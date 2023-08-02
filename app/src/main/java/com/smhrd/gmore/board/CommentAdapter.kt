package com.smhrd.gmore.board


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.R

class CommentAdapter(private val commentList: List<CommentVO>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.apply {
            tvCommentNickname.text = comment.nickname
            tvCommentDate.text = comment.date_created
            tvCommentContent.text = comment.content
        }
    }

    override fun getItemCount() = commentList.size

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCommentNickname: TextView = itemView.findViewById(R.id.tvCommentNickname)
        val tvCommentDate: TextView = itemView.findViewById(R.id.tvCommentDate)
        val tvCommentContent: TextView = itemView.findViewById(R.id.tvCommentContent)
    }
}
