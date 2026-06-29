package com.jisu98.order.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {
    private lateinit var repository: MenuRepositoryImpl

    @Before
    fun setUp() {
        repository = MenuRepositoryImpl()
    }

    @Test
    fun `given repository when getMenus then return all dummy menus`() {
        val menus = repository.getMenus()

        assertEquals(12, menus.size)
    }

    @Test
    fun `given repository when getMenus then sold out menus have isSoldOut true`() {
        val menus = repository.getMenus()
        val soldOutMenus = menus.filter { it.isSoldOut }

        assertTrue(soldOutMenus.isNotEmpty())
        soldOutMenus.forEach { assertTrue(it.isSoldOut) }
    }

    @Test
    fun `given repository when getMenus then available menus have isSoldOut false`() {
        val menus = repository.getMenus()
        val availableMenus = menus.filter { !it.isSoldOut }

        assertTrue(availableMenus.isNotEmpty())
        availableMenus.forEach { assertFalse(it.isSoldOut) }
    }
}
