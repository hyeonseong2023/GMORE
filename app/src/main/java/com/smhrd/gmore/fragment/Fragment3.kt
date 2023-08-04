package com.smhrd.gmore.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.smhrd.gmore.R
import com.smhrd.gmore.fragment.Fragment3VO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class Fragment3 : Fragment() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    var login_id:  String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화 및 빈 어댑터 설정
        rvFavorites = view.findViewById(R.id.rv_favorites)
        rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        favoriteAdapter = FavoriteAdapter(emptyList())
        rvFavorites.adapter = favoriteAdapter
        val spf = requireActivity().getSharedPreferences("userSPF", Context.MODE_PRIVATE)
        login_id = spf.getString("userId", "1")
        // 데이터 가져오기
        fetchFavorites()

    }

    private fun fetchFavorites() {
        thread {
            try {

                val urlString = "http://172.30.1.11:8888/favorites/${login_id}"
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
                val favorites =
                    gson.fromJson(response.toString(), Array<Fragment3VO>::class.java).toList()

                // 가져온 데이터로 RecyclerView 어댑터 업데이트
                activity?.runOnUiThread {
                    favoriteAdapter.updateDataSet(favorites)
                }

            } catch (e: Exception) {
                Log.e("Fetch Favorites", "Error fetching favorite list: ${e.message}", e)
            }
        }
    }
}

