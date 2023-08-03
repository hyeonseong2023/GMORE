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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        chatList.add(ChatRoomVO("ssoo", "sss", "겜 친추 하실래요???", "오후14:32"))
        chatList.add(ChatRoomVO("백으", "sss", "배그 쉽지가 않네", "오후 3:29"))
        chatList.add(ChatRoomVO("lol", "sss", "재밌다ㅎㅎㅎㅎ", "오후 3:12"))

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

    fun getTime(): String {
        // Calendar 객체는 getInstance() 메소드로 객체를 생성한다
        val currentTime = Calendar.getInstance().time
        // 시간을 나타낼 형식, 어느위치의 시간을 가져올건지 설정
        // "yyyy.MM.dd HH:mm:sss"

        val time = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN).format(currentTime)

        return time
    }


    /**시간을 오후 9:13같은 형식으로 바꿔준다*/
    fun myTime(time: String): String {
        var timeResult = ""
        if (time == "")
            return ""
        if (time.substring(11, 13).toInt() > 12)
            timeResult += "오전 " + (time.substring(11, 13).toInt() - 12) + time.substring(13, 16)
        else if (time.substring(11, 13).toInt() == 12)
            timeResult += "오전 " + time.substring(11, 16)
        else if (time.substring(11, 13).toInt() > 9)
            timeResult += "오후 " + time.substring(11, 16)
        else if (time.substring(11, 13).toInt() == 0)
            timeResult += "오후 12" + time.substring(13, 16)
        else
            timeResult += "오후 " + time.substring(12, 16)
        return timeResult
    }


}