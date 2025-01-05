package com.shopping.buy_recipes.dto

data class OmsErrorResponse(val status: Int, val message: String?, val error: ErrorResponse)

data class ErrorResponse(val status: Int, val message: String?)