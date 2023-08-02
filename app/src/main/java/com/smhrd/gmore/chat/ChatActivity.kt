package com.smhrd.gmore.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.smhrd.gmore.R
import com.smhrd.gmore.utils.FBAuth

class ChatActivity : AppCompatActivity() {
    lateinit var rvChat : RecyclerView
    lateinit var btnChatSend : Button
    lateinit var etChatMsg: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        rvChat = findViewById(R.id.rvChat)
        btnChatSend = findViewById(R.id.btnChatSend)
        etChatMsg = findViewById(R.id.etChatMsg)


        val database = Firebase.database
        val myRef = database.getReference("message")

//        val data = ArrayList<ChatVO>()
        val data = ArrayList<ChatVO>()

        data.add(ChatVO("안녕하", "dd","시간1"))
        data.add(ChatVO("안녕하", "dd","시간1"))

        var adapter = ChatAdapter(applicationContext, data)
        rvChat.layoutManager = LinearLayoutManager(applicationContext)// 목록형
        rvChat.adapter = adapter


        btnChatSend.setOnClickListener {
            myRef.push().setValue(ChatVO(etChatMsg.text.toString(),"dd","오후3:48"))

            rvChat.smoothScrollToPosition(data.size-1)
            etChatMsg.text.clear()
        }

        myRef.addChildEventListener(ChildEvent(data,adapter))
    }
}