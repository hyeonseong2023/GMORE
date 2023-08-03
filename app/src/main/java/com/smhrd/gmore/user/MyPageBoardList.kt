package com.smhrd.gmore.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.gmore.R
import com.smhrd.gmore.board.BoardDetailVO
import org.json.JSONArray

class MyPageBoardList : AppCompatActivity() {

    lateinit var rvMyBoard: RecyclerView
    var data = ArrayList<BoardDetailVO>()

    lateinit var requestQueue: RequestQueue
    var userId = 0

//    lateinit var adapter: MyPageBoardAdapter
    lateinit var board : BoardDetailVO



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_board_list)

        requestQueue = Volley.newRequestQueue(this@MyPageBoardList)
        rvMyBoard = findViewById(R.id.rvMyBoard)


        // spf 이용해 로그인 유저 데이터 받아오기
        val spf = getSharedPreferences(
            "userSPF",
            Context.MODE_PRIVATE
        )

        // spf 에서 user 데이터 가져오기
        userId = spf.getInt("userId", 0)
        Log.d("리스트인덱스", userId.toString())
        val request = object : StringRequest(
            Request.Method.POST,
            "http://172.30.1.40:8888/user/boardlist",
            { response ->
                Log.d("response", response.toString())




                var result = JSONArray(response)
                for (i in 0 until result.length()) {
                    // 여기 수정 -> board 전역 변수로 변경
                     board = Gson().fromJson(result.get(i).toString(), BoardDetailVO::class.java)
                    data.add(board)
//                    adapter = MyPageBoardAdapter(applicationContext, R.layout.myboardtemplate, data)


                }

                // ✨✨✨✨✨adapter 생성시 넘겨 주는 매개 변수 순서 잘 맞춰 주기
                var adapter = MyPageBoardAdapter(applicationContext, R.layout.myboardtemplate, data)
                rvMyBoard.layoutManager = LinearLayoutManager(this)
                rvMyBoard.adapter = adapter


            }, { error ->
                Log.d("error", error.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap<String, String>()
                params.put("userId", userId.toString())

                return params
            }
        }
        requestQueue.add(request)

    }


}