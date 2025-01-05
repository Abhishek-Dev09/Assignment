package com.shopping.buy_recipes.controller;

import com.shopping.buy_recipes.dto.ProductDTO
import com.shopping.buy_recipes.dto.RecipeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shopping.buy_recipes.service.RecipeServiceImpl

@RestController
@RequestMapping("recipe")
public class RecipeController {

    @Autowired
    private lateinit var reciepeService: RecipeServiceImpl

    @GetMapping
    fun getAllRecipes(): ResponseEntity<List<ProductDTO>> {
        val recipes = reciepeService.getAllRecipes()
        return ResponseEntity.ok(recipes)
    }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable("id") recipeId: Int): ResponseEntity<RecipeDTO> {
        val recipe = reciepeService.getRecipeById(recipeId)
        return ResponseEntity.ok(recipe)
    }
}
