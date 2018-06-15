package com.github.zemke.tippspiel2.core.config

import org.springframework.web.filter.CommonsRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

class CommonsRequestLoggingFilterImpl : CommonsRequestLoggingFilter() {

    override fun beforeRequest(request: HttpServletRequest?, message: String?) {}
}
