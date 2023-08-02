package com.smhrd.gmore.user

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.smhrd.gmore.MainActivity
import com.smhrd.gmore.R
import com.smhrd.gmore.databinding.ActivityLoginBinding
import com.smhrd.gmore.vo.MemberVO
import com.smhrd.gmore.vo.MembersResponse
import com.smhrd.gmore.vo.RQMember
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    lateinit var etLoginId : EditText
    lateinit var etLoginPw : EditText
    lateinit var btnLogin : Button
    lateinit var tvToJoin : TextView

    lateinit var reqQue : RequestQueue

    var reqURL : String = "http://172.30.1.11:8888/"


    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        etLoginId = findViewById(R.id.etLoginId)
        etLoginPw = findViewById(R.id.etLoginPw)
        btnLogin = findViewById(R.id.btnLogin)
        tvToJoin = findViewById(R.id.tvToJoin)

        reqQue = Volley.newRequestQueue(this@LoginActivity)

        // 카카오 로그인 init
        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        // MainActivity로 가는 Intent
        var it_toMain = Intent(this@LoginActivity, MainActivity::class.java)

        // SharedPreference
        val spf = getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        // MODE_PRIVATE : 내부 캐시에 저장 -> 노출 x

        // - editor 사용
        val editor= spf.edit()

        // ------------------------------------------------------------------------------------------------------------------------------------------------

        // (기본) 로그인 하기
        btnLogin.setOnClickListener {
            var inputId = etLoginId.text.toString()
            var inputPw = etLoginPw.text.toString()


            var request = object : StringRequest(
                Request.Method.POST,
                    // http://172.30.1.21:8888/member/login
                reqURL + "member/login",
//                "http://172.30.1.40:8888/user/checknick",
                {
                        response ->
                    startActivity(it_toMain)
                    Log.d("response", response.toString())

                    val gson = Gson()
                    val membersResponse: MembersResponse = gson.fromJson(response, MembersResponse::class.java)
                    val firstMember: RQMember? = membersResponse.members.firstOrNull()

                    firstMember?.let { member ->
                        val userId: Int = member.id
                        val userNick: String = member.nick

                        editor.putString("loginedId", userNick) // 아이디 값을 SPF에 저장
                        editor.commit()

                        intent.putExtra("selected_login_id", userId.toString()) // 아이디 값을 인텐트에 저장
                        startActivity(it_toMain)
                    } ?: run {
                        // 회원 정보가 없을 경우, 처리할 수 있는 코드를 추가해주세요.
                    }
                },
                {
                        error ->
                    Log.d("error", error.toString())
                }
            ){
                override fun getParams(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap<String, String>()

                    val mv : MemberVO = MemberVO(inputId, inputPw, null, null)
                    params.put("LoginMember", Gson().toJson(mv))

                    return params
                }
            }
            reqQue.add(request)
        }



        // 회원가입으로 가기
        tvToJoin.setOnClickListener{
            var it = Intent(this, JoinActivity::class.java)
            startActivity(it)
        }


        // 카카오톡으로 로그인 하기
        binding.ivLoginKako.setOnClickListener {
            // 콜백함수
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                // 실패했을 때
                if (error != null) {
                    if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        Toast.makeText(this, "취소하셨습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("LOGIN", "카카오계정으로 로그인 실패 콜백함수", error)
                        Toast.makeText(this, "로그인 실패-콜백", Toast.LENGTH_SHORT).show()
                    }
                }
                // 성공했을 때
                else if (token != null) { 
                    Log.i("LOGIN", "카카오계정으로 로그인 성공 콜백함수 ${token.accessToken}")
                    //Toast.makeText(this, "로그인 성공-콜백", Toast.LENGTH_SHORT).show()

                    Log.d("LOGIN", "사용자 정보 가져오기 . me()")
                    UserApiClient.instance.me{ user, error ->
                        if(error!=null) {
                            Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
                        } else if(user != null) {
                            Log.i(ContentValues.TAG, "사용자 정보 요청 성공" + "이메일: ${user.kakaoAccount?.email}")
                            var loginedID = user.kakaoAccount?.email!!


                            checkEmail(loginedID) {isEmailExists ->
                                if(isEmailExists) {
                                    Log.d("Login", "spf에 저장")
                                    // spf에 저장!
                                    editor.putString("loginedId", loginedID)
                                    editor.commit()

                                    startActivity(it_toMain)
                                    finish()
                                } else {
                                    //  // Toast.makeText(this, "카카오톡 로그인 실패", Toast.LENGTH_SHORT).show()
                                }
                            }


                        }
                    }
                }
            } // 콜백함수 끝

            Log.d("카카오 로그인", "카카오 계정으로 로그인")
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }


    }// oncreate 끝

    private fun checkEmail(id : String, callback : (Boolean)-> Unit) {
        var isOK : Boolean = false
        var request = object:StringRequest(Request.Method.GET,
            reqURL+"member/checkID/" + id,
            {
                response ->
                if(response.equals("이메일 있음")) {
                    Log.d("Login", "이메일 있음")
                    callback(true)
                } else {
                    Log.d("Login", "이메일 없음")
                    Toast.makeText(this, "회원가입이 되어있지 않습니다. 회원가입을 해주세요", Toast.LENGTH_LONG).show()
                    callback(false)
                }
            }, {
                err ->
                Log.d("checkEmail", "에러"+err.toString())
                callback(false)
            }
        ){}
        reqQue.add(request)
    }
}