package com.bugmaker.bilibili.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

/**
 * author     : liyunfei
 * date       : 2025/11/19
 * description: 异常代码配置类
 * 用于加载 exception.properties 文件中的异常代码和消息映射
 */
@Component
@PropertySource("classpath:config/exception.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "exception")
data class ExceptionCodeConfiguration(
    var codes: Map<Int, String> = emptyMap()
) {
    /**
     * 根据异常代码获取异常消息
     * @param code 异常代码
     * @return 异常消息，如果找不到则返回默认消息
     */
    fun getMessage(code: Int): String {
        return codes[code] ?: "未知错误"
    }
}