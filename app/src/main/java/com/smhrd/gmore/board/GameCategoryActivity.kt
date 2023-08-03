package com.smhrd.gmore.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smhrd.gmore.HomeActivity
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

    lateinit var reqQueue: RequestQueue
    lateinit var rv: RecyclerView
    lateinit var tvCategoryName: TextView
    val boardList = ArrayList<BoardCategoryVO>()
    lateinit var btnWriteNext : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_category)

        rv = this.findViewById(R.id.rvCategoryList1)
        tvCategoryName = this.findViewById(R.id.tvCategoryListName1)
        btnWriteNext = this.findViewById(R.id.btnNextWrite)

        reqQueue = Volley.newRequestQueue(this@GameCategoryActivity)

        btnWriteNext.setOnClickListener {
            var intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("buttonText", tvCategoryName.text.toString())
            startActivity(intent)
        }

        val category = intent.getStringExtra("buttonText")

        val request = object : StringRequest(
            Request.Method.GET,
            "http://172.30.1.24:8888/board/category?category=$category",
//            "https://57ef-211-223-144-120.ngrok.io/board/category?category=$category",
            { response ->
                Log.d("response", response.toString())

                var result = JSONArray(response)

                // JSON 응답을 List<BoardCategoryVO>로 변환하여 boardList 에 저장
                val typeToken = object : TypeToken<List<BoardCategoryVO>>() {}.type
                boardList.clear()
                boardList.addAll(Gson().fromJson(response, typeToken))

                val adapter = BoardCategoryAdapter(this@GameCategoryActivity, boardList)

                // 아이템 클릭 이벤트 처리
                adapter.categoryClickEvent = object : CategoryClickEvent {
                    override fun onItemClick(position: Int) {
                        val selectedBoard = boardList[position]

                        val intent = Intent(this@GameCategoryActivity, HomeActivity::class.java)
                        intent.putExtra("selected_board_id", selectedBoard.categoryBoardId)
                        startActivity(intent)
                    }
                }

                rv.layoutManager = LinearLayoutManager(this@GameCategoryActivity)
                rv.adapter = adapter
            },
            { error ->
                Log.d("error!!", error.toString())
            }
        ) {}
        reqQueue.add(request)

    }

}