package com.jisu98.order.presentation.menu

import com.jisu98.order.domain.model.CartItem
import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu

sealed class MenuUiState {
    data object Loading : MenuUiState()

    data class Success(
        val selectedCategory: Category = Category.ALL,
        val menus: List<Menu> = emptyList(),
        val cartItems: List<CartItem> = emptyList(),
    ) : MenuUiState() {
        val cartTotalCount: Int get() = cartItems.sumOf { it.quantity }
        val cartTotalPrice: Int get() = cartItems.sumOf { it.menu.price * it.quantity }
    }

    data class Error(val message: String) : MenuUiState()
}
