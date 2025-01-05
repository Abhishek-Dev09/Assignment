package com.shopping.buy_recipes.service

import com.shopping.buy_recipes.dao.ProductDao
import com.shopping.buy_recipes.dao.RecipeProductDao
import com.shopping.buy_recipes.dto.ProductDTO
import com.shopping.buy_recipes.dto.RecipeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecipeServiceImpl : RecipeService{

    @Autowired
    lateinit var productDao: ProductDao

    @Autowired
    lateinit var recipeProductDao: RecipeProductDao

    override fun getAllRecipes(): List<ProductDTO> {
        return productDao.getProductByType("RECIPE").map { product ->
            ProductDTO(
                id = product.id,
                name = product.name,
                priceInCents = product.priceInCents
            )
        }
    }

    override fun getRecipeById(recipeId: Int): RecipeDTO {
        val productIds = recipeProductDao.getRecipeProductById(recipeId).map { it.productId }.toMutableList()
        productIds.add(recipeId)
        val products = productDao.getProductsByIds(productIds).map { product ->
            ProductDTO(
                id = product.id,
                name = product.name,
                priceInCents = product.priceInCents
            )
        }
        return RecipeDTO(
            id = products.first { it.id == recipeId }.id,
            name = products.first { it.id == recipeId }.name,
            products = recipeProductDao.getRecipeProductById(recipeId).map { recipeProduct ->
                productDao.getProductById(recipeProduct.productId).let { product ->
                    ProductDTO(
                        id = product.id,
                        name = product.name,
                        priceInCents = product.priceInCents
                    )
                }
            },
            priceInCents = products.first { it.id == recipeId }.priceInCents
        )
    }
}
