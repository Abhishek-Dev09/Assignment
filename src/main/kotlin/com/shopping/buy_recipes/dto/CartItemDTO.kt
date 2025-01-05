package com.shopping.buy_recipes.dto

import com.shopping.buy_recipes.pojo.CartItemType

data class CartItemDTO(
    val id: Int,
    val type: CartItemType, //Added this var to differentiate between product and recipe
    val name: String,
    val priceInCents: Int,
)
