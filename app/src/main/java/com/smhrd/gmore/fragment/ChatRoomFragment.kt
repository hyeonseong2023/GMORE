package com.smhrd.gmore.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import com.smhrd.gmore.R
import com.smhrd.gmore.chat.ChatActivity
import com.smhrd.gmore.chat.ChatRoomAdapter
import com.smhrd.gmore.chat.ChatRoomVO
import com.smhrd.gmore.chat.OnItemClickListener
import com.smhrd.gmore.utils.FBAuth
import com.smhrd.gmore.utils.FBDatabase

class ChatRoomFragment(var spf : String): Fragment() {

    lateinit var adapter: ChatRoomAdapter
    val chatList = ArrayList<ChatRoomVO>()
    val keyData = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_chat_rooms, container, false)
        Log.d("ss",spf)
        val rvChatRoom = view.findViewById<RecyclerView>(R.id.rvChatRoom)
        chatList.add(ChatRoomVO("thgml", spf, "gg", "gg"))
        chatList.add(ChatRoomVO("sohee", "thgmldi", "gg", "gg"))
        chatList.add(ChatRoomVO("thgml", "thgmldi", "gg", "gg"))

//        getChatRoomData()

        adapter = ChatRoomAdapter(requireActivity(), chatList, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                var intent = Intent(requireActivity(), ChatActivity::class.java)

                startActivity(intent)

            }
        })
//        adapter.setOnItemClickListener(object : ChatRoomAdapter.onItemClickListener{
//            override fun onItemClick(view: View, position: Int) {
//                val intent = Intent(requireContext(),ChatActivity::class.java)
//                var oppUid = chatList[position].uidOne
//                if (oppUid == spf)
//                    oppUid = chatList[position].uidTwo
//                intent.putExtra("oppUid", oppUid)
//                intent.putExtra("chatroomKey", keyData[position])
//
//                startActivity(intent)
//            }
//        })
        rvChatRoom.layoutManager = LinearLayoutManager(requireActivity())
        rvChatRoom.adapter = adapter


        return view
    }
//    fun getChatRoomData(){
//        val postListener = object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                chatList.clear()
//                // firebase에서 snapshot으로 데이터를 받아온 경우
//                for(model in snapshot.children) {
//                    val item = model.getValue(ChatRoomVO::class.java) as ChatRoomVO
//                    if(item.uidOne == FBAuth.getUid() || item.uidTwo == FBAuth.getUid()) {
//                        chatList.add(item)
//                        keyData.add(model.key.toString())
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // 오류가 발생했을 때 실행되는 함수
//            }
//        }
//        FBDatabase.database.getReference("chatroom").addValueEventListener(postListener)
//    }

}