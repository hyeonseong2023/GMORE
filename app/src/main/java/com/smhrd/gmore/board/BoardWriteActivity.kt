package com.smhrd.gmore.board

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smhrd.gmore.R
//import com.smhrd.gmore.databinding.ActivityBoardWriteBinding
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
class BoardWriteActivity : AppCompatActivity() {

    lateinit var btnWriteClose : ImageButton
    lateinit var btnWriteOk : ImageButton
    lateinit var etWriteTitle : EditText
    lateinit var etWriteContent : EditText
    lateinit var btnWritePhoto : ImageButton
    lateinit var btnWriteCam : ImageButton
    lateinit var ivUpload : ImageView

    lateinit var writeImgLine : View
    lateinit var reqQueue : RequestQueue
    lateinit var encodeImgString : String
    val STORAGE_CODE = 1000

    var imgCamUpload  = false
    var imgPhotoUpload = false

    // 작성 중인 게시글 닫을 때 알림창 확인 클릭했을 때
    val dialogListener = DialogInterface.OnClickListener { dialogInterface, i ->
        // 💡 코드 합치고 나면 finish()가 아닌 게시글 리스트로 돌아가기!
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)


        btnWriteClose = findViewById(R.id.btnWriteClose)
        btnWriteOk = findViewById(R.id.btnWriteOk)
        btnWriteCam = findViewById(R.id.btnWriteCam)
        btnWritePhoto = findViewById(R.id.btnWritephoto)
        etWriteTitle = findViewById(R.id.etWriteTitle)
        etWriteContent = findViewById(R.id.etWriteContent)
        ivUpload = findViewById(R.id.ivUpload)
        writeImgLine = findViewById(R.id.writeImgLine)

        reqQueue = Volley.newRequestQueue(this@BoardWriteActivity)


        // 작성 중인 게시글 닫기 버튼
        btnWriteClose.setOnClickListener {
            // 제목과 내용에 적힌 글이 없다면
            if(etWriteTitle.text.toString() == "" && etWriteContent.text.toString() == ""){
                // 💡 코드 합치고 나면 finish()가 아닌 게시글 리스트로 돌아가기!
                finish()
            }else{  // 제목이나 내용에 글이 적혀있다면 알림창 띄우기
                val builder : AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("작성 중인 글을 취소하시겠습니까? 확인 선택 시, 작성된 글은 저장되지 않습니다.")
                builder.setPositiveButton("확인", dialogListener)
                builder.setNegativeButton("취소", null)
                builder.create().show()
            }
        }


        // 작성한 게시글 업로드 버튼
        btnWriteOk.setOnClickListener {
            // 게임카테고리, 제목, 내용, 작성자, 이미지, 작성일
            val inputTitle = etWriteTitle.text.toString()
            val inputContent = etWriteContent.text.toString()

            val request = object : StringRequest(
                Request.Method.POST,
                "http://172.30.1.29:8888/board/write",
                { response ->
                    Log.d("response", response.toString())
                },
                { error ->
                    Log.d("error", error.toString())
                }
            ){
//                override fun getParams(): MutableMap<String, String>? {
//                    return params
//                }
            }
            reqQueue.add(request)
        }


        // 갤러리에서 이미지 가져오기 버튼
        btnWritePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, STORAGE_CODE)
        }


        // 카메라 버튼
        btnWriteCam.setOnClickListener{

    }
    }


    // startCamera() : 카메라를 실행하고 미리보기를 보여주는 역할




        // 갤러리 화면에서 이미지를 선택한 경우 현재 화면 보여주기
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            when (requestCode) {
                STORAGE_CODE -> {
                    // image에 대한 uri 가져오기
                    val selectedImgUri = data?.data
                    if (selectedImgUri != null) {
                        // uri -> bitmap으로 변환
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImgUri)
                        ivUpload.setImageBitmap(bitmap)

                        val options = BitmapFactory.Options()
                        options.inSampleSize = 4

                        val resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
                        encodeBitmapImg(resized)
                        writeImgLine.visibility = View.VISIBLE
                    }
                }
            }
        }

        // bitmap -> String (Base64)
        private fun encodeBitmapImg(bitmap: Bitmap) {
            // ByteArray 생성할 수 있는 Stream
            val byteArrayOutputStream = ByteArrayOutputStream()
            // bitmap 압축
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            // img -> Array
            val bytesOfImg = byteArrayOutputStream.toByteArray()
            encodeImgString = Base64.encodeToString(bytesOfImg, Base64.DEFAULT)
        }

}

