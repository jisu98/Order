package com.jisu98.order.domain.model

data class Cart(
    val items: List<CartItem>,
    val totalPrice: Int,
)
