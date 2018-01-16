package com.github.zemke.tippspiel2.entity.embeddable

import org.hibernate.validator.constraints.NotBlank
import javax.persistence.Embeddable

@Embeddable
data class FullName(

        @NotBlank val firstName: String,
        @NotBlank val lastName: String
)
