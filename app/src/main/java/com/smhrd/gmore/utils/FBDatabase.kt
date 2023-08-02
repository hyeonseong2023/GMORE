package com.smhrd.gmore.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBDatabase {
    //realTime database 사용은 이 클래스를 통해서 진행
    companion object {
        val database = Firebase.database

        //        FBdatabase.getBoardRef()를 통해 호출
        fun getBoardRef(): DatabaseReference {
            return database.getReference("board")
        }

        fun getAllBoardRef(): DatabaseReference {
            return database.getReference("allboard")
        }

        fun getMemberRef(): DatabaseReference {
            return database.getReference("member")
        }

        fun getCommentRef(key: String): DatabaseReference {
            return database.getReference("comment").child(key)
        }

        fun getChatRef(key: String): DatabaseReference {
            return database.getReference("chat").child(key)
        }

        fun getBookmarkRef(): DatabaseReference {
            return database.getReference("bookmarkList")
        }

        fun getLikeRef(): DatabaseReference {
            return database.getReference("like")
        }


//        fun getContentRef(): DatabaseReference {
//            return database.getReference("content")
//        }
//
//        fun getBookmarkRef(): DatabaseReference {
//            return database.getReference("bookmarkList")
//        }
//        // database 인스턴스를 클래스마다 생성할 필요X

    }
}