package com.jstarczewski.ewaha

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class Locationn(
    val localization: String?,
    val prices: Prices? = null
)