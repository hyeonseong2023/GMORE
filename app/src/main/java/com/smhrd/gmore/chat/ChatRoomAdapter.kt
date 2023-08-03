package com.smhrd.gmore.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.bumptech.glide.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.smhrd.gmore.R
import com.smhrd.gmore.utils.FBAuth
import com.smhrd.gmore.utils.FBDatabase
import com.smhrd.gmore.vo.MemberVO

class ChatRoomAdapter(val context: Context, val chatroomList: ArrayList<ChatRoomVO>, private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {


//
//    lateinit var monItemClickListener: onItemClickListener
//
//    fun setOnItemClickListener(onItemClickListener: onItemClickListener) {
//        monItemClickListener = onItemClickListener
//    }

    inner class ViewHolder(itemView: View, private val onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val imgCRT: ImageView
        val tvCRTNick: TextView
        val tvCRTLastMsg: TextView
        val tvCRTLastMsgTime: TextView
        init {
            imgCRT = itemView.findViewById(R.id.imgCRT)
            tvCRTNick = itemView.findViewById(R.id.tvCRTNick)
            tvCRTLastMsg = itemView.findViewById(R.id.tvCRTLastMsg)
            tvCRTLastMsgTime = itemView.findViewById(R.id.tvCRTLastMsgTime)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_room_temp, parent, false)
        return ViewHolder(view, onItemClickListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var oppUid = chatroomList[position].uidOne
//        if (chatroomList[position].uidOne == FBAuth.getUid())
//            oppUid = chatroomList[position].uidTwo

//        getUserNick(oppUid, holder.tvCRTNick, holder.imgCRT)

        holder.tvCRTLastMsg.text = chatroomList[position].lastChatMsg
        holder.tvCRTNick.text = chatroomList[position].uidOne
        holder.tvCRTLastMsgTime.text = chatroomList[position].lastChatTime
//        holder.tvCRTLastMsgTime.text = FBAuth.myTime(chatroomList[position].lastChatTime)
    }

    override fun getItemCount(): Int {
        return chatroomList.size
    }

//    fun getImageData(key : String, view: ImageView){
//        val storageReference = Firebase.storage.reference.child("$key.png")
//
//        storageReference.downloadUrl.addOnCompleteListener { task->
//            if (task.isSuccessful){
//                Glide.with(context)
//                    .load(task.result)
//                    .into(view)
//            }
//        }
//    }

//    fun getUserNick(uid: String, tv: TextView, iv: ImageView){
//        FBDatabase.database.getReference("member").child(uid).get().addOnSuccessListener {
//            val item = it.getValue(MemberVO::class.java) as MemberVO
//            tv.setText(item.nick)
//            getImageData(item.id, iv)
//
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
//    }
}