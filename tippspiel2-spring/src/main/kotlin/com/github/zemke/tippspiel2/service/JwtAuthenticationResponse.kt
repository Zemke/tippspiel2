package com.github.zemke.tippspiel2.service

import java.io.Serializable

/**
 * Created by stephan on 20.03.16.
 */
class JwtAuthenticationResponse(val token: String) : Serializable {

    companion object {
        private const val serialVersionUID = 1250166508152483573L
    }
}
