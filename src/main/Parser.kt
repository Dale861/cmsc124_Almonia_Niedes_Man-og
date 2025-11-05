package main

class Parser(private val tokens: List<Token>) {
    private var current = 0

    class ParseError : RuntimeException()

    fun parse(): Expr? {
        return try {
            if (check(TokenType.CHAMPION)) {
                champion()
            } else {
                expression()
            }
        } catch (e: ParseError) {
            null
        }
    }

    fun parseStatements(): List<Stmt> {
        val statements = mutableListOf<Stmt>()
        while (!isAtEnd()) {
            statements.add(statement()) // or expressionStatement()
        }
        return statements
    }


    // champion ::= "champion" IDENTIFIER "{" eventHandler* "}"
    private fun champion(): Expr {
        consume(TokenType.CHAMPION, "Expect 'champion' keyword.")
        val name = consume(TokenType.IDENTIFIER, "Expect champion name.")
        consume(TokenType.LEFT_BRACE, "Expect '{' after champion name.")

        val events = mutableListOf<Expr.EventHandler>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            events.add(eventHandler())
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after champion body.")
        return Expr.Champion(name, events)
    }

    // eventHandler ::= eventType ( "(" params? ")" )? "{" statement* "}"
    private fun eventHandler(): Expr.EventHandler {
        val eventType = advance() // onAbilityCast, onHealthBelow, onAttack, etc.

        val params = mutableListOf<Token>()

        // Parameters are optional - only parse if we see '('
        if (match(TokenType.LEFT_PAREN)) {
            if (!check(TokenType.RIGHT_PAREN)) {
                do {
                    params.add(advance())
                } while (match(TokenType.COMMA))
            }
            consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters.")
        }

        consume(TokenType.LEFT_BRACE, "Expect '{' before event body.")

        val body = mutableListOf<Stmt>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            body.add(statement())
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after event body.")
        return Expr.EventHandler(eventType, params, body)
    }

    // statement ::= ifStmt | whileStmt | comboStmt | exprStmt | block
    private fun statement(): Stmt {
        return when {
            match(TokenType.IF) -> ifStatement()
            match(TokenType.WHILE) -> whileStatement()
            match(TokenType.COMBO) -> comboStatement()
            match(TokenType.LEFT_BRACE) -> Stmt.Block(block())
            else -> expressionStatement()
        }
    }

    private fun ifStatement(): Stmt {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'.")
        val condition = expression()
        consume(TokenType.RIGHT_PAREN, "Expect ')' after condition.")
        consume(TokenType.LEFT_BRACE, "Expect '{' after condition.")

        val thenBranch = mutableListOf<Stmt>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            thenBranch.add(statement())
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after then branch.")

        var elseBranch: List<Stmt>? = null
        if (match(TokenType.ELSE)) {
            consume(TokenType.LEFT_BRACE, "Expect '{' after 'else'.")
            elseBranch = mutableListOf()
            while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
                elseBranch.add(statement())
            }
            consume(TokenType.RIGHT_BRACE, "Expect '}' after else branch.")
        }

        return Stmt.If(condition, thenBranch, elseBranch)
    }

    private fun whileStatement(): Stmt {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'while'.")
        val condition = expression()
        consume(TokenType.RIGHT_PAREN, "Expect ')' after condition.")
        consume(TokenType.LEFT_BRACE, "Expect '{' after condition.")

        val body = mutableListOf<Stmt>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            body.add(statement())
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after while body.")

        return Stmt.While(condition, body)
    }

    private fun comboStatement(): Stmt {
        consume(TokenType.LEFT_BRACE, "Expect '{' after 'combo'.")
        val actions = mutableListOf<Stmt>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            actions.add(statement())
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after combo body.")
        return Stmt.Combo(actions)
    }

    private fun block(): List<Stmt> {
        val statements = mutableListOf<Stmt>()
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(statement())
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.")
        return statements
    }

    private fun expressionStatement(): Stmt {
        val expr = expression()
        return Stmt.Expression(expr)
    }

    // expression ::= logic_or
    private fun expression(): Expr = logicOr()

    // logic_or ::= logic_and ( "or" logic_and )*
    private fun logicOr(): Expr {
        var expr = logicAnd()
        while (match(TokenType.OR)) {
            val operator = previous()
            val right = logicAnd()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // logic_and ::= equality ( "and" equality )*
    private fun logicAnd(): Expr {
        var expr = equality()
        while (match(TokenType.AND)) {
            val operator = previous()
            val right = equality()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // equality ::= comparison ( ( "==" ) comparison )*
    private fun equality(): Expr {
        var expr = comparison()
        while (match(TokenType.EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // comparison ::= term ( ( ">" | ">=" | "<" | "<=" ) term )*
    private fun comparison(): Expr {
        var expr = term()
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL,
                TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // term ::= factor ( ( "+" | "-" ) factor )*
    private fun term(): Expr {
        var expr = factor()
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // factor ::= unary ( ( "*" | "/" ) unary )*
    private fun factor(): Expr {
        var expr = unary()
        while (match(TokenType.STAR, TokenType.SLASH)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    // unary ::= ( "not" ) unary | call
    private fun unary(): Expr {
        if (match(TokenType.NOT)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return call()
    }

    // call ::= primary ( "(" arguments? ")" )?
    private fun call(): Expr {
        var expr = primary()

        if (match(TokenType.LEFT_PAREN)) {
            val args = mutableListOf<Expr>()
            if (!check(TokenType.RIGHT_PAREN)) {
                do {
                    args.add(expression())
                } while (match(TokenType.COMMA))
            }
            consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.")

            // Convert Variable to Call
            if (expr is Expr.Variable) {
                expr = Expr.Call(expr.name, args)
            }
        }

        return expr
    }

    // primary ::= NUMBER | STRING | "true" | "false" | IDENTIFIER | "(" expression ")"
    private fun primary(): Expr {
        if (match(TokenType.TRUE)) return Expr.Literal(true)
        if (match(TokenType.FALSE)) return Expr.Literal(false)

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return Expr.Literal(previous().literal)
        }

        if (match(TokenType.IDENTIFIER) || isGameKeyword()) {
            return Expr.Variable(previous())
        }

        if (match(TokenType.LEFT_PAREN)) {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun isGameKeyword(): Boolean {
        return match(TokenType.CAST, TokenType.USE_ITEM, TokenType.ATTACK,
            TokenType.MOVE_TO, TokenType.PLACE_WARD, TokenType.PING,
            TokenType.RECALL, TokenType.TELEPORT, TokenType.ENEMY_IN_RANGE,
            TokenType.NEAREST_ENEMY, TokenType.ALLY_IN_RANGE, TokenType.HAS_BUFF,
            TokenType.ITEM_READY, TokenType.COOLDOWN_READY, TokenType.GET_HEALTH,
            TokenType.GET_MANA, TokenType.DRAGON_PIT, TokenType.BARON_PIT,
            TokenType.TOP_LANE, TokenType.MID_LANE, TokenType.BOT_LANE,
            TokenType.TRI_BUSH, TokenType.RIVER, TokenType.BASE)
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

    private fun isAtEnd(): Boolean = peek().type == TokenType.EOF

    private fun peek(): Token = tokens[current]

    private fun previous(): Token = tokens[current - 1]

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun error(token: Token, message: String): ParseError {
        println("[Line ${token.line}] Error at '${token.lexeme}': $message")
        return ParseError()
    }
}
