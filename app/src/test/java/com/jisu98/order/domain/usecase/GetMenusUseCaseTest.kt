package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.repository.MenuRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMenusUseCaseTest {
    private lateinit var menuRepository: MenuRepository
    private lateinit var useCase: GetMenusUseCase

    @Before
    fun setUp() {
        menuRepository = mockk()
        useCase = GetMenusUseCase(menuRepository)
    }

    @Test
    fun `given ALL category when invoke then return all menus`() {
        val menus = listOf(
            menu(id = 1, category = Category.BEVERAGE),
            menu(id = 2, category = Category.FOOD),
            menu(id = 3, category = Category.DESSERT),
        )
        every { menuRepository.getMenus() } returns menus

        val result = useCase(Category.ALL)

        assertEquals(menus, result)
    }

    @Test
    fun `given BEVERAGE category when invoke then return only beverage menus`() {
        val menus = listOf(
            menu(id = 1, category = Category.BEVERAGE),
            menu(id = 2, category = Category.FOOD),
            menu(id = 3, category = Category.BEVERAGE),
        )
        every { menuRepository.getMenus() } returns menus

        val result = useCase(Category.BEVERAGE)

        assertEquals(listOf(menus[0], menus[2]), result)
    }

    @Test
    fun `given category with no matching menus when invoke then return empty list`() {
        val menus = listOf(
            menu(id = 1, category = Category.BEVERAGE),
        )
        every { menuRepository.getMenus() } returns menus

        val result = useCase(Category.DESSERT)

        assertEquals(emptyList<Menu>(), result)
    }

    private fun menu(id: Int, category: Category) = Menu(
        id = id,
        name = "menu$id",
        price = 1000,
        category = category,
        isSoldOut = false,
    )
}
