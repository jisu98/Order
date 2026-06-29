package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    operator fun invoke(menu: Menu) {
        if (menu.isSoldOut) return

        cartRepository.add(menu)
    }
}
