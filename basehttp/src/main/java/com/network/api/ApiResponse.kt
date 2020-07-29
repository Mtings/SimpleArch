package com.network.api

import androidx.annotation.Keep

@Keep
data class ApiResponse<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)