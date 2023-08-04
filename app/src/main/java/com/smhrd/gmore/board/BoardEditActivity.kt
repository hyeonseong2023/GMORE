package com.smhrd.gmore.board

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.smhrd.gmore.R
import java.io.ByteArrayOutputStream

class BoardEditActivity : AppCompatActivity() {

    lateinit var btnEditClose: ImageButton
    lateinit var btnEditOk: ImageButton
    lateinit var etEditTitle: EditText
    lateinit var etEditContent: EditText
    lateinit var btnEditPhoto: ImageButton
    lateinit var btnEditCam: ImageButton
    lateinit var ivEditUpload: ImageView
    lateinit var ivDelete: ImageButton
    lateinit var imagebitmap : Bitmap

    lateinit var EditImgLine: View
    lateinit var reqQueue: RequestQueue
    var encodeImgString: String = ""
    val STORAGE_CODE = 1000

    var imgCamUpload = false
    var imgPhotoUpload = false

    // 작성 중인 게시글 닫을 때 알림창 확인 클릭했을 때
    val dialogListener = DialogInterface.OnClickListener { dialogInterface, i ->
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)

        btnEditClose = findViewById(R.id.btnEditClose)
        btnEditOk = findViewById(R.id.btnEditOk)
        etEditTitle = findViewById(R.id.etEditTitle)
        etEditContent = findViewById(R.id.etEditContent)
        btnEditPhoto = findViewById(R.id.btnEditPhoto)
        btnEditCam = findViewById(R.id.btnEditCam)
        EditImgLine = findViewById(R.id.EditImgLine)
        ivEditUpload = findViewById(R.id.ivEditUpload)
        ivDelete = findViewById(R.id.ivEditDelete)

        reqQueue = Volley.newRequestQueue(this@BoardEditActivity)

        val spf = getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        val userId = spf.getString("userId", "")?.toInt()
        val userNick = spf.getString("userNick", "").toString()

        // 💡 해당 게시물 정보 불러오기
        val boardId = intent.getStringExtra("boardId")?.toInt()
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val date = intent.getStringExtra("date")
        val image = intent.getStringExtra("base64_image").toString()
        Log.d("image", intent.getStringExtra("base64_image").toString())

        // 이미지 예외처리
        // 불러온 이미지 String -> Bitmap
        if(intent.getStringExtra("base64_image").toString() == "null"){ // 불러올 이미지가 없다면
            ivDelete.visibility = View.INVISIBLE
        }else{  // 불러올 이미지가 있다면
            val imageBytes = Base64.decode(image, 0)
            imagebitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivEditUpload.setImageBitmap(imagebitmap)
        }



        etEditTitle.setText(title)
        etEditContent.setText(content)


        // 뒤로가기 버튼
        btnEditClose.setOnClickListener {
            if (etEditTitle.text.toString() == "" && etEditContent.text.toString() == "") {
                // 해당 게임 게시판으로 돌아가기
                var it = Intent(this, GameCategoryActivity::class.java)
                startActivity(it)
                finish()
            } else {  // 제목이나 내용에 글이 적혀있다면 알림창 띄우기
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("작성 중인 글을 취소하시겠습니까? 확인 선택 시, 수정된 글은 저장되지 않습니다.")
                builder.setPositiveButton("확인", dialogListener)
                builder.setNegativeButton("취소", null)
                builder.create().show()
            }
        }


        // 글 수정하기 버튼
        btnEditOk.setOnClickListener {
            val inputTitle = etEditTitle.text.toString()
            val inputContent = etEditContent.text.toString()
            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.40:8888/board/update", // board_id
//                "http://localhost:8888/board/write",
                { response ->
                    Log.d("response", response.toString())
                    if (response == "Success") {
                        val it = Intent(this, GameCategoryActivity::class.java)
                        startActivity(it)
                        finish()
                    } else {
                        Toast.makeText(this, "Fail....", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.d("에러", error.toString())
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap<String, String>()
                    val board = BoardDetailVO(boardId, inputTitle, inputContent, encodeImgString, null, userId, date, userNick)
                    params.put("board", Gson().toJson(board))
                    return params
                }
            }
            reqQueue.add(request)
        }


        // 갤러리 버튼
        btnEditPhoto.setOnClickListener {
            imgPhotoUpload = true
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, STORAGE_CODE)
        }


        // 카메라 버튼
        btnEditCam.setOnClickListener {
            imgCamUpload = true
            val cameraPermissionCheck = ContextCompat.checkSelfPermission(
                this@BoardEditActivity,
                android.Manifest.permission.CAMERA
            )
            val REQUEST_IMAGE_CAPTURE = 101
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    Log.d("req", REQUEST_IMAGE_CAPTURE.toString())
                }
            }
        }

        // 추가된 이미지 삭제 버튼
        ivDelete.setOnClickListener{
            ivEditUpload.setImageBitmap(null)
            imgCamUpload = false
            imgPhotoUpload = false
            ivDelete.visibility = View.INVISIBLE
        }
    }


    private fun encodeBitmapImg(bitmap: Bitmap) {
        // ByteArray 생성할 수 있는 Stream
        val byteArrayOutputStream = ByteArrayOutputStream()
        // bitmap 압축
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        // img -> Array
        val bytesOfImg = byteArrayOutputStream.toByteArray()
        encodeImgString = Base64.encodeToString(bytesOfImg, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!imgCamUpload) {  // 갤러리에서 이미지를 가져오면 이미지뷰에 사진 삽입
            when (requestCode) {
                STORAGE_CODE -> {
                    // image에 대한 uri 가져오기
                    val selectedImgUri = data?.data
                    if (selectedImgUri != null) {
                        // uri -> bitmap으로 변환
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(contentResolver, selectedImgUri)
                        ivEditUpload.setImageBitmap(bitmap)

                        val options = BitmapFactory.Options()
                        options.inSampleSize = 4

                        val resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
                        encodeBitmapImg(resized)
                        ivDelete.visibility = View.VISIBLE
                        EditImgLine.visibility = View.VISIBLE
                    }
                }
            }
        } else {  // 카메라 촬영을 하면 이미지뷰에 사진 삽입
            if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
                // Bundle로 데이터 입력
                val extras: Bundle? = data?.extras
                // Bitmap으로 형변환
                val imageBitmap: Bitmap = extras?.get("data") as Bitmap
                // ImageView에 Bitmap으로 이미지를 입력
                ivEditUpload.setImageBitmap(imageBitmap)
                val options = BitmapFactory.Options()
                options.inSampleSize = 4

                val resized = Bitmap.createScaledBitmap(imageBitmap, 100, 100, true)
                encodeBitmapImg(resized)
                ivDelete.visibility = View.VISIBLE
                EditImgLine.visibility = View.VISIBLE
            }
        }
    }
}


