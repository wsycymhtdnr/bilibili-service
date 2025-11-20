// GlobalExceptionAdvice refreshed
package com.bugmaker.bilibili.core

import com.bugmaker.bilibili.core.config.ExceptionCodeConfiguration
import com.bugmaker.bilibili.exception.HttpException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * author     : liyunfei
 * date       : 2025/11/18 02:12
 * description: 全局异常处理器
 */
@RestControllerAdvice
@Validated
class GlobalExceptionAdvice(
    private val exceptionCodeConfig: ExceptionCodeConfiguration
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /** 处理 HttpException - 业务异常 */
    @ExceptionHandler(HttpException::class)
    fun handleHttpException(
        e: HttpException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("HttpException: code=${e.code}, httpStatus=${e.httpStatusCode}, path=${request.requestURI}")
        val message = exceptionCodeConfig.getMessage(e.code)
        val response = ApiResponse.failure<Nothing>(e.code, message)
        val httpStatus = HttpStatus.valueOf(e.httpStatusCode)
        return ResponseEntity.status(httpStatus).body(response)
    }

    /** 处理 @RequestBody 参数校验异常 */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ApiResponse<Nothing> {
        logger.warn("Validation failed: path=${request.requestURI}")
        val errors = e.bindingResult.allErrors.joinToString("; ") { err ->
            when (err) {
                is FieldError -> "${err.field}: ${err.defaultMessage}"
                else -> "${err.objectName}: ${err.defaultMessage}"
            }
        }
        return ApiResponse.failure(400, "参数校验失败: $errors")
    }

    /** 处理url路径上的异常 */
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(
        e: ConstraintViolationException,
        request: HttpServletRequest
    ): ApiResponse<Nothing> {
        logger.warn("Constraint violations: path=${request.requestURI}")
        val errors = e.constraintViolations.joinToString("; ") { v ->
            val path = v.propertyPath?.toString() ?: ""
            "$path: ${v.message}"
        }
        return ApiResponse.failure(400, "参数校验失败: $errors")
    }

    /** 处理 @ModelAttribute / 表单参数绑定异常 */
    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBindException(
        e: BindException,
        request: HttpServletRequest
    ): ApiResponse<Nothing> {
        logger.warn("Bind exception: path=${request.requestURI}")
        val errors = e.bindingResult.allErrors.joinToString("; ") { err ->
            when (err) {
                is FieldError -> "${err.field}: ${err.defaultMessage}"
                else -> "${err.objectName}: ${err.defaultMessage}"
            }
        }
        return ApiResponse.failure(400, "参数绑定失败: $errors")
    }

    /** 处理参数类型不匹配 */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): ApiResponse<Nothing> {
        logger.warn("Type mismatch: parameter=${e.name}, path=${request.requestURI}")
        return ApiResponse.failure(400, "参数类型错误: ${e.name} 应该是 ${e.requiredType?.simpleName}")
    }

    /** 处理非法参数 */
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException,
        request: HttpServletRequest
    ): ApiResponse<Nothing> {
        logger.warn("Illegal argument: message=${e.message}, path=${request.requestURI}")
        return ApiResponse.failure(400, e.message ?: "非法参数")
    }

    /** 处理所有未捕获异常 */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnknownException(
        e: Exception,
        request: HttpServletRequest
    ): ApiResponse<Nothing> {
        logger.error(
            "Unhandled exception: type=${e::class.qualifiedName}, message=${e.message}, path=${request.requestURI}",
            e
        )
        val message = if (isProdEnv()) "服务器内部错误" else e.message ?: "未知错误"
        return ApiResponse.failure(500, message)
    }

    private fun isProdEnv(): Boolean {
        val env = System.getProperty("spring.profiles.active") ?: ""
        return env.contains("prod", ignoreCase = true)
    }
}