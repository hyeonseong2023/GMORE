package com.smhrd.gmore.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smhrd.gmore.R
import com.smhrd.gmore.vo.BoardCategoryVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GameCategoryActivity : AppCompatActivity() {

    lateinit var reqQueue:RequestQueue
    lateinit var rv:RecyclerView
    lateinit var tvCategoryName : TextView
    val boardList = ArrayList<BoardCategoryVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_category)


        rv = this.findViewById(R.id.rvCategoryList1)
        tvCategoryName = this.findViewById(R.id.tvCategoryListName1)

        reqQueue = Volley.newRequestQueue(this@GameCategoryActivity)
        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.24:8888/board",
            {
                response ->
                // 로그 출력: 원본 응답 확인
                Log.d("response", response)
                Log.d("response", response.toString())

                var result = JSONArray(response)
                Log.d("jsonA", result.toString())

//                for(i in 0 until result.length()){
//                    val board = Gson().fromJson(result.get(i).toString(), BoardCategoryVO::class.java)
//                    boardList.add(board)
//                }

                val gson = Gson()
                val typeToken = object : TypeToken<List<BoardCategoryVO>>() {}.type
                // 로그 출력: 타입 토큰(TypeToken) 확인
                Log.d("TypeToken", typeToken.toString())
                boardList.clear()
                boardList.addAll(gson.fromJson(response, typeToken))
                // 로그 출력: 변환된 데이터 확인
                Log.d("boardList", boardList.toString())

                val adapter = BoardCategoryAdapter(this@GameCategoryActivity, boardList)
                rv.layoutManager = LinearLayoutManager(this@GameCategoryActivity)
                rv.adapter = adapter

            },
            {
                error ->
                Log.d("error!!", error.toString())
            }
        ){}
        reqQueue.add(request)
    }


}