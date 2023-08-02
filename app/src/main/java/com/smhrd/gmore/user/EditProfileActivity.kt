package com.smhrd.gmore.user
// 회원정보 수정(닉네임 / PW) 및 탈퇴 페이지
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.smhrd.gmore.R

class EditProfileActivity : AppCompatActivity() {

    lateinit var etNickEditProfile: EditText
    lateinit var etPwEditProfile: EditText
    lateinit var etPwEditProfile2: EditText
    lateinit var tvQuitEditProfile: TextView
    lateinit var btnNickCheckEditPage : Button
   lateinit var btnSaveEditProfile : Button

   lateinit var reqQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etNickEditProfile = findViewById(R.id.etNickEditProfile)
        etPwEditProfile = findViewById(R.id.etPwEditProfile)
        etPwEditProfile2 = findViewById(R.id.etPwEditProfile2)
        tvQuitEditProfile = findViewById(R.id.tvQuitEditPrpfile)
        btnNickCheckEditPage = findViewById(R.id.btnNickCheckEditPage)
        btnSaveEditProfile = findViewById(R.id.btnSaveEditProfile)


        reqQueue = Volley.newRequestQueue(this@EditProfileActivity)


        // spf 이용해 로그인 유저 데이터 받아오기
        val spf = getSharedPreferences(
            //  -- fragment2 에서 생성한 것과 KEY 값 맞춰주기
            "userSPF",
            Context.MODE_PRIVATE
        )

        // spf 에서 user id 가져와 textView에 출력
        val userId = spf.getString("userId", "")
        Log.d("spf 값??", userId.toString())
        etNickEditProfile.hint = userId


        btnNickCheckEditPage.setOnClickListener{

        }




    }

}