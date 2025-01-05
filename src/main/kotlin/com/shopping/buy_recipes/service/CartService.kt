package com.shopping.buy_recipes.service

import com.shopping.buy_recipes.dto.CartDTO

interface CartService {
    fun getCartById(cartId: Int): CartDTO
    fun addItemToCart(cartId: Int, itemId: Int, isRecipe: Boolean): CartDTO
    fun removeItemFromCart(cartId: Int, itemId: Int, isRecipe: Boolean): CartDTO
}