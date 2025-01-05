package com.shopping.buy_recipes.dto

data class CartDTO(
    val id: Int,
    val totalInCents: Int,
    val items: List<CartItemDTO>
)
