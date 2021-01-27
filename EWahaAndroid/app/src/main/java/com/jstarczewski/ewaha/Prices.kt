package com.jstarczewski.ewaha

import com.fasterxml.jackson.annotation.JsonProperty

data class Prices(
    val ON: Float? = null,
    @JsonProperty("95")
    val benzo95: Float? = null,
    @JsonProperty("98")
    val benzo98: Float? = null
) {
    override fun toString(): String {
        var prices = ""
        val b95 = benzo95
        val b98 = benzo98
        val on = ON
        if (b95 != null) {
            prices += "95 = $b95"
        }
        if (b98 != null) {
            prices += " 98 = $b98"
        }
        if (on != null) {
            prices += " ON = $on"
        }
        return prices
    }
}