package com.shopping.buy_recipes.dto;

data class RecipeDTO(
        val id: Int,
        val name: String,
        val products: List<ProductDTO>,
        val priceInCents: Int,
)
