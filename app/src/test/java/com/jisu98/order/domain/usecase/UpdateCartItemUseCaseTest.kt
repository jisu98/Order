package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.repository.CartRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UpdateCartItemUseCaseTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var useCase: UpdateCartItemUseCase

    @Before
    fun setUp() {
        cartRepository = mockk(relaxed = true)
        useCase = UpdateCartItemUseCase(cartRepository)
    }

    @Test
    fun `given quantity above zero after delta when invoke then update quantity`() {
        every { cartRepository.getQuantity(1) } returns 3

        useCase(menuId = 1, delta = -1)

        verify(exactly = 1) { cartRepository.updateQuantity(1, 2) }
        verify(exactly = 0) { cartRepository.remove(any()) }
    }

    @Test
    fun `given quantity becomes zero after delta when invoke then remove item`() {
        every { cartRepository.getQuantity(1) } returns 1

        useCase(menuId = 1, delta = -1)

        verify(exactly = 1) { cartRepository.remove(1) }
        verify(exactly = 0) { cartRepository.updateQuantity(any(), any()) }
    }

    @Test
    fun `given quantity becomes negative after delta when invoke then remove item`() {
        every { cartRepository.getQuantity(1) } returns 1

        useCase(menuId = 1, delta = -5)

        verify(exactly = 1) { cartRepository.remove(1) }
        verify(exactly = 0) { cartRepository.updateQuantity(any(), any()) }
    }
}
