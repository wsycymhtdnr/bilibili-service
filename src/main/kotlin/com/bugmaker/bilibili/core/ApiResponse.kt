package com.bugmaker.bilibili.core

/**
 * author     : liyunfei
 * date       : 2025/11/18 02:15
 * description: 统一的 API 响应格式
 * @param T 响应数据的类型
 * @property code 业务状态码，0 表示成功，非 0 表示各种业务错误
 * @property message 响应消息
 * @property data 响应数据
 */
data class ApiResponse<T>(
    val code: Int = 0,
    val message: String = "success",
    val data: T? = null
) {
    companion object {
        /**
         * 成功响应
         */
        fun <T> success(data: T? = null, message: String = "操作成功"): ApiResponse<T> {
            return ApiResponse(code = 0, message = message, data = data)
        }

        /**
         * 失败响应
         */
        fun <T> failure(code: Int, message: String): ApiResponse<T> {
            return ApiResponse(code = code, message = message, data = null)
        }
    }
}