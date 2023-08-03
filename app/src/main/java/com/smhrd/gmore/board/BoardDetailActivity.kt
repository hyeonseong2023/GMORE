package com.smhrd.gmore.board

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.smhrd.gmore.R
import com.smhrd.gmore.user.JoinActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class BoardDetailActivity : AppCompatActivity() {

    // 뷰 변수 선언
    private lateinit var tvBoardTitle: TextView
    private lateinit var tvBoardWriter: TextView
    private lateinit var tvBoardDate: TextView
    private lateinit var tvBoardContent: TextView
    private lateinit var ivBoardImage: ImageView
    private lateinit var ivInputAdd: ImageView
    private lateinit var rvComments: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var boardbookmark: ImageView
    private lateinit var boardLike: ImageView
    private lateinit var etCommentInput: EditText
    private lateinit var btnSubmitComment: ImageView
    private lateinit var btnBoradDelete: Button
    private lateinit var btnBoradUpdate: Button

    lateinit var boardId: String
    lateinit var login_id: String
    lateinit var login_nick: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        // 뷰 초기화를 setContentView 호출 후에 수행합니다.
        etCommentInput = findViewById(R.id.editTextText)
        btnSubmitComment = findViewById(R.id.ivInputAdd)
        tvBoardTitle = findViewById(R.id.tvBoardTitle)
        tvBoardWriter = findViewById(R.id.tvBoardWriter)
        tvBoardDate = findViewById(R.id.tvBoardDate)
        tvBoardContent = findViewById(R.id.tvBoardContent)
        ivBoardImage = findViewById(R.id.ivBoardImage)
        ivInputAdd = findViewById(R.id.ivInputAdd)
        rvComments = findViewById(R.id.rvComments)
        boardbookmark = findViewById(R.id.boardBookmark)
        boardLike = findViewById(R.id.boardLike)
        btnBoradDelete = findViewById(R.id.btnBoradDelete)
        btnBoradUpdate = findViewById(R.id.btnBoradUpdate)
        val sharedPreferences = getSharedPreferences("sdf", Context.MODE_PRIVATE)
        login_id = sharedPreferences.getString("selected_login_id", "1") ?: "1"
        login_nick = sharedPreferences.getString("userNick", "1") ?: "1"

        boardId = intent.getIntExtra("selected_board_id", -1).toString()
        Log.d("boardId??? : ", boardId)
        fetchBoardDetail()
        fetchComments()
        var isBookmarked = false // 북마크 상태를 저장하는 변수 (기본값: false)

        //별이미지 사용하는 북마크
        boardbookmark.setOnClickListener {
            isBookmarked = !isBookmarked // isBookmarked 값을 전환

            if (isBookmarked) {
                boardbookmark.setImageResource(R.drawable.star_on) // 활성화 된 별 이미지로 변경
            } else {
                boardbookmark.setImageResource(R.drawable.star_off) // 비활성화 된 별 이미지로 변경
            }
            updateBookmark(isBookmarked)
        }
        btnSubmitComment.setOnClickListener {
            submitComment()
        }

        var isLiked = false // 좋아요 상태를 저장하는 변수 (기본값: false)
//좋아요
        boardLike.setOnClickListener {
            isLiked = !isLiked

            if (isLiked) {
                boardLike.setImageResource(R.drawable.harton) // 활성화 된 하트 이미지로 변경
            } else {
                boardLike.setImageResource(R.drawable.icon_like_off) // 비활성화 된 하트 이미지로 변경
            }
            updateLike(isLiked)
        }

        btnBoradDelete.setOnClickListener {
            fetchBoardDelete()
        }

        btnBoradUpdate.setOnClickListener {
            val intent = Intent(this, BoardEditActivity::class.java).apply {
                putExtra("boardId", boardId)
                putExtra("title", tvBoardTitle.text.toString())
                putExtra("writer", tvBoardWriter.text.toString())
                putExtra("date", tvBoardDate.text.toString())
                putExtra("content", tvBoardContent.text.toString())
                // 이미지 URL을 변수로 가져올 수 있는 경우 다음 변수 이름을 사용합니다.
                // putExtra("image_url", imageUrl)
            }
            startActivity(intent)
        }
    }

    //게시글 삭제 버튼 반응
    private fun fetchBoardDelete() {
        thread {
            try {
                val urlString = "http://172.30.1.24:8888/board/detail/$boardId/delete"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "GET"

                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                val response = StringBuilder()

                var inputLine: String?
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }

                // 받은 값(응답 문자열)에 따른 조건 처리
                when (response.toString().trim()) {
                    "Success" -> {
                        runOnUiThread {
                            val intent = Intent(this, GameCategoryActivity::class.java)
                            startActivity(intent)
                            finish() // 현재 Activity 닫기
                        }
                    }

                    "Fail" -> {
                        runOnUiThread {
                            Toast.makeText(this, "실패.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    else -> {

                    }
                }

            } catch (e: Exception) {
                Log.e("Fetch c", "Error fetching board detail: ${e.message}", e)
            }
        }
    }

    private fun fetchBoardDetail() {
        thread {
            try {
                val urlString = "http://172.30.1.24:8888/board/detail/${boardId}"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                val response = StringBuilder()

                var inputLine: String?
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                `in`.close()

                Log.d("Response", response.toString())
                Log.d("Response", "Received response: $response")
                // 응답을 Kotlin 데이터 클래스로 변환
                val gson = Gson()
                val boardDetail = gson.fromJson(response.toString(), BoardDetailVO::class.java)

                // UI 갱신
                runOnUiThread {
                    tvBoardTitle.text = boardDetail.title
                    tvBoardWriter.text = boardDetail.nickname
                    tvBoardDate.text = boardDetail.date_created
                    tvBoardContent.text = boardDetail.content
                    // 이미지 로드
                    // 예를 들어 Glide 라이브러리를 사용한다면:
                    // Glide.with(this).load(boardDetail.image_url).into(ivBoardImage)
                    if (login_nick == boardDetail.nickname) {
                        btnBoradDelete.visibility = View.VISIBLE
                    }

                }

            } catch (e: Exception) {
                Log.e("Fetch c", "Error fetching board detail: ${e.message}", e)
            }
        }
    }

    private fun fetchComments() {
        thread {
            try {
                val urlString = "http://172.30.1.24:8888/board/detail/${boardId}/comments"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "GET"

                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                val response = StringBuilder()

                var inputLine: String?
                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                `in`.close()

                Log.d("Response", response.toString())

                // 응답을 Kotlin 데이터 클래스로 변환
                val gson = Gson()
                val comments =
                    gson.fromJson(response.toString(), Array<CommentVO>::class.java).toList()

                // UI 갱신
                runOnUiThread {
                    // 리사이클러뷰 어댑터 설정
                    commentAdapter = CommentAdapter(comments)
                    rvComments.adapter = commentAdapter
                    rvComments.layoutManager = LinearLayoutManager(this@BoardDetailActivity)
                }

            } catch (e: Exception) {
                Log.e("Fetch Comments", "Error fetching comment list: ${e.message}", e)
            }
        }
    }

    private fun updateBookmark(isBookmarked: Boolean) {
        thread {
            try {
                val urlString =
                    "http://172.30.1.24:8888/board/detail/${boardId}/1/$isBookmarked/book"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "GET"

                val responseCode = conn.responseCode
                Log.d("Response", "Response Code: $responseCode")

            } catch (e: Exception) {
                Log.e("Update Bookmark", "Error updating bookmark: ${e.message}", e)
            }
        }
    }

    private fun updateLike(isLiked: Boolean) {
        thread {
            try {
                val urlString = "http://172.30.1.24:8888/board/detail/${boardId}/1/$isLiked/like"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "GET"

                val responseCode = conn.responseCode
                Log.d("Response", "Response Code: $responseCode")

            } catch (e: Exception) {
                Log.e("Update Like", "Error updating like: ${e.message}", e)
            }
        }
    }

    private fun submitComment() {
        thread {
            try {
//                val commentText = etCommentInput.text.toString()
//                val urlString = "http://172.30.1.11:8888/board/detail/${boardId}/1/$isLiked/like"
//
//                val url = URL(urlString)
//                val conn = url.openConnection() as HttpURLConnection
//
//                conn.requestMethod = "POST"
//                conn.doOutput = true
//
//                val outputStream = conn.outputStream
//                outputStream.write(postData.toByteArray())
//                outputStream.close()
//
//                val responseCode = conn.responseCode
//                Log.d("Response", "Response Code: $responseCode")
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    runOnUiThread {
//                        etCommentInput.setText("") // 코멘트 전송에 성공하면 텍스트를 지웁니다.
//                        fetchComments() // 코멘트 목록을 새로 고칩니다.
//                    }
//                }

            } catch (e: Exception) {
                Log.e("Submit Comment", "Error submitting comment: ${e.message}", e)
            }
        }
    }


}
