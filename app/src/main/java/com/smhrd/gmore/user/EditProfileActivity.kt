package com.smhrd.gmore.user
// 회원정보 수정(닉네임 / PW) 및 탈퇴 페이지
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.smhrd.gmore.R

class EditProfileActivity : AppCompatActivity() {

    lateinit var etNickEditProfile:EditText
    lateinit var etPwEditProfile : EditText
    lateinit var etPwEditProfile2:EditText
    lateinit var tvQuitEditProfile : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etNickEditProfile = findViewById(R.id.etNickEditProfile)
        etPwEditProfile = findViewById(R.id.etPwEditProfile)
        etPwEditProfile2 = findViewById(R.id.etPwEditProfile2)
        tvQuitEditProfile = findViewById(R.id.tvQuitEditPrpfile)




    }
}