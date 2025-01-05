package com.shopping.buy_recipes.dao;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.shopping.buy_recipes.pojo.CartItem
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet

@Repository
public class CartItemDao {

   @Autowired
   private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

   private class CartItemMapper : RowMapper<CartItem> {
      override fun mapRow(rs: ResultSet, rowNum: Int): CartItem {
         return CartItem(
            id = rs.getInt("id"),
            cartId = rs.getInt("cart_id"),
            productId = rs.getInt("product_id"),
         )
      }
   }

    fun getCartItemsByCartId(cartId: Int): List<CartItem> {
        val sql = "SELECT * FROM cart_items WHERE cart_id = :cartId"
        val parameterSource = MapSqlParameterSource().addValue("cartId", cartId)

        return try {
            namedParameterJdbcTemplate.query(sql, parameterSource, CartItemMapper())
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving cart items for cart id $cartId", ex
            )
        }
    }

    fun addCartItemsByCartId(cartId: Int, productId: Int): Int {
        val sql = "INSERT INTO cart_items (cart_id, product_id) VALUES (:cartId, :productId)"
        val parameterSource = MapSqlParameterSource()
            .addValue("cartId", cartId)
            .addValue("productId", productId)

        return try {
            namedParameterJdbcTemplate.update(sql, parameterSource)
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item with product id $productId to cart id $cartId", ex
            )
        }
    }

    fun deleteCartItemsByCartId(cartId: Int, productId: Int): Int {
        val sql = "DELETE FROM cart_items WHERE cart_id = :cartId AND product_id = :productId"
        val parameterSource = MapSqlParameterSource()
            .addValue("cartId", cartId)
            .addValue("productId", productId)

        return try {
            val rowsAffected = namedParameterJdbcTemplate.update(sql, parameterSource)
            if (rowsAffected == 0) {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cart item with product id $productId not found in cart id $cartId"
                )
            }
            rowsAffected
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting cart item with product id $productId from cart id $cartId", ex
            )
        }
    }
}
