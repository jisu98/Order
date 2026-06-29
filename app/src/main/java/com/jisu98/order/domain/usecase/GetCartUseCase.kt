package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.model.CartItem
import com.jisu98.order.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    operator fun invoke(): Flow<List<CartItem>> = cartRepository.getCartItems()
}
