package com.smhrd.gmore.vo

import java.io.Serializable

data class BoardCategoryVO(
    var categoryTitle: String,
    var categoryNick: String,
    var categoryDate: String,
    var categoryLikeCnt: Int?,
<<<<<<< HEAD
    var board_id  : Int
=======
    var board_id : Int
>>>>>>> daun
) : Serializable
