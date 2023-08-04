package com.smhrd.gmore.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.smhrd.gmore.board.BoardDetailActivity
import com.smhrd.gmore.board.BoardDetailVO

class MyPageBoardAdapter (var context: Context, var template: Int, var data:ArrayList<BoardDetailVO>):
    RecyclerView.Adapter<MyPageBoardViewHolder>() {

   var clickidx = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageBoardViewHolder {
        var template_View: View = LayoutInflater.from(context).inflate(template, parent, false)

        var VH: MyPageBoardViewHolder = MyPageBoardViewHolder(template_View)


        return VH.apply {
           tvMyTitle.setOnClickListener{
               clickidx = data.get(position).board_id!!
               Log.d("클릭한 idx",clickidx.toString())
               // Intent 통해 해당 게시글 페이지로 넘기기
//               var intent = Intent(context,BoardDetailActivity::class.java)
//               intent.putExtra("selected_board_id",clickidx)
//               context.startActivity(intent)

               val intent = Intent(context, BoardDetailActivity::class.java)
               intent.putExtra("selected_board_id", clickidx)
               intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
               context.startActivity(intent)

           }
        }
    }



    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyPageBoardViewHolder, position: Int) {


        val imageBytes = Base64.decode(data.get(position).image_url, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)





        holder.imgMyBoard.setImageBitmap(image)
        holder.tvMyTitle.text = data.get(position).title
        holder.tvMyDate.text = data.get(position).date_created!!.substring(0 until 10)

    }

}
