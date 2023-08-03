package com.smhrd.gmore.vo

import java.io.Serializable

data class BoardCategoryVO(
    var categoryTitle: String,
    var categoryNick: String,
    var categoryDate: String,
    var categoryLikeCnt: Int?,
    var board_id  : Int
) : Serializable
