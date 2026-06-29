package com.jisu98.order.presentation.menu

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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import com.jisu98.order.domain.model.Category
import com.jisu98.order.domain.model.Menu
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuScreen(
    onNavigateToOrder: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MenuUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        is MenuUiState.Success -> MenuContent(
            state = state,
            onCategorySelected = viewModel::selectCategory,
            onMenuClick = viewModel::addToCart,
            onOrderClick = onNavigateToOrder,
        )

        is MenuUiState.Error -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = state.message, color = Color.Red)
        }
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState.Success,
    onCategorySelected: (Category) -> Unit,
    onMenuClick: (Menu) -> Unit,
    onOrderClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CategoryTabs(
            selectedCategory = state.selectedCategory,
            onCategorySelected = onCategorySelected,
        )
        MenuList(
            menus = state.menus,
            onMenuClick = onMenuClick,
            modifier = Modifier.weight(1f),
        )
        CartSummaryBar(
            totalCount = state.cartTotalCount,
            totalPrice = state.cartTotalPrice,
            onOrderClick = onOrderClick,
        )
    }
}

@Composable
private fun CategoryTabs(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
) {
    val categories = Category.entries
    ScrollableTabRow(selectedTabIndex = categories.indexOf(selectedCategory)) {
        categories.forEach { category ->
            Tab(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                text = { Text(category.label) },
            )
        }
    }
}

@Composable
private fun MenuList(
    menus: List<Menu>,
    onMenuClick: (Menu) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(menus, key = { it.id }) { menu ->
            MenuListItem(menu = menu, onClick = { onMenuClick(menu) })
        }
    }
}

@Composable
private fun MenuListItem(
    menu: Menu,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        enabled = !menu.isSoldOut,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = menu.name, fontWeight = FontWeight.Medium)
                Text(text = formatPrice(menu.price), fontSize = 14.sp)
            }
            if (menu.isSoldOut) {
                Text(text = "품절", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CartSummaryBar(
    totalCount: Int,
    totalPrice: Int,
    onOrderClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "${totalCount}개 · ${formatPrice(totalPrice)}", fontWeight = FontWeight.Medium)
        Button(
            onClick = onOrderClick,
            enabled = totalCount > 0,
        ) {
            Text("주문하기")
        }
    }
}

private fun formatPrice(price: Int): String =
    NumberFormat.getNumberInstance(Locale.KOREA).format(price) + "원"

private val Category.label: String
    get() = when (this) {
        Category.ALL -> "전체"
        Category.BEVERAGE -> "음료"
        Category.FOOD -> "푸드"
        Category.DESSERT -> "디저트"
    }
