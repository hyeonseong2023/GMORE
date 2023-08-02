package com.smhrd.gmore.chat

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ChildEvent(var data: ArrayList<ChatVO>, var adapter: ChatAdapter) : ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        // 데이터 추가 감지

        //snapshot => firebase database 에 저장된 데이터
        //json 구조로 응답함 => ChatVO 형태로 변환
        var temp : ChatVO? = snapshot.getValue(ChatVO::class.java)
        data.add(temp!!)
        Log.d("data",data.toString())
        adapter.notifyDataSetChanged()

    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        // 데이터 변경 감지
        // ArrayList에 추가된 데이터 추가하고 Adapter 새로고침

    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        // 제거
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        // 옮겨짐
    }

    override fun onCancelled(error: DatabaseError) {
        // 뭔가 문제가 발생한걸 감지!
    }
}