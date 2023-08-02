package com.smhrd.gmore.fragment
// MyPage - 프로필 사진 수정 / 내 글목록 조회 / 로그아웃
import android.app.Activity
import android.content.Context

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle

import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

import com.smhrd.gmore.R
import com.smhrd.gmore.user.EditProfileActivity
import com.smhrd.gmore.user.User_VO
import java.io.ByteArrayOutputStream

class Fragment4 : Fragment() {
    lateinit var imgProfileMypage: ImageView
    lateinit var tvIdMypage: TextView
    lateinit var tvNickMypage: TextView
    lateinit var btnEditMypage: Button
    lateinit var btnBoardListMypage: Button
    lateinit var btnLogoutMypage: Button
    lateinit var userId:String

    // Volley 사용
    lateinit var reqQueue: RequestQueue

    // 가져온img Uri 전역 변수로 사용해야 할 경우 사용할 변수
    lateinit var getProfileImg: Uri

    // 갤러리 이미지 가져오기 위한 변수
    val STORAGE_CODE = 1000

    // 이미지 인코딩 하기 위한 변수
    lateinit var encodeImgString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_4, container, false)

        imgProfileMypage = view.findViewById(R.id.imgProfileMypage)
        tvIdMypage = view.findViewById(R.id.tvIdMypage)
        tvNickMypage = view.findViewById(R.id.tvNickMypage)
        btnEditMypage = view.findViewById(R.id.btnSaveEditProfile)
        btnBoardListMypage = view.findViewById(R.id.btnBoardListMypage)
        btnLogoutMypage = view.findViewById(R.id.btnLogoutMypage)



        // ✨ SharedPreference 생성
        //url 값 저장 (SharedPreference -> 내부 메모리)
        val spf = requireActivity().getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        //MODE_PRIVATE : 내부 캐시에 저장 --> 노출X

        // 저장 - editor 사용
        val editor = spf.edit()
        editor.putString("userId","user1@example.com")
        editor.commit()



        // spf 에서 user id 가져와 textView에 출력
        userId = spf.getString("userId", "")!!






        // 쓰기기 /읽기 권한 설정 확인용 변수 선언
        var writePermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        var readPermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // 프로필 사진 변경
        imgProfileMypage.setOnClickListener {

            // 권한 설정 확인
            // 읽기 쓰기 권한 설정 X 라면
            if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions( // 읽기 쓰기 권한 요청
                    // Fragment는 Context가 this가 아님! requireActivity() 임
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    1
                )

            }
            // 읽기 쓰기 권한 설정이 되어있다면
            else {

                // 방법 2
                val it = Intent(Intent.ACTION_GET_CONTENT)
                it.type = "image/*"
                startActivityForResult(it, STORAGE_CODE)

            }


        }


        // 회원정보 수정 페이지로 이동
        btnEditMypage.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)

        }


        // 로그아웃
        btnLogoutMypage.setOnClickListener {

        }
        return view
    }

    // onCreate 밖
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Volley 설정
        reqQueue = Volley.newRequestQueue(requireActivity())

        when (requestCode) {
            STORAGE_CODE -> {
                // image uri 가져오기
                // 앞에 쓴 data = 매개변수 data: Intent?의 data
                val selectedImgUri = data?.data
                if (selectedImgUri != null) {
                    // uri를 bitmap 형태로 변환 하면 -> 에뮬레이터에 이미지 가져올 수 있음
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImgUri
                    )
                    imgProfileMypage.setImageBitmap(bitmap)

                    // 아래(fun encodeBitmapImg)서 가공한 인코딩 데이터 다시 용량 줄여주기
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 4 // 4분의 1 크기로 변환

                    // filter:true => 선명 false =? 흐릿
                    val resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true)

                    encodeBitmapImg(resized)


                }
            }

        }

        // 변경한 프로필 이미지 node로 전송
        val request = object : StringRequest(
            Request.Method.POST,
            "http://172.30.1.40:8888/user/updateimg",
            { response ->
                Log.d("response", response.toString())

            }, { error ->
                Log.d("error", error.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {

                val params: MutableMap<String, String> = HashMap<String, String>()

                val imgMypage = encodeImgString

                val myPageData = User_VO(null, userId,null, null,null,imgMypage)

                params.put("myPageData", Gson().toJson(myPageData))
                Log.d("이미지전송데이터 ?", imgMypage.toString())
                return params
            }
        }
        reqQueue.add(request)
    }



// bitmap -> String()  ==> 노드로 이미지 데이터 넘기기 위한 작업
private  fun encodeBitmapImg(bitmap: Bitmap){

    // 이미지 문자열 들이 들어갈 ByteArray 생성
    val byteArrayOutputStream = ByteArrayOutputStream()
    // 받아온 bitmap을 압축 시키기 (문자열이 너무 크면 오류가 나기도 함)
    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)

    // 이미지 배열 형태로 가공
    val byteOfImg = byteArrayOutputStream.toByteArray()

    //  byteArray에 든 데이터 문자열로 인코딩 (base64)
    encodeImgString = Base64.encodeToString(byteOfImg, Base64.DEFAULT)

    // 가공한 데이터 onActivityResult 메서드에서 사용
}


}




