package com.smhrd.gmore.vo

class RQMember (
    val id: Int,
    val nick : String
    )

data class MembersResponse(
    val members: List<RQMember>
)