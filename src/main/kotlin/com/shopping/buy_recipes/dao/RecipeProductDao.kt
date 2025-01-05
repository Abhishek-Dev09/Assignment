package com.shopping.buy_recipes.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.shopping.buy_recipes.pojo.RecipeProduct
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet

@Repository
class RecipeProductDao {

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private class RecipeProductMapper : RowMapper<RecipeProduct> {
        override fun mapRow(rs: ResultSet, rowNum: Int): RecipeProduct {
            return RecipeProduct(
                id = rs.getInt("id"),
                recipeId = rs.getInt("recipe_id"),
                productId = rs.getInt("product_id"),
            )
        }
    }

    fun getRecipeProductById(recipeId: Int): List<RecipeProduct> {
        val sql = "SELECT * FROM product_recipes WHERE recipe_id = :recipeId"
        val parameterSource = MapSqlParameterSource().addValue("recipeId", recipeId)

        return try {
            namedParameterJdbcTemplate.query(sql, parameterSource, RecipeProductMapper())
        } catch (ex: EmptyResultDataAccessException) {
            emptyList()
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unable to fetch products for recipe ID $recipeId",
                ex
            )
        }
    }
}
