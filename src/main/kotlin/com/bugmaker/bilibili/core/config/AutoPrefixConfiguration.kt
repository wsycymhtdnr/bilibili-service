package com.bugmaker.bilibili.core.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method

/**
 * author     : liyunfei
 * date       : 2025/11/19 23:39
 * description: 自动前缀配置类
 * 根据控制器类的包名自动为请求映射添加前缀
 */
@Configuration
class AutoPrefixConfiguration : WebMvcRegistrations {

    @Value("\${bugmaker.package.name}")
    private lateinit var apiPackagePath: String

    override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return object : RequestMappingHandlerMapping() {
            override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
                val mappingInfo = super.getMappingForMethod(method, handlerType)
                return mappingInfo?.let {
                    val prefix = getPrefix(handlerType)
                    return RequestMappingInfo.paths(prefix).build().combine(it)
                }
            }

            private fun getPrefix(handlerType: Class<*>): String {
                val packageName = handlerType.`package`.name
                return packageName.replace(apiPackagePath, "")
                    .replace(".", "/")
            }
        }
    }
}

