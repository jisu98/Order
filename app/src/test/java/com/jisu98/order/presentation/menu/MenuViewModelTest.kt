package com.jisu98.order.presentation.menu

import com.jisu98.order.domain.model.Cart
import com.jisu98.order.domain.model.CartItem
import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.usecase.AddToCartUseCase
import com.jisu98.order.domain.usecase.GetCartUseCase
import com.jisu98.order.domain.usecase.GetMenusUseCase
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
class MenuViewModelTest {
    private lateinit var getMenusUseCase: GetMenusUseCase
    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var viewModel: MenuViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMenusUseCase = mockk()
        getCartUseCase = mockk()
        addToCartUseCase = mockk(relaxed = true)
        every { getMenusUseCase(any()) } returns emptyList()
        every { getCartUseCase() } returns flowOf(Cart(items = emptyList(), totalPrice = 0))
        viewModel = MenuViewModel(getMenusUseCase, getCartUseCase, addToCartUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given viewModel init when created then uiState is Success`() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is MenuUiState.Success)
    }

    @Test
    fun `given ALL category when init then load all menus`() = runTest {
        val menus = listOf(menu(id = 1, category = Category.BEVERAGE))
        every { getMenusUseCase(Category.ALL) } returns menus

        viewModel = MenuViewModel(getMenusUseCase, getCartUseCase, addToCartUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as MenuUiState.Success
        assertEquals(menus, state.menus)
    }

    @Test
    fun `given success state when selectCategory then menus filtered by category`() = runTest {
        val beverageMenus = listOf(menu(id = 1, category = Category.BEVERAGE))
        every { getMenusUseCase(Category.BEVERAGE) } returns beverageMenus
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.selectCategory(Category.BEVERAGE)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as MenuUiState.Success
        assertEquals(Category.BEVERAGE, state.selectedCategory)
        assertEquals(beverageMenus, state.menus)
    }

    @Test
    fun `given menu when addToCart then invoke addToCartUseCase`() = runTest {
        val menu = menu(id = 1, category = Category.BEVERAGE)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.addToCart(menu)

        verify(exactly = 1) { addToCartUseCase(menu) }
    }

    @Test
    fun `given cart with items when cart updates then uiState reflects cart`() = runTest {
        val cartItem = CartItem(menu = menu(id = 1, category = Category.BEVERAGE), quantity = 2)
        val cart = Cart(items = listOf(cartItem), totalPrice = 2000)
        every { getCartUseCase() } returns flowOf(cart)

        viewModel = MenuViewModel(getMenusUseCase, getCartUseCase, addToCartUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as MenuUiState.Success
        assertEquals(2, state.cartTotalCount)
        assertEquals(2000, state.cartTotalPrice)
    }

    private fun menu(id: Int, category: Category) = Menu(
        id = id,
        name = "menu$id",
        price = 1000,
        category = category,
        isSoldOut = false,
    )
}
