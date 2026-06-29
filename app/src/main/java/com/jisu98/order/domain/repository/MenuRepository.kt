package com.jisu98.order.domain.repository

import com.jisu98.order.domain.model.Menu

interface MenuRepository {
    fun getMenus(): List<Menu>
}
