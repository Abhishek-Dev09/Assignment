package com.shopping.buy_recipes.dao

import com.shopping.buy_recipes.pojo.Cart
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.sql.ResultSet
import java.sql.SQLException

@Repository
public class CartDao {

    @Autowired
    private lateinit var namedParameterJdbcTemplate: NamedParameterJdbcTemplate

    private class CartMapper : RowMapper<Cart> {
        @Throws(SQLException::class)
        override fun mapRow(rs: ResultSet, rowNum: Int): Cart {
            return Cart(
                id = rs.getInt("id"),
                totalInCents = rs.getInt("total_in_cents")
            )
        }
    }

    fun getCartById(id: Int): Cart {
        val sql = "SELECT * FROM carts WHERE id = :id"
        val parameterSource = MapSqlParameterSource().addValue("id", id)

        return try {
            namedParameterJdbcTemplate.queryForObject(sql, parameterSource, CartMapper())
                ?: throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cart with id $id not found"
                )
        } catch (ex: EmptyResultDataAccessException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Cart with id $id not found"
            )
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving cart with id $id", ex
            )
        }
    }

    fun updateCartPriceById(id: Int, price: Int): Int {
        val sql = "UPDATE carts SET total_in_cents = total_in_cents + :price WHERE id = :id"
        val parameterSource = MapSqlParameterSource()
            .addValue("id", id)
            .addValue("price", price)

        return try {
            val rowsUpdated = namedParameterJdbcTemplate.update(sql, parameterSource)
            if (rowsUpdated == 0) {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cart with id $id not found"
                )
            }
            rowsUpdated
        } catch (ex: Exception) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error updating cart price for id $id", ex
            )
        }
    }
}
