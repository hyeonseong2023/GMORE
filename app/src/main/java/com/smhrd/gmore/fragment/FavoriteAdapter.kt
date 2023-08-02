package com.smhrd.gmore.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // 레이아웃 바인딩
    }
}

