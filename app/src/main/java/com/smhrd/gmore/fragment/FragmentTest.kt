package com.smhrd.gmore.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smhrd.gmore.R

class FragmentTest : AppCompatActivity() {

    lateinit var bnv : BottomNavigationView
    lateinit var fl : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_test)

        bnv = findViewById(R.id.bnv)
        fl = findViewById(R.id.fl)

        bnv.setOnItemSelectedListener {
            Log.d("id", it.itemId.toString())

            // when = switch 문과 유사
            when (it.itemId) {
                //supportFragmentManager활용 transaction 생성
                //transacrion을 통해 프래그먼트 교체 -> commit(와료)
                // it.itemId(클린한 id값) 과   R.id.tab1 값이 같으면...
                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction().replace(
                        // fl 구간에 Fragment1() 보이게 하겠다
                        R.id.fl,
                        Fragment1()
                    ).commit()
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        // fl 구간에 Fragment1() 보이게 하겠다
                        R.id.fl,
                        Fragment2()
                    ).commit()
                }
                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        // fl 구간에 Fragment1() 보이게 하겠다
                        R.id.fl,
                        Fragment3()
                    ).commit()
                }
                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        // fl 구간에 Fragment1() 보이게 하겠다
                        R.id.fl,
                        Fragment4()
                    ).commit()
                }
            }
            //setOnItemSelectedListener 안에는 무조건 boolean 타입 적어 줘야 함
            // false - 로 기본값을 주면 이벤트 인식을 잘 못 함
            // true 가 이벤트 인식 효율이 더 좋다
            true
        }

    }
}