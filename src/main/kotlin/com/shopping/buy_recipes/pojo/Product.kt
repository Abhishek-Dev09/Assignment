package com.shopping.buy_recipes.pojo

data class Product(
    override val id: Int,
    override val name: String,
    override val priceInCents: Int,
    val type: CartItemType
) : Entity
