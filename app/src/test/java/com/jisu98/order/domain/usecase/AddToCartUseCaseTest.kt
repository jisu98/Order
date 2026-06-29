package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.repository.CartRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AddToCartUseCaseTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var useCase: AddToCartUseCase

    @Before
    fun setUp() {
        cartRepository = mockk(relaxed = true)
        useCase = AddToCartUseCase(cartRepository)
    }

    @Test
    fun `given available menu when invoke then add to cart`() {
        val menu = menu(isSoldOut = false)

        useCase(menu)

        verify(exactly = 1) { cartRepository.add(menu) }
    }

    @Test
    fun `given sold out menu when invoke then do not add to cart`() {
        val menu = menu(isSoldOut = true)

        useCase(menu)

        verify(exactly = 0) { cartRepository.add(any()) }
    }

    private fun menu(isSoldOut: Boolean) = Menu(
        id = 1,
        name = "아메리카노",
        price = 3000,
        category = Category.BEVERAGE,
        isSoldOut = isSoldOut,
    )
}
