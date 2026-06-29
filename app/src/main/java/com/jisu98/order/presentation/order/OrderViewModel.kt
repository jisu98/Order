package com.jisu98.order.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jisu98.order.domain.usecase.GetCartUseCase
import com.jisu98.order.domain.usecase.PlaceOrderUseCase
import com.jisu98.order.domain.usecase.UpdateCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<OrderUiState>(OrderUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observeCart()
    }

    fun increaseQuantity(menuId: Int) {
        updateCartItemUseCase(menuId, delta = 1)
    }

    fun decreaseQuantity(menuId: Int) {
        updateCartItemUseCase(menuId, delta = -1)
    }

    fun placeOrder() {
        placeOrderUseCase()
    }

    private fun observeCart() {
        getCartUseCase().onEach { cart ->
            _uiState.update {
                OrderUiState.Success(
                    cartItems = cart.items,
                    totalPrice = cart.totalPrice,
                )
            }
        }.launchIn(viewModelScope)
    }
}
