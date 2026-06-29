package com.jisu98.order.presentation.order

import com.jisu98.order.domain.model.Cart
import com.jisu98.order.domain.model.CartItem
import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.usecase.GetCartUseCase
import com.jisu98.order.domain.usecase.PlaceOrderUseCase
import com.jisu98.order.domain.usecase.UpdateCartItemUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderViewModelTest {
    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var updateCartItemUseCase: UpdateCartItemUseCase
    private lateinit var placeOrderUseCase: PlaceOrderUseCase
    private lateinit var viewModel: OrderViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCartUseCase = mockk()
        updateCartItemUseCase = mockk(relaxed = true)
        placeOrderUseCase = mockk(relaxed = true)
        every { getCartUseCase() } returns flowOf(Cart(items = emptyList(), totalPrice = 0))
        viewModel = OrderViewModel(getCartUseCase, updateCartItemUseCase, placeOrderUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given cart flow when init then uiState is Success`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is OrderUiState.Success)
    }

    @Test
    fun `given cart with items when init then uiState reflects cart`() = runTest {
        val cartItem = CartItem(menu = menu(id = 1, price = 3000), quantity = 2)
        val cart = Cart(items = listOf(cartItem), totalPrice = 6000)
        every { getCartUseCase() } returns flowOf(cart)

        viewModel = OrderViewModel(getCartUseCase, updateCartItemUseCase, placeOrderUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as OrderUiState.Success
        assertEquals(1, state.cartItems.size)
        assertEquals(6000, state.totalPrice)
    }

    @Test
    fun `given menu id when increaseQuantity then invoke updateCartItemUseCase with delta 1`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.increaseQuantity(menuId = 1)

        verify(exactly = 1) { updateCartItemUseCase(menuId = 1, delta = 1) }
    }

    @Test
    fun `given menu id when decreaseQuantity then invoke updateCartItemUseCase with delta -1`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.decreaseQuantity(menuId = 1)

        verify(exactly = 1) { updateCartItemUseCase(menuId = 1, delta = -1) }
    }

    @Test
    fun `given cart with items when placeOrder then invoke placeOrderUseCase`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.placeOrder()

        verify(exactly = 1) { placeOrderUseCase() }
    }

    private fun menu(id: Int, price: Int) = Menu(
        id = id,
        name = "menu$id",
        price = price,
        category = Category.BEVERAGE,
        isSoldOut = false,
    )
}
