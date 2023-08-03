package com.smhrd.gmore.chat

import android.content.Context
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    lateinit var rvChat : RecyclerView
    lateinit var btnChatSend : Button
    lateinit var etChatMsg: EditText
    lateinit var tvChatNick:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        var spf = getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        var loginedID = spf.getString("loginedId", "-")


        rvChat = findViewById(R.id.rvChat)
        btnChatSend = findViewById(R.id.btnChatSend)
        etChatMsg = findViewById(R.id.etChatMsg)
        tvChatNick = findViewById(R.id.tvChatNick)


        val database = Firebase.database
        val myRef = database.getReference("message")

        val data = ArrayList<ChatVO>()

        data.add(ChatVO("안녕하세요", "ddd",myTime(getTime())))
        data.add(ChatVO("안녕하", "ddd",myTime(getTime())))

        var adapter = ChatAdapter(applicationContext, data,loginedID.toString())
        rvChat.layoutManager = LinearLayoutManager(applicationContext)// 목록형
        rvChat.adapter = adapter
        tvChatNick.text = loginedID


        btnChatSend.setOnClickListener {
            myRef.push().setValue(ChatVO(etChatMsg.text.toString(),loginedID.toString(), myTime(getTime())))

            rvChat.smoothScrollToPosition(data.size-1)
            etChatMsg.text.clear()
        }

        myRef.addChildEventListener(ChildEvent(data,adapter))
    }



    // 아이디 비교하기
    /**사용자uid와 targetUid 비교
     * @return Boolean*/


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
    fun getUid():String{
//            auth = FirebaseAuth.getInstance() // class 안에서 인스턴스 생성할 때 getInstance()붙여준다
        return "dd"
    }



}




