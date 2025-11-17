package parser
import scanner.*


// expression hierarchy 
sealed class Expr {
    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr()
    data class Unary(val operator: Token, val right: Expr) : Expr()
    data class Literal(val value: Any?) : Expr()
    data class Grouping(val expression: Expr) : Expr()
}

class AstPrinter {

    fun print(expr: Expr) {
        println(astToString(expr))
    }

    private fun astToString(expr: Expr): String = when (expr) {
        is Expr.Binary -> {
            val op = when (expr.operator.type) {
                TokenType.PLUS -> "idugang sa"
                TokenType.MINUS -> "ibawas sa"
                TokenType.TIMES -> "itimes sa"
                TokenType.DIVIDE -> "idivide sa"
                TokenType.MODULO -> "modulo"
                TokenType.GREATER_THAN -> "mas dako sa"
                TokenType.GREATER_THAN_EQUAL -> "mas dako or pareha sa"
                TokenType.LESS_THAN -> "mas gamay sa"
                TokenType.LESS_THAN_EQUAL -> "mas gamay or pareha sa"
                TokenType.EQUALTO -> "kay pareha sa"
                TokenType.NOT_EQUAL -> "dili pareha sa"
                TokenType.AND -> "ug"
                TokenType.OR -> "or"
                else -> expr.operator.lexeme
            }
            "${astToString(expr.left)} $op ${astToString(expr.right)}"
        }

        is Expr.Unary -> {
            val op = when (expr.operator.type) {
                TokenType.NOT -> "dele"
                TokenType.MINUS -> "negatib!"
                TokenType.PLUS -> "pasitib"
                else -> expr.operator.lexeme
            }
            "$op ${astToString(expr.right)}"
        }

        is Expr.Literal -> when (expr.value) {
            null -> "waay"
            is String -> expr.value
            else -> expr.value.toString()
        }

        is Expr.Grouping -> "( ${astToString(expr.expression)} )"
    }
}
