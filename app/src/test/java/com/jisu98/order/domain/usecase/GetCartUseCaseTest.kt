package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.model.Cart
import com.jisu98.order.domain.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCartUseCaseTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var useCase: GetCartUseCase

    @Before
    fun setUp() {
        cartRepository = mockk()
        useCase = GetCartUseCase(cartRepository)
    }

    @Test
    fun `given cart in repository when invoke then return cart flow`() = runTest {
        val cart = Cart(items = emptyList(), totalPrice = 0)
        every { cartRepository.getCart() } returns flowOf(cart)

        val result = useCase().first()

        assertEquals(cart, result)
    }
}
