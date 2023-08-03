package com.smhrd.gmore.vo

class RQMember (
    val id: Int,
    val nick : String
    // 혜주 추가
   , val eamil : String
    )

data class MembersResponse(
    val members: List<RQMember>
)