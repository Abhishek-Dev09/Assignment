package com.shopping.buy_recipes.controller

import com.shopping.buy_recipes.dto.CartDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.shopping.buy_recipes.service.CartServiceImpl
import org.springframework.beans.factory.annotation.Autowired

@RestController
@RequestMapping("/carts")
class CartController {

    @Autowired
    private lateinit var cartService: CartServiceImpl

    @GetMapping("/{id}")
    fun getCart(@PathVariable id: Int): ResponseEntity<CartDTO> {
        val cart = cartService.getCartById(id)
        return ResponseEntity.ok(cart)
    }

    @PostMapping("/{cartId}/recipes/{recipeId}")
    fun addRecipeToCart(
        @PathVariable cartId: Int,
        @PathVariable recipeId: Int
    ): ResponseEntity<CartDTO> {
        val cart = cartService.addItemToCart(cartId, recipeId, true);
        return ResponseEntity.ok(cart)
    }

    @PostMapping("/{cartId}/products/{productId}")
    fun addProductToCart(
        @PathVariable cartId: Int,
        @PathVariable productId: Int
    ): ResponseEntity<CartDTO> {
        val cart = cartService.addItemToCart(cartId, productId, false);
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/{cartId}/recipes/{recipeId}")
    fun removeRecipeFromCart(
        @PathVariable cartId: Int,
        @PathVariable recipeId: Int
    ): ResponseEntity<CartDTO> {
        val cart = cartService.removeItemFromCart(cartId, recipeId, true);
        return ResponseEntity.ok(cart)
    }

    @DeleteMapping("/{cartId}/products/{productId}")
    fun removeProductsFromCart(
        @PathVariable cartId: Int,
        @PathVariable productId: Int
    ): ResponseEntity<CartDTO> {
        val cart = cartService.removeItemFromCart(cartId, productId, false);
        return ResponseEntity.ok(cart)
    }
}
