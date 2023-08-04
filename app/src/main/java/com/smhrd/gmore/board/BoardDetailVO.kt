package com.smhrd.gmore.board


class BoardDetailVO(val board_id: Int?,
                    val title: String,
                    val content: String,
                    val image_url: String?,
                    val category: String?,
                    val user_id : Int?,
                    val date_created: String?,
                    val nickname: String?
)

