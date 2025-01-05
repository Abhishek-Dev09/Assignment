package com.shopping.buy_recipes.service


import com.shopping.buy_recipes.dao.CartDao
import com.shopping.buy_recipes.dao.CartItemDao
import com.shopping.buy_recipes.dao.ProductDao
import com.shopping.buy_recipes.pojo.Cart
import com.shopping.buy_recipes.pojo.CartItemType
import com.shopping.buy_recipes.pojo.Product
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CartServiceImplTest {

    private lateinit var cartService: CartServiceImpl
    private lateinit var cartDao: CartDao
    private lateinit var cartItemDao: CartItemDao
    private lateinit var productDao: ProductDao

    @BeforeEach
    fun setUp() {
        cartDao = mock(CartDao::class.java)
        cartItemDao = mock(CartItemDao::class.java)
        productDao = mock(ProductDao::class.java)
        cartService = CartServiceImpl().apply {
            this.cartDao = cartDao
            this.cartItemDao = cartItemDao
            this.productDao = productDao
        }
    }

    @Test
    fun `test getCartById`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val product = Product(id = 1, type = CartItemType.PRODUCT, name = "Test Product", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(cartItemDao.getCartItemsByCartId(1)).thenReturn(listOf(product))
        `when`(productDao.getProductsByIds(listOf(1))).thenReturn(listOf(product))

        val result = cartService.getCartById(1)
        assertEquals(1, result.id)
        assertEquals(1000, result.totalInCents)
        assertEquals(1, result.items.size)
        assertEquals("Test Product", result.items[0].name)
    }

    @Test
    fun `test addItemToCart with recipe`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val recipe = Product(id = 1, type = CartItemType.RECIPE, name = "Test Recipe", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(productDao.getProductById(1)).thenReturn(recipe)

        val result = cartService.addItemToCart(1, 1, true)
        verify(cartItemDao).addCartItemsByCartId(1, 1)
        verify(cartDao).updateCartPriceById(1, 500)
        assertEquals(1, result.id)
    }

    @Test
    fun `test addItemToCart with product`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val product = Product(id = 1, type = CartItemType.PRODUCT, name = "Test Product", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(productDao.getProductById(1)).thenReturn(product)

        val result = cartService.addItemToCart(1, 1, false)
        verify(cartItemDao).addCartItemsByCartId(1, 1)
        verify(cartDao).updateCartPriceById(1, 500)
        assertEquals(1, result.id)
    }

    @Test
    fun `test addItemToCart with invalid recipe`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val product = Product(id = 1, type = CartItemType.PRODUCT, name = "Test Product", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(productDao.getProductById(1)).thenReturn(product)

        val exception = assertThrows(Exception::class.java) {
            cartService.addItemToCart(1, 1, true)
        }
        assertEquals("itemId: 1 is not a recipe", exception.message)
    }

    @Test
    fun `test removeItemFromCart with recipe`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val recipe = Product(id = 1, type = CartItemType.RECIPE, name = "Test Recipe", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(productDao.getProductById(1)).thenReturn(recipe)

        val result = cartService.removeItemFromCart(1, 1, true)
        verify(cartItemDao).deleteCartItemsByCartId(1, 1)
        verify(cartDao).updateCartPriceById(1, -500)
        assertEquals(1, result.id)
    }

    @Test
    fun `test removeItemFromCart with product`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val product = Product(id = 1, type = CartItemType.PRODUCT, name = "Test Product", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(productDao.getProductById(1)).thenReturn(product)

        val result = cartService.removeItemFromCart(1, 1, false)
        verify(cartItemDao).deleteCartItemsByCartId(1, 1)
        verify(cartDao).updateCartPriceById(1, -500)
        assertEquals(1, result.id)
    }

    @Test
    fun `test removeItemFromCart with invalid recipe`() {
        val cart = Cart(id = 1, totalInCents = 1000)
        val product = Product(id = 1, type = CartItemType.PRODUCT, name = "Test Product", priceInCents = 500)
        `when`(cartDao.getCartById(1)).thenReturn(cart)
        `when`(productDao.getProductById(1)).thenReturn(product)

        val exception = assertThrows(Exception::class.java) {
            cartService.removeItemFromCart(1, 1, true)
        }
        assertEquals("itemId: 1 is not a recipe", exception.message)
    }
}

private fun Any.thenReturn(listOf: List<Product>) {

}
