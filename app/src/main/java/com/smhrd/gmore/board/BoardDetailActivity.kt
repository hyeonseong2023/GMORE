package com.smhrd.gmore.board

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
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
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
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
    private lateinit var rvComments: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var boardbookmark: ImageView
    private lateinit var boardLike: ImageView
    private lateinit var etCommentInput: EditText
    private lateinit var btnSubmitComment: ImageView
    private lateinit var btnBoradDelete: Button
    private lateinit var btnBoradUpdate: Button

    lateinit var boardId:String
    lateinit var login_id:String
    lateinit var login_nick:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        // 뷰 초기화를 setContentView 호출 후에 수행합니다.
        etCommentInput = findViewById(R.id.edtComment)
        btnSubmitComment = findViewById(R.id.ivInputAdd)
        tvBoardTitle = findViewById(R.id.tvBoardTitle)
        tvBoardWriter = findViewById(R.id.tvBoardWriter)
        tvBoardDate = findViewById(R.id.tvBoardDate)
        tvBoardContent = findViewById(R.id.tvBoardContent)
        ivBoardImage = findViewById(R.id.ivBoardImage)
        rvComments = findViewById(R.id.rvComments)
        boardbookmark = findViewById(R.id.boardBookmark)
        boardLike = findViewById(R.id.boardLike)
        btnBoradDelete = findViewById(R.id.btnBoradDelete)
        btnBoradUpdate = findViewById(R.id.btnBoradUpdate)
        val spf = getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        login_id = spf.getString("userId", "").toString()   // 유저코드 값 불러오기
        login_nick = spf.getString("userNick", "").toString() // 닉네임 값 불러오기
        boardId =  intent.getIntExtra("selected_board_id", -1).toString()
        Log.d("boardId??? : ", login_id)
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

        btnBoradDelete.setOnClickListener{
            fetchBoardDelete()
        }


        btnBoradUpdate.setOnClickListener {
            val intent = Intent(this, BoardEditActivity::class.java).apply {
                putExtra("boardId", boardId)
                putExtra("title", tvBoardTitle.text.toString())
                putExtra("writer", tvBoardWriter.text.toString())
                putExtra("date", tvBoardDate.text.toString())
                putExtra("content", tvBoardContent.text.toString())
                if (ivBoardImage.drawable != null) {
                    putExtra("base64_image", bitmapToBase64((ivBoardImage.drawable as BitmapDrawable).bitmap))
                }
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

            conn.requestMethod = "DELETE"

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
                    if (boardDetail.nickname == login_nick) {
                        btnBoradDelete.visibility = View.VISIBLE
                        btnBoradUpdate.visibility = View.VISIBLE
                    } else {
                        btnBoradDelete.visibility = View.GONE
                        btnBoradUpdate.visibility = View.GONE
                    }
                    if (boardDetail.image_url != null) {
                        // 이미지 로드
                        val bitmapImage = decodeBase64ToBitmap(boardDetail.image_url)
                        if (bitmapImage != null) {
                            ivBoardImage.setImageBitmap(bitmapImage)
                        }
                    }

                    // 게시물 작성자와 현재 로그인한 사용자가 동일할 때 삭제 버튼을 표시

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64: String): Bitmap {
        val imageBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun decodeBase64ToBitmap(base64: String): Bitmap? {
        if (base64.isEmpty()) {
            return null
        }

        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
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
                val urlString = "http://172.30.1.24:8888/board/detail/${boardId}/${login_id}/$isBookmarked/book"
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
    override fun onBackPressed() {
        val intent = Intent(this, GameCategoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateLike(isLiked: Boolean) {
        thread {
            try {
                val urlString = "http://172.30.1.24:8888/board/detail/${boardId}/${login_id}/$isLiked/like"
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
        val content = etCommentInput.text.toString().trim()

        if (content.isEmpty()) {
            Toast.makeText(this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        thread {
            try {
                val urlString = "http://172.30.1.24:8888/board/detail/$boardId/newcomment/$login_id"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection

                conn.requestMethod = "POST"
                conn.doOutput = true
                // 댓글 내용을 서버로 전송하기 위한 설정
                val body = "content=$content"
                val postData = body.toByteArray(Charsets.UTF_8)
                conn.setRequestProperty("Content-Length", postData.size.toString())
                conn.outputStream.write(postData)

                val responseCode = conn.responseCode
                Log.d("Response", "Response Code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread {
                        Toast.makeText(this, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
                        etCommentInput.text.clear()
                        fetchComments() // 댓글 작성 후 댓글 목록을 갱신합니다.
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "댓글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("Submit Comment", "Error submitting comment: ${e.message}", e)
            }
        }
    }




}
