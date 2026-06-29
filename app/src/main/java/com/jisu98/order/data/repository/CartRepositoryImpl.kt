package com.jisu98.order.data.repository

import com.jisu98.order.domain.model.CartItem
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor() : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getCartItems(): Flow<List<CartItem>> = _cartItems.asStateFlow()

    override fun getQuantity(menuId: Int): Int =
        _cartItems.value.find { it.menu.id == menuId }?.quantity ?: 0

    override fun add(menu: Menu) {
        _cartItems.update { items ->
            val existing = items.find { it.menu.id == menu.id }

            if (existing != null) {
                items.map { if (it.menu.id == menu.id) it.copy(quantity = it.quantity + 1) else it }
            } else {
                items + CartItem(menu = menu, quantity = 1)
            }
        }
    }

    override fun updateQuantity(menuId: Int, quantity: Int) {
        _cartItems.update { items ->
            items.map { if (it.menu.id == menuId) it.copy(quantity = quantity) else it }
        }
    }

    override fun remove(menuId: Int) {
        _cartItems.update { items -> items.filter { it.menu.id != menuId } }
    }

    override fun clear() {
        _cartItems.update { emptyList() }
    }
}
