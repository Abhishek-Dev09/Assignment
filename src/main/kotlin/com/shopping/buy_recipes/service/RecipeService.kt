package com.shopping.buy_recipes.service

import com.shopping.buy_recipes.dto.ProductDTO
import com.shopping.buy_recipes.dto.RecipeDTO

interface RecipeService {
    fun getRecipeById(recipeId: Int): RecipeDTO
    fun getAllRecipes(): List<ProductDTO>
}
