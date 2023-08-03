package com.smhrd.gmore.user

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.smhrd.gmore.R
import com.smhrd.gmore.databinding.ActivityJoinBinding
import com.smhrd.gmore.vo.MemberVO

class JoinActivity : AppCompatActivity() {
    lateinit var ivKakao : ImageView
    lateinit var etJoinId : EditText
    lateinit var etJoinPw : EditText
    lateinit var etJoinPwCheck : EditText
    lateinit var etJoinNick : EditText
    lateinit var btnJoinIdCheck : Button
    lateinit var btnNickCheck : Button
    lateinit var btnJoin : Button


    var isIdOk : Boolean = false
    var isPwOk : Boolean = false
    var isNickOk : Boolean = false

    var reqURL : String = "http://172.30.1.29:8888/"

    lateinit var binding : ActivityJoinBinding
    val constraintSet = ConstraintSet()
    lateinit var constraint1 : ConstraintLayout


    lateinit var reqQue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_join)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        constraint1 = findViewById(R.id.joinRoot)


        etJoinId = findViewById(R.id.etJoinId)
        etJoinPw = findViewById(R.id.etJoinPw)
        etJoinPwCheck = findViewById(R.id.etJoinPwCheck)
        etJoinNick = findViewById(R.id.etJoinNick)
        btnJoinIdCheck = findViewById(R.id.btnJoinIdCheck)
        btnNickCheck = findViewById(R.id.btnNickCheck)
        btnJoin = findViewById(R.id.btnJoin)
        ivKakao = findViewById(R.id.ivKakao)
        btnJoin.isEnabled = false

        reqQue = Volley.newRequestQueue(this@JoinActivity)


        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        // 해쉬코드 알아내는 코드
        var keyHash = Utility.getKeyHash(this)
        Log.d("키 해쉬는 ", keyHash)


        // 아이디
        etJoinId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            //텍스트가 변경된 이후에 호출.
            override fun afterTextChanged(s: Editable) {
                checkJoin()
            }
        })

        // 아이디 중복 검사
        btnJoinIdCheck.setOnClickListener {
            if(!etJoinId.text.isEmpty()) {
                checkID(etJoinId.text.toString())
            }
        }

        // 닉네임 입력 감지
        etJoinNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            //텍스트가 변경된 이후에 호출.
            override fun afterTextChanged(s: Editable) {
                btnJoin.isEnabled = false

            }
        })

        // 닉네임 중복 검사
        btnNickCheck.setOnClickListener {
            Log.d("실행", "안1")
            if(!etJoinNick.text.isEmpty()) {
                checkNick(etJoinNick.text.toString())
            }else {
                isNickOk = false
                checkJoin()
            }
        }

        // 비밀번호 & 비밀번호 확인 검사
        etJoinPwCheck.addTextChangedListener(object : TextWatcher {
            var number = 0
            var preText: String? = null
            //변경되기 전 문자열을 담고있다.
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                preText = s.toString()
            }

            //텍스트가 변경될때 마다 호출된다. 보통은 이 함수안에 이벤트를 많이 사용하는것 같다.
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                //밑의 editText.setText(number+""); 가 실행되면 onTextChanged()함수가 계속해서 다시 호출 되기 때문에 추가했다.
                if (s.toString() == preText) return

                //editText에 포커스가 되어있고 텍스트가 하나라도 입력되어 있을때 동작하기 위해서 추가.
//                if (etJoinPwCheck.isFocusable() && s.toString() != "") {
//                    number = try {
//                        etJoinPwCheck.getText().toString().toInt()
//                    } catch (e: NumberFormatException) {
//                        e.printStackTrace()
//                        return
//                    }
                    //etJoinPwCheck.setText(number.toString() + "")
//                }
            }

            //텍스트가 변경된 이후에 호출.
            override fun afterTextChanged(s: Editable) {
                if( etJoinPw.text.toString().equals(etJoinPwCheck.text.toString()) ) {
                    isPwOk = true
                    checkJoin()
                    Log.d("비밀번호 일치", "비밀번호가 일치함.")
                } else {
                    Log.d("비밀번호 일치", "비밀번호가 불일치함.")
                    isPwOk = false
                    checkJoin()
                }
            }
        })


        // 회원가입 하기
        btnJoin.setOnClickListener {
            var request = object: StringRequest(
                Request.Method.POST,
                reqURL+"member/join/",
                {
                    response->
//                    Log.d("비밀번호 일치", "비밀번호가 일치함.")
                    if(response.equals("로그인 성공")) {
                        var intent = Intent(this@JoinActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "로그인 실패😥", Toast.LENGTH_SHORT).show()
                    }


                }, {
                    error ->

                }
            ){
                override fun getParams() : MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap<String, String>()

                    val mv : MemberVO = MemberVO(etJoinId.text.toString(), etJoinPw.text.toString(), etJoinNick.text.toString(), "")
                    params.put("JoinMember", Gson().toJson(mv))
                    return params
                }

            }
            reqQue.add(request)
        }


        // 카카오톡으로 회원가입
        ivKakao.setOnClickListener {

            fun navigateToLoginActivity() {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }

            Log.d("카카오 로그인", "클릭 됨")

            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨


            // 콜백함수
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {

                    if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        Toast.makeText(this, "취소하셨습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("Join", "카카오계정으로 로그인 실패 콜백함수", error)
                        Toast.makeText(this, "로그인 실패-콜백", Toast.LENGTH_SHORT).show()
                    }

                } else if (token != null) {
                    Log.i("LOGIN", "카카오계정으로 로그인 성공 콜백함수 ${token.accessToken}")
                    var tempPW : String = token.accessToken
                    binding.etJoinPw.setText(tempPW)
                    binding.etJoinPwCheck.setText(tempPW)
                    Toast.makeText(this, "로그인 성공-콜백", Toast.LENGTH_SHORT).show()

                    Log.d("LOGIN", "사용자 정보 가져오기 . me()")

                    // 카카오톡의 이메일 정보 가져오기
                    UserApiClient.instance.me{ user, error ->
                        if(error!=null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        } else if(user != null) {
                            Log.i(TAG, "사용자 정보 요청 성공" + "이메일: ${user.kakaoAccount?.email}")

                            // 아이디 입력 칸이 비어있으면
                            if(!user.kakaoAccount?.email!!.isEmpty()) {
                                Log.d("돼나?", "돼나???")
                                Log.d("돼나?", "돼나???")
                                etJoinId.setText("${user.kakaoAccount?.email.toString()}")
                                //Toast.makeText(this, "로그인 성공-콜백", Toast.LENGTH_SHORT).show()
                                if(user.kakaoAccount?.email.toString()!=null) {
                                    Toast.makeText(this, "로그인 성공-콜백"+user.kakaoAccount?.email.toString(), Toast.LENGTH_SHORT).show()
                                }

                                etJoinId.isEnabled = false
                                isIdOk = true
                                binding.etJoinPw.visibility = View.GONE
                                binding.etJoinPwCheck.visibility = View.GONE
                                constraintSet.connect(binding.constraintLayout2.id, ConstraintSet.BOTTOM, binding.linearLayout.id, ConstraintSet.TOP)

                                constraintSet.applyTo(constraint1)
                                checkJoin()


                            }

                        }


                    }

                }
            } // 콜백함수 끝


            Log.d("카카오 로그인", "카카오 계정으로 로그인")
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)


        }  //setOnClickListener 함수 끝



    }

    fun getJoinCheck() {
        if(isIdOk && isPwOk && isNickOk) {
            btnJoin.isEnabled = true
        }
    }

    fun checkID(id : String) {
        Log.d("실행", "안3: "+id)
        Log.d("실행", reqURL+"member/checkID")
        var request = object:StringRequest( Request.Method.GET,
            reqURL+"member/checkID/"+id,
            {
                response ->
                Log.d("response", response)
                if(response.equals("이메일 없음")) {
                    Toast.makeText(this, "아이디 중복 검사 : 아이디 사용 가능!", Toast.LENGTH_SHORT).show()
                    isIdOk = true
                    checkJoin()
                } else {
                    Toast.makeText(this, "아이디 중복 검사 : 아이디 사용 불가능", Toast.LENGTH_SHORT).show()
                    isIdOk = false
                    checkJoin()
                }

            }, {
                error ->
                Log.d("error", error.toString())
                Toast.makeText(this, "error발생", Toast.LENGTH_SHORT).show()
            }
        ){ }
        reqQue.add(request)
    }

    fun checkNick(nick : String) {

        Log.d("실행", "안3: "+nick)
        Log.d("실행", reqURL+"member/checkNick")
        var request = object:StringRequest( Request.Method.GET,
            reqURL+"member/checkNick/"+nick,
            {
                response ->
                Log.d("response", response)



                if(response.equals("[]")) {
                    Toast.makeText(this, "닉네임 중복 검사 : 닉네임 사용 가능!", Toast.LENGTH_SHORT).show()
                    isNickOk = true
                    checkJoin()
                } else {
                    Toast.makeText(this, "닉네임 중복 검사 : 닉네임 사용 불가능", Toast.LENGTH_SHORT).show()
                    isNickOk = false
                    checkJoin()
                }

            }, {
                error ->
                Log.d("error", error.toString())
                Toast.makeText(this, "error발생", Toast.LENGTH_SHORT).show()
            }
        ){ }
        reqQue.add(request)
        checkJoin()
    }

    fun checkJoin() {
        if (isIdOk && isPwOk && isNickOk) {
            btnJoin.isEnabled = true
        } else {
            btnJoin.isEnabled = false
        }
    }

}