package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.repository.CartRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PlaceOrderUseCaseTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var useCase: PlaceOrderUseCase

    @Before
    fun setUp() {
        cartRepository = mockk(relaxed = true)
        useCase = PlaceOrderUseCase(cartRepository)
    }

    @Test
    fun `given cart with items when invoke then clear cart`() {
        useCase()

        verify(exactly = 1) { cartRepository.clear() }
    }
}
