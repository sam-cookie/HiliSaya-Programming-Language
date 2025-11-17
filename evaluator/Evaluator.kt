package Evaluator
import parser.*
import scanner.*

class Evaluator {
    fun evaluate(expr: Expr?): Any? {
    if (expr == null) return null
    val result = when (expr) {
        is Expr.Literal -> expr.value
        is Expr.Grouping -> evaluate(expr.expression)
        is Expr.Unary -> evaluateUnary(expr)
        is Expr.Binary -> evaluateBinary(expr)
    }

    return when (result) {
        true -> "tuod"
        false -> "atik"
        else -> result
    }
}

    private fun evaluateUnary(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)
        return when (expr.operator.type) {
            TokenType.MINUS -> {
                if (right is Double) return -right
                runtimeError(expr.operator, "Number dapat ang operand sa unary '-'.")
                null
            }
            TokenType.NOT -> !isTruthy(right)
            else -> null
        }
    }

    private fun evaluateBinary(expr: Expr.Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            TokenType.PLUS -> {
                when {
                    left is Double && right is Double -> left + right
                    left is String && right is String -> left + right
                    else -> {
                        runtimeError(expr.operator, "Ang duwa kay dapat pareho nga type: duha ka numero o duha ka string.")
                        null
                    }
                }
            }
            TokenType.MINUS -> {
                if (left is Double && right is Double) left - right else {
                    runtimeError(expr.operator, "Di ni pwede bai! Number dapat ang operand")
                    null
                }
            }
            TokenType.TIMES -> {
                if (left is Double && right is Double) left * right else {
                    runtimeError(expr.operator, "Di ni pwede bai! Number dapat ang operand")
                    null
                }
            }
            TokenType.DIVIDE -> {
                if (left is Double && right is Double) {
                    if (right == 0.0) {
                        runtimeError(expr.operator, "Tadlong bala. Bawal magdivide by 0 bai.")
                        null
                    } else left / right
                } else {
                    runtimeError(expr.operator, "Di ni pwede bai! Number dapat ang operand")
                    null
                }
            }
            TokenType.GREATER_THAN -> compareNumbers(expr.operator, left, right) { l, r -> l > r }
            TokenType.GREATER_THAN_EQUAL -> compareNumbers(expr.operator, left, right) { l, r -> l >= r }
            TokenType.LESS_THAN -> compareNumbers(expr.operator, left, right) { l, r -> l < r }
            TokenType.LESS_THAN_EQUAL -> compareNumbers(expr.operator, left, right) { l, r -> l <= r }
            TokenType.EQUALTO -> isEqual(left, right)
            TokenType.NOT_EQUAL -> !isEqual(left, right)
            else -> null
        }
    }

    private fun compareNumbers(
        operator: Token,
        left: Any?,
        right: Any?,
        comparison: (Double, Double) -> Boolean
    ): Boolean? {
        return if (left is Double && right is Double) comparison(left, right) else {
            runtimeError(operator, "Di ni pwede bai! Number dapat ang operand")
            null
        }
    }

    private fun runtimeError(token: Token, message: String) {
        println("[line ${token.line}] Runtime error: $message")
    }

    private fun isTruthy(value: Any?): Boolean {
        return when (value) {
            null -> false
            is Boolean -> value
            else -> true
        }
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null) return false
        return a == b
    }
}
