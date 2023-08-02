package com.smhrd.gmore.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {


    companion object{
        lateinit var auth: FirebaseAuth



        /**uid를 가져온다*/
        fun getUid():String{
//            auth = FirebaseAuth.getInstance() // class 안에서 인스턴스 생성할 때 getInstance()붙여준다
            return "dd"
        }

        /**현재시간을 가져온다*/
        fun getTime(): String{
            // Calendar 객체는 getInstance() 메소드로 객체를 생성한다
            val currentTime = Calendar.getInstance().time
            // 시간을 나타낼 형식, 어느위치의 시간을 가져올건지 설정
            // "yyyy.MM.dd HH:mm:sss"

            val time = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN).format(currentTime)

            return time
        }

        /**시간을 오후 9:13같은 형식으로 바꿔준다*/
        fun myTime(time: String): String{
            var timeResult = ""
            if(time=="")
                return ""
            if (time.substring(11,13).toInt()>12)
                timeResult += "오후 " + (time.substring(11,13).toInt() - 12) + time.substring(13,16)
            else if (time.substring(11,13).toInt()==12)
                timeResult += "오후 " + time.substring(11,16)
            else if (time.substring(11,13).toInt()>9)
                timeResult += "오전 " + time.substring(11,16)
            else if (time.substring(11,13).toInt()==0)
                timeResult += "오전 12" + time.substring(13,16)
            else
                timeResult += "오전 " + time.substring(12,16)
            return timeResult
        }

        /**사용자uid와 targetUid 비교
         * @return Boolean*/
        fun checkUid(targetUid: String):Boolean {
            val userUid = FBAuth.getUid()
            Log.d("check"," 사용자uid: $userUid, targetUid: $targetUid")
            return targetUid==userUid
        }

    }
}