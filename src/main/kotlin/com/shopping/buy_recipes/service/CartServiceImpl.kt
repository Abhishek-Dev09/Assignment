package com.shopping.buy_recipes.service

import com.shopping.buy_recipes.pojo.CartItemType
import com.shopping.buy_recipes.dao.CartDao
import com.shopping.buy_recipes.dao.CartItemDao
import com.shopping.buy_recipes.dao.ProductDao
import com.shopping.buy_recipes.dto.CartDTO
import com.shopping.buy_recipes.dto.CartItemDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartServiceImpl : CartService{
    @Autowired
    lateinit var cartDao: CartDao

    @Autowired
    lateinit var cartItemDao: CartItemDao

    @Autowired
    lateinit var productDao: ProductDao

    override fun getCartById(cartId: Int): CartDTO {
        val cart = cartDao.getCartById(cartId)
        val productIds = cartItemDao.getCartItemsByCartId(cartId).map { it.productId }
        val products = productDao.getProductsByIds(productIds)
        return CartDTO(
            id = cart.id,
            totalInCents = cart.totalInCents,
            items = products.map { product ->
                CartItemDTO(
                    id = product.id,
                    type = product.type,
                    name = product.name,
                    priceInCents = product.priceInCents
                )
            }
        )
    }

    @Transactional
    override fun addItemToCart(cartId: Int, itemId: Int, isRecipe: Boolean): CartDTO {
        val cart = cartDao.getCartById(cartId)
        val item = productDao.getProductById(itemId)
        if (isRecipe && item.type != CartItemType.RECIPE) {
            throw Exception("itemId: $itemId is not a recipe")
        } else if (!isRecipe && item.type != CartItemType.PRODUCT) {
            throw Exception("itemId: $itemId is not a product")
        }
        cartItemDao.addCartItemsByCartId(cart.id, item.id)
        cartDao.updateCartPriceById(cart.id, item.priceInCents)
        return getCartById(cartId)
    }

    @Transactional
    override fun removeItemFromCart(cartId: Int, itemId: Int, isRecipe: Boolean): CartDTO {
        val cart = cartDao.getCartById(cartId)
        val item = productDao.getProductById(itemId)
        if (isRecipe && item.type != CartItemType.RECIPE) {
            throw Exception("itemId: $itemId is not a recipe")
        } else if (!isRecipe && item.type != CartItemType.PRODUCT) {
            throw Exception("itemId: $itemId is not a product")
        }
        cartItemDao.deleteCartItemsByCartId(cart.id, item.id)
        cartDao.updateCartPriceById(cart.id, -item.priceInCents)
        return getCartById(cartId)
    }
}
