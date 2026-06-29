package com.jisu98.order.domain.repository

import com.jisu98.order.domain.model.CartItem
import com.jisu98.order.domain.model.Menu
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun getQuantity(menuId: Int): Int
    fun add(menu: Menu)
    fun updateQuantity(menuId: Int, quantity: Int)
    fun remove(menuId: Int)
    fun clear()
}
