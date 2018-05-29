package com.github.zemke.tippspiel2.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IndexController {

    @RequestMapping(path = ["/**/{[path:[^\\.]*}"])
    fun redirect(): String {
        return "forward:/"
    }
}
