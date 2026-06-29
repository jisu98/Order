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
        val cartTotalCount: Int = 0,
        val cartTotalPrice: Int = 0,
    ) : MenuUiState()

    data class Error(val message: String) : MenuUiState()
}
