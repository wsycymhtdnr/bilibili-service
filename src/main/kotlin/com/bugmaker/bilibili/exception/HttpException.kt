package com.bugmaker.bilibili.exception

/**
 * author     : liyunfei
 * date       : 2025/11/18 01:53
 * description: Http错误异常
 * @property code 业务错误码
 * @property httpStatusCode HTTP状态码
 */
sealed class HttpException(
    open val code: Int,
    open val httpStatusCode: Int
) : RuntimeException() {

    // 4xx 客户端错误
    data class ParameterException(override val code: Int) : HttpException(code, 400)
    data class UnAuthenticatedException(override val code: Int) : HttpException(code, 401)
    data class ForbiddenException(override val code: Int) : HttpException(code, 403)
    data class NotFoundException(override val code: Int) : HttpException(code, 404)

    // 5xx 服务器错误
    data class ServerErrorException(override val code: Int) : HttpException(code, 500)
}