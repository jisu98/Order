package com.jisu98.order.data.model

data class MenuDto(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String,
    val isSoldOut: Boolean,
)
