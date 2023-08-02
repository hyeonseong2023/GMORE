package com.smhrd.gmore.user

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
import com.smhrd.gmore.R
import com.smhrd.gmore.vo.MemberVO

class LoginActivity : AppCompatActivity() {
    lateinit var etLoginId : EditText
    lateinit var etLoginPw : EditText
    lateinit var btnLogin : Button
    lateinit var tvToJoin : TextView

    lateinit var reqQue : RequestQueue

    var reqURL : String = "http://172.30.1.21:8888/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginId = findViewById(R.id.etLoginId)
        etLoginPw = findViewById(R.id.etLoginPw)
        btnLogin = findViewById(R.id.btnLogin)
        tvToJoin = findViewById(R.id.tvToJoin)

        reqQue = Volley.newRequestQueue(this@LoginActivity)

        // 로그인 하기
        btnLogin.setOnClickListener {
            var inputId = etLoginId.text.toString()
            var inputPw = etLoginPw.text.toString()


            var request = object : StringRequest(
                Request.Method.POST,
                    // http://172.30.1.21:8888/member/login
                reqURL + "member/login",
                {
                    response ->
                    Log.d("response", response)
                }, {
                        error ->
                    Log.d("error", error.toString())
                    Toast.makeText(this, "error발생", Toast.LENGTH_SHORT).show()
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

        tvToJoin.setOnClickListener{
            var it = Intent(this, JoinActivity::class.java)
            startActivity(it)
        }


    }
}