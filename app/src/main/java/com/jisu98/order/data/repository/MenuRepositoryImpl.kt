package com.jisu98.order.data.repository

import com.jisu98.order.data.model.MenuDto
import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.repository.MenuRepository
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor() : MenuRepository {
    override fun getMenus(): List<Menu> = dummyMenus.map { it.toDomain() }

    private fun MenuDto.toDomain(): Menu = Menu(
        id = id,
        name = name,
        price = price,
        category = Category.valueOf(category),
        isSoldOut = isSoldOut,
    )

    companion object {
        private val dummyMenus = listOf(
            MenuDto(1, "아메리카노", 3000, "BEVERAGE", false),
            MenuDto(2, "카페라떼", 4000, "BEVERAGE", false),
            MenuDto(3, "카푸치노", 4500, "BEVERAGE", true),
            MenuDto(4, "바닐라라떼", 4500, "BEVERAGE", false),
            MenuDto(5, "콜드브루", 4000, "BEVERAGE", false),
            MenuDto(6, "토스트", 3500, "FOOD", false),
            MenuDto(7, "샌드위치", 5000, "FOOD", false),
            MenuDto(8, "크로플", 4000, "FOOD", true),
            MenuDto(9, "치즈케이크", 5500, "DESSERT", false),
            MenuDto(10, "마카롱", 2000, "DESSERT", false),
            MenuDto(11, "티라미수", 5000, "DESSERT", true),
            MenuDto(12, "브라우니", 3500, "DESSERT", false),
        )
    }
}
