package com.bugmaker.bilibili.api.v1

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated

@Validated
@RestController
class TestController {

    @GetMapping("/test/{id}")
    fun test(
        @PathVariable @Min(1) id: Long,
        @RequestParam("name") @NotBlank @Size(min = 2, max = 30) name: String
    ): String {
        return "ok"
    }

    // 示例 1: 触发 BindException（@ModelAttribute 绑定，不使用 @Valid）- GET 方式（query string）
    data class BindForm(
        @field:NotBlank(message = "name 不能为空")
        val name: String = "",

        @field:Min(value = 1, message = "count 最小为 1")
        val count: Int = 0
    )

    @GetMapping("/test/bind")
    fun bindExample(@ModelAttribute form: BindForm): String {
        // 当类型转换失败（例如 count=abc）或绑定错误时，会抛出 BindException（未声明 BindingResult）
        return "bind ok: ${form.name}, ${form.count}"
    }

    // 示例 1 补充：POST 表单方式，强制使用 application/x-www-form-urlencoded，确保不是 @RequestBody 路径
    @PostMapping(
        value = ["/test/bind"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun bindPost(@ModelAttribute form: BindForm): String {
        return "bind post ok: ${form.name}, ${form.count}"
    }

    // 示例 2: 触发 MethodArgumentTypeMismatchException
    @GetMapping("/test/type-mismatch")
    fun typeMismatchExample(@RequestParam("id") id: Long): String {
        return "id=$id"
    }

    // 示例 3: 手动抛出 IllegalArgumentException
    @GetMapping("/test/illegal")
    fun illegalArgExample(@RequestParam("name") name: String?): String {
        if (name == null || name.isBlank()) {
            throw IllegalArgumentException("name 不能为空")
        }
        if (name == "bad") {
            throw IllegalArgumentException("不合法的 name 值: $name")
        }
        return "hello $name"
    }
}