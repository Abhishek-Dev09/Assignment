package com.shopping.buy_recipes.pojo

data class Reciepe(
    override val id: Int,
    override val name: String,
    val products: List<Product>
) : Entity {
    override val priceInCents: Int
        get() = products.sumOf { it.priceInCents }
}
