package com.jisu98.order.presentation.order

import com.jisu98.order.domain.model.CartItem

sealed class OrderUiState {
    data object Loading : OrderUiState()

    data class Success(
        val cartItems: List<CartItem> = emptyList(),
    ) : OrderUiState() {
        val totalPrice: Int get() = cartItems.sumOf { it.menu.price * it.quantity }
    }

    data class Error(val message: String) : OrderUiState()
}
