package com.smhrd.gmore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.smhrd.gmore.board.GameCategoryActivity

class HomeActivity : AppCompatActivity() {

    lateinit var btn1: Button
    lateinit var btn2: Button
    lateinit var tvNick : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        tvNick = findViewById(R.id.tvNick)

        btn1.setOnClickListener {
            var it_next: Intent =
                Intent(this, GameCategoryActivity::class.java) // 현재 액티비티(첫번째 메인), 두번째 받는 사람
            it_next.putExtra("buttonText", btn1.text.toString())
            startActivity(it_next)
        }

        btn2.setOnClickListener {
            var it_next: Intent =
                Intent(this, GameCategoryActivity::class.java) // 현재 액티비티(첫번째 메인), 두번째 받는 사람
            it_next.putExtra("buttonText", btn2.text.toString())
            startActivity(it_next)
        }


        tvNick.text =  intent.getIntExtra("selected_board_id", -1).toString()

    }
}