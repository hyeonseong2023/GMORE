package com.smhrd.gmore.user
// 회원정보 수정(닉네임 / PW) 및 탈퇴 페이지
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.gmore.R
import com.smhrd.gmore.fragment.Fragment4
import org.json.JSONArray

class EditProfileActivity : AppCompatActivity() {

    lateinit var etNickEditProfile: EditText
    lateinit var etPwEditProfile: EditText
    lateinit var etPwEditProfile2: EditText
    lateinit var tvQuitEditProfile: TextView
    lateinit var btnNickCheckEditPage: Button
    lateinit var btnSaveEditProfile: Button


    lateinit var reqQueue: RequestQueue

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

        // spf 에서 user 데이터 가져오기
        val userEmail = spf.getString("userEmail", "")
        val userNick = spf.getString("userNick", "")
        Log.d("spf 값??", userNick.toString())
        etNickEditProfile.hint = userNick



        // 닉네임 중복 확인
        btnNickCheckEditPage.setOnClickListener {
            val changeNick = etNickEditProfile.text.toString()

            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.40:8888/user/checknick",
                { response ->
                    Log.d("response", response.toString())

                    if (response.toString().equals("닉네임중복")) {
                        Toast.makeText(this, "사용 불가능한 닉네임", Toast.LENGTH_SHORT).show()
                        etNickEditProfile.text = null
                    } else {

                        Toast.makeText(this, "사용 가능한 닉네임", Toast.LENGTH_SHORT).show()
                    }

                }, { error ->
                    Log.d("error", error.toString())
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap<String, String>()
                    params.put("changeNick", changeNick)
                    return params
                }
            }
            reqQueue.add(request)

        }


        // 변경 정보 저장
        btnSaveEditProfile.setOnClickListener {
            // 닉네임만 변경
            // PW만 변경(비밀번호 일치 확인)
            // 닉&PW 둘 다 변경(비밀번호 일치 확인)
            Log.d("PW 값",etPwEditProfile.text.toString())
            if (etPwEditProfile.text.toString().equals(etPwEditProfile2.text.toString()) || etPwEditProfile.text.toString() == null) {

                val request = object : StringRequest(
                    Request.Method.POST,
                    "http://172.30.1.40:8888/user/editmypage",
                    { response ->
                        Log.d("response", response.toString())

                        if (response.toString().equals("변경실패")) {
                            Toast.makeText(this, "변경실패", Toast.LENGTH_SHORT).show()
                            etNickEditProfile.text = null
                        } else {

                            Toast.makeText(this, "회원정보 변경", Toast.LENGTH_SHORT).show()

                            // ✨ spf에 변경한 유저 정보 담아주기
                            val spf = getSharedPreferences("userSPF", Context.MODE_PRIVATE)
                            val editor = spf.edit()
                            editor.putString("userNick",etNickEditProfile.text.toString())
                            editor.commit()

                            // 마이페이지로 화면 이동
                            var it = Intent(this@EditProfileActivity,Fragment4::class.java)
//                            startActivity(it)
                            finish()
                        }

                    }, { error ->
                        Log.d("error", error.toString())
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        val params: MutableMap<String, String> = HashMap<String, String>()
                        val editPageData = User_VO(
                            null,
                            userEmail,
                            etPwEditProfile.text.toString(),
                            etNickEditProfile.text.toString(),
                            null,
                            null
                        )

                        params.put("editPageData", Gson().toJson(editPageData))
                        Log.d("회원정보 수정 데이터 ?", editPageData.toString())
                        return params
                    }
                }
                reqQueue.add(request)

            } else {
                Toast.makeText(this, "PW를 확인 해주세요", Toast.LENGTH_SHORT).show()
            }
        }


    }

}