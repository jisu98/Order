package com.jisu98.order.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import com.jisu98.order.domain.usecase.AddToCartUseCase
import com.jisu98.order.domain.usecase.GetCartUseCase
import com.jisu98.order.domain.usecase.GetMenusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenusUseCase: GetMenusUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MenuUiState>(MenuUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMenus()
        observeCart()
    }

    fun selectCategory(category: Category) {
        val current = _uiState.value as? MenuUiState.Success ?: return
        _uiState.update {
            current.copy(
                selectedCategory = category,
                menus = getMenusUseCase(category),
            )
        }
    }

    fun addToCart(menu: Menu) {
        addToCartUseCase(menu)
    }

    private fun loadMenus() {
        _uiState.value = MenuUiState.Success(
            selectedCategory = Category.ALL,
            menus = getMenusUseCase(Category.ALL),
        )
    }

    private fun observeCart() {
        getCartUseCase().onEach { cartItems ->
            val current = _uiState.value as? MenuUiState.Success ?: return@onEach
            _uiState.update { current.copy(cartItems = cartItems) }
        }.launchIn(viewModelScope)
    }
}
