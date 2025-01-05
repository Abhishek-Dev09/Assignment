package com.shopping.buy_recipes.dao

import com.shopping.buy_recipes.pojo.CartItemType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.shopping.buy_recipes.pojo.Product
import com.shopping.buy_recipes.utils.Exception.ProductNotFoundException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet
import java.util.*

@Repository
class ProductDao {

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private class ProductMapper : RowMapper<Product> {
        override fun mapRow(rs: ResultSet, rowNum: Int): Product {
            return Product(
                id = rs.getInt("id"),
                name = rs.getString("name"),
                priceInCents = rs.getInt("price_in_cents"),
                type = CartItemType.valueOf(rs.getString("type").uppercase(Locale.getDefault()))
            )
        }
    }

    fun getProductById(id: Int): Product {
        val sql = "SELECT * FROM products WHERE id = :id"
        val parameterSource = MapSqlParameterSource().addValue("id", id)

        return try {
            namedParameterJdbcTemplate.queryForObject(sql, parameterSource, ProductMapper())
                ?: throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Product with id $id not found")
        } catch (ex: EmptyResultDataAccessException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Product with id $id not found")
        }
    }

    fun getProductsByIds(productIds: List<Int>): List<Product> {
        val sql = """
        SELECT *
        FROM products p
        WHERE p.id IN (:productIds)
    """
        val parameterSource = MapSqlParameterSource().addValue("productIds", productIds)

        return try {
            namedParameterJdbcTemplate.query(sql, parameterSource, ProductMapper()).takeIf { it.isNotEmpty() }
                ?: throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No products found for the given IDs: $productIds"
                )
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Error retrieving products by IDs: $productIds", ex
            )
        }
    }

    fun getProductByType(type: String): List<Product> {
        val sql = "SELECT * FROM products WHERE type = :type"
        val parameterSource = MapSqlParameterSource().addValue("type", type)

        return try {
            namedParameterJdbcTemplate.query(sql, parameterSource, ProductMapper()).takeIf { it.isNotEmpty() }
                ?: throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No products found for the given type: $type"
                )
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Error retrieving products by type: $type", ex
            )
        }
    }
}
