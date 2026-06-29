package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    operator fun invoke(menuId: Int, delta: Int) {
        val newQty = cartRepository.getQuantity(menuId) + delta

        if (newQty <= 0) {
            cartRepository.remove(menuId)
        } else {
            cartRepository.updateQuantity(menuId, newQty)
        }
    }
}
