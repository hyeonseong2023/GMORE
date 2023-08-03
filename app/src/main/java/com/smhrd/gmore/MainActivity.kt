package com.smhrd.gmore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smhrd.gmore.databinding.ActivityMainBinding
import com.smhrd.gmore.fragment.ChatRoomFragment
import com.smhrd.gmore.fragment.Fragment1
import com.smhrd.gmore.fragment.Fragment3
import com.smhrd.gmore.fragment.Fragment4

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // spf 가져오기
        var spf = getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        var loginedID = spf.getString("loginedId", "-")

        binding.tvTest112.text = loginedID

        // ------------------------------------------------------------------------------------------------------------------------------------------------


        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            Fragment1()
        ).commit()

        // bnv에서 선택한 메뉴에 따라 fl에 표시할 Fragment를 갈아 끼우기!
        binding.bnv.setOnItemSelectedListener {
            // Log.d("id", it.itemId.toString()) //2131231124 ~ 2131231127

            when(it.itemId) {
                R.id.tab1 -> {
                    // supportFragmentManager 활용 transaction 생성
                    // transaction을 통해 프래그먼트 교체 -> commit(완료)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment1()
                    ).commit()
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        ChatRoomFragment(loginedID.toString())
                    ).commit()
                }
                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment3()
                    ).commit()
                }
                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        Fragment4()
                    ).commit()
                }
            }
            // boolean : true(이벤트 인식이 더 좋음!) /false (이벤트 인식)
            true
        }




    }

}