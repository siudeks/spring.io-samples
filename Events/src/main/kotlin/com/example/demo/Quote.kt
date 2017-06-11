package com.example.demo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Quote {
    var id: Long? = null;
    var quote: String? = null;
}