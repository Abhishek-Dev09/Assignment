package com.shopping.buy_recipes.service

import com.shopping.buy_recipes.dao.ProductDao
import com.shopping.buy_recipes.dao.RecipeProductDao
import com.shopping.buy_recipes.pojo.CartItemType
import com.shopping.buy_recipes.pojo.Product
import com.shopping.buy_recipes.pojo.RecipeProduct
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RecipeServiceImplTest {

    private lateinit var recipeService: RecipeServiceImpl
    private lateinit var productDao: ProductDao
    private lateinit var recipeProductDao: RecipeProductDao

    @BeforeEach
    fun setUp() {
        productDao = mock(ProductDao::class.java)
        recipeProductDao = mock(RecipeProductDao::class.java)
        recipeService = RecipeServiceImpl().apply {
            this.productDao = productDao
            this.recipeProductDao = recipeProductDao
        }
    }

    @Test
    fun `test getAllRecipes`() {
        val products = listOf(
            Product(id = 1, name = "Recipe 1", priceInCents = 1000, type = CartItemType.RECIPE),
            Product(id = 2, name = "Recipe 2", priceInCents = 2000, type = CartItemType.RECIPE)
        )
        `when`(productDao.getProductByType("RECIPE")).thenReturn(products)

        val result = recipeService.getAllRecipes()
        assertEquals(2, result.size)
        assertEquals("Recipe 1", result[0].name)
        assertEquals(1000, result[0].priceInCents)
        assertEquals("Recipe 2", result[1].name)
        assertEquals(2000, result[1].priceInCents)
    }

    @Test
    fun `test getRecipeById`() {
        val recipeId = 1
        val products = listOf(
            Product(id = 1, name = "Recipe 1", priceInCents = 1000, type = CartItemType.RECIPE),
            Product(id = 2, name = "Product 1", priceInCents = 500, CartItemType.PRODUCT)
        )
        val recipeProducts = listOf(
            RecipeProduct(1,recipeId = 1, productId = 2)
        )
        `when`(recipeProductDao.getRecipeProductById(recipeId)).thenReturn(recipeProducts)
        `when`(productDao.getProductsByIds(listOf(1, 2))).thenReturn(products)
        `when`(productDao.getProductById(2)).thenReturn(products[1])

        val result = recipeService.getRecipeById(recipeId)
        assertEquals(1, result.id)
        assertEquals("Recipe 1", result.name)
        assertEquals(1000, result.priceInCents)
        assertEquals(1, result.products.size)
        assertEquals("Product 1", result.products[0].name)
        assertEquals(500, result.products[0].priceInCents)
    }
}
