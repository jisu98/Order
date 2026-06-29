package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.repository.CartRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    operator fun invoke() {
        cartRepository.clear()
    }
}
