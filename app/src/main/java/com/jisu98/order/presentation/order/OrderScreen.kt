package com.jisu98.order.presentation.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jisu98.order.domain.model.CartItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderScreen(
    onOrderComplete: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is OrderUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        is OrderUiState.Success -> OrderContent(
            state = state,
            onIncrease = viewModel::increaseQuantity,
            onDecrease = viewModel::decreaseQuantity,
            onPlaceOrder = {
                viewModel.placeOrder()
                onOrderComplete()
            },
        )

        is OrderUiState.Error -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = state.message, color = Color.Red)
        }
    }
}

@Composable
private fun OrderContent(
    state: OrderUiState.Success,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onPlaceOrder: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.cartItems, key = { it.menu.id }) { cartItem ->
                CartItemRow(
                    cartItem = cartItem,
                    onIncrease = { onIncrease(cartItem.menu.id) },
                    onDecrease = { onDecrease(cartItem.menu.id) },
                )
                HorizontalDivider()
            }
        }
        TotalPriceBar(
            totalPrice = state.totalPrice,
            onPlaceOrder = onPlaceOrder,
        )
    }
}

@Composable
private fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cartItem.menu.name, fontWeight = FontWeight.Medium)
            Text(
                text = formatPrice(cartItem.menu.price * cartItem.quantity),
                fontSize = 14.sp,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease) { Text("-") }
            Text(text = "${cartItem.quantity}", fontWeight = FontWeight.Bold)
            IconButton(onClick = onIncrease) { Text("+") }
        }
    }
}

@Composable
private fun TotalPriceBar(
    totalPrice: Int,
    onPlaceOrder: () -> Unit,
) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "합계 ${formatPrice(totalPrice)}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Button(onClick = onPlaceOrder) {
            Text("주문 완료")
        }
    }
}

private fun formatPrice(price: Int): String =
    NumberFormat.getNumberInstance(Locale.KOREA).format(price) + "원"
