package com.dyotakt.hawaldar

import kotlinx.serialization.Serializable

@Serializable
data class items (
    var name: String,
    val id: String,
    var data : String,
    val backgroundColor: String,
    var otp: Int
)