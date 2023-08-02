package com.smhrd.gmore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smhrd.gmore.databinding.ActivityLoginBinding
import com.smhrd.gmore.databinding.ActivityMainBinding

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




    }

}