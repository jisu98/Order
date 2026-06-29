package com.jisu98.order.presentation.order

import com.jisu98.order.domain.model.CartItem

sealed class OrderUiState {
    data object Loading : OrderUiState()

    data class Success(
        val cartItems: List<CartItem> = emptyList(),
        val totalPrice: Int = 0,
    ) : OrderUiState()

    data class Error(val message: String) : OrderUiState()
}
