package com.jisu98.order.data.repository

import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {
    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setUp() {
        repository = CartRepositoryImpl()
    }

    @Test
    fun `given empty cart when getCart then return empty items`() = runTest {
        val cart = repository.getCart().first()

        assertTrue(cart.items.isEmpty())
        assertEquals(0, cart.totalPrice)
    }

    @Test
    fun `given new menu when add then item added with quantity 1`() = runTest {
        val menu = menu(id = 1, price = 3000)

        repository.add(menu)
        val cart = repository.getCart().first()

        assertEquals(1, cart.items.size)
        assertEquals(1, cart.items[0].quantity)
    }

    @Test
    fun `given existing menu when add then increment quantity`() = runTest {
        val menu = menu(id = 1, price = 3000)
        repository.add(menu)

        repository.add(menu)
        val cart = repository.getCart().first()

        assertEquals(1, cart.items.size)
        assertEquals(2, cart.items[0].quantity)
    }

    @Test
    fun `given cart with items when getCart then return correct totalPrice`() = runTest {
        repository.add(menu(id = 1, price = 3000))
        repository.add(menu(id = 1, price = 3000))
        repository.add(menu(id = 2, price = 4000))

        val cart = repository.getCart().first()

        assertEquals(10000, cart.totalPrice)
    }

    @Test
    fun `given item in cart when updateQuantity then quantity updated`() = runTest {
        val menu = menu(id = 1, price = 3000)
        repository.add(menu)

        repository.updateQuantity(menuId = 1, quantity = 5)
        val cart = repository.getCart().first()

        assertEquals(5, cart.items[0].quantity)
    }

    @Test
    fun `given item in cart when remove then item removed`() = runTest {
        repository.add(menu(id = 1, price = 3000))
        repository.add(menu(id = 2, price = 4000))

        repository.remove(menuId = 1)
        val cart = repository.getCart().first()

        assertEquals(1, cart.items.size)
        assertEquals(2, cart.items[0].menu.id)
    }

    @Test
    fun `given cart with items when clear then cart is empty`() = runTest {
        repository.add(menu(id = 1, price = 3000))
        repository.add(menu(id = 2, price = 4000))

        repository.clear()
        val cart = repository.getCart().first()

        assertTrue(cart.items.isEmpty())
        assertEquals(0, cart.totalPrice)
    }

    @Test
    fun `given item in cart when getQuantity then return correct quantity`() = runTest {
        val menu = menu(id = 1, price = 3000)
        repository.add(menu)
        repository.add(menu)

        val quantity = repository.getQuantity(menuId = 1)

        assertEquals(2, quantity)
    }

    @Test
    fun `given no item in cart when getQuantity then return zero`() {
        val quantity = repository.getQuantity(menuId = 99)

        assertEquals(0, quantity)
    }

    private fun menu(id: Int, price: Int) = Menu(
        id = id,
        name = "menu$id",
        price = price,
        category = Category.BEVERAGE,
        isSoldOut = false,
    )
}
