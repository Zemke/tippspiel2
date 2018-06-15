package com.github.zemke.tippspiel2.core.config

import org.springframework.web.filter.CommonsRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

class CommonsRequestLoggingFilterImpl : CommonsRequestLoggingFilter() {

    override fun beforeRequest(request: HttpServletRequest?, message: String?) {}

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun afterRequest(request: HttpServletRequest?, message: String?) {
        if (request != null && request.servletPath.startsWith("/api/")) {
            super.afterRequest(request, "method=${request.method};$message")
        }
    }
}
