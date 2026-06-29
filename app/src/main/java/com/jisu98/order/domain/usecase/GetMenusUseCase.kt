package com.jisu98.order.domain.usecase

import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.repository.MenuRepository
import javax.inject.Inject

class GetMenusUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    operator fun invoke(category: Category): List<Menu> {
        val menus = menuRepository.getMenus()

        return if (category == Category.ALL) menus
        else menus.filter { it.category == category }
    }
}
