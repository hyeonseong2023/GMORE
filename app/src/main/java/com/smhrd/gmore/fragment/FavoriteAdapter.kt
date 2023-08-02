package com.smhrd.gmore.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.R
import com.smhrd.gmore.fragment.Fragment3VO

class FavoriteAdapter(private var favorites: List<Fragment3VO>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    // ViewHolder 및 다른 관련 메소드를 구현

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favorites[position]
        // 데이터 바인딩
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun updateDataSet(newFavorites: List<Fragment3VO>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryBoardId = itemView.findViewById<TextView>(R.id.tv_category_board_id)
        private val tvFavoriteTitle = itemView.findViewById<TextView>(R.id.tv_favorite_title)
        private val tvDateCreated = itemView.findViewById<TextView>(R.id.tv_date_created)
        private val tvCategoryNick = itemView.findViewById<TextView>(R.id.tv_category_nick)
        private val tvCategoryLikeCnt = itemView.findViewById<TextView>(R.id.tv_category_like_cnt)

        fun bind(favorite: Fragment3VO) {
            tvCategoryBoardId.text = "Board ID: ${favorite.categoryBoardId}"
            tvFavoriteTitle.text = "Title: ${favorite.categoryTitle}"
            tvDateCreated.text = "Date: ${favorite.categoryDate}"
            tvCategoryNick.text = "Nickname: ${favorite.categoryNick}"
            tvCategoryLikeCnt.text = "Like Count: ${favorite.categoryLikeCnt}"
            // 여기에 필요한 다른 데이터 바인딩이 있다면 추가해주세요
        }
    }


}


