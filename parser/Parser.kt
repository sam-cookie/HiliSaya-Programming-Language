package parser
import scanner.*

class Parser(private val tokens: List<Token>) {
    private var current = 0

    fun parse(): Expr {
        return when {
            check(TokenType.VAR) || check(TokenType.FUNCTION) -> declaration()
            else -> expression()
        }
    }

    // expression -> logical_or
    private fun expression(): Expr = logicalOr()

    private fun logicalOr(): Expr {
        var expr = logicalAnd()
        while (match(TokenType.OR)) {
            val operator = previous()
            val right = logicalAnd()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun logicalAnd(): Expr {
        var expr = equality()
        while (match(TokenType.AND)) {
            val operator = previous()
            val right = equality()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun equality(): Expr {
        var expr = comparison()
        while (match(TokenType.EQUALTO, TokenType.NOT_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

   private fun comparison(): Expr {
        var expr = stringConcat() 
        while (match(
                TokenType.GREATER_THAN, TokenType.GREATER_THAN_EQUAL,
                TokenType.LESS_THAN, TokenType.LESS_THAN_EQUAL
            )) {
            val operator = previous()
            val right = stringConcat()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }


    private fun term(): Expr {
        var expr = factor()
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // handling concatenation with SUMPAY token
    private fun stringConcat(): Expr {
        var expr = term()
        while (match(TokenType.SUMPAY)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun factor(): Expr {
        var expr = unary()
        while (match(TokenType.TIMES, TokenType.DIVIDE, TokenType.MODULO)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.NOT, TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return primary()
    }

    private fun primary(): Expr {
        when {
            match(TokenType.NUMBER, TokenType.STRING, TokenType.TRUE, TokenType.FALSE, TokenType.NULL) -> {
                return Expr.Literal(previous().literal)
            }

            match(TokenType.LEFT_PAREN) -> {
                val expr = expression()
                if (!match(TokenType.RIGHT_PAREN)) {
                    reportError(peek(), "Dapat naay ')' sa katapusan bai.")
                    return Expr.Literal("")
                }
                return Expr.Grouping(expr)
            }

            match(TokenType.IDENTIFIER) -> {
                return Expr.Literal(previous().lexeme)
            }

            else -> {
                reportError(peek(), "Tarungi ang expression bai.")
                return Expr.Literal("")
            }
        }
    }

    private fun declaration(): Expr {
        return when {
            match(TokenType.VAR) -> Expr.Literal(previous().lexeme)
            match(TokenType.FUNCTION) -> Expr.Literal(previous().lexeme)
            else -> {
                reportError(peek(), "Butngi sad ug bar o declaration bai.")
                return Expr.Literal("")
            }
        }
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd() = peek().type == TokenType.EOF
    private fun peek(): Token = tokens[current]
    private fun previous(): Token = tokens[current - 1]

    private fun reportError(token: Token, message: String) {

        // val msg = if (token.type == TokenType.EOF)
        //     "[line ${token.line}] Error sa katapusan:: $message"
        // else
        //     "[line ${token.line}] Error sa '${token.lexeme}': $message"
        // println(msg)
    }
        
}

