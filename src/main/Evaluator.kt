package main

class Evaluator {

    private val champions = mutableMapOf<String, Stmt.Champion>()

    class RuntimeError(token: Token, message: String) :
        RuntimeException("[Line ${token.line}] Runtime Error at '${token.lexeme}': $message")

    fun evaluate(expr: Expr): Any? {
        return when (expr) {
            is Expr.Literal -> expr.value
            is Expr.Grouping -> evaluate(expr.expression)
            is Expr.Unary -> {
                val right = evaluate(expr.right)
                when (expr.operator.type) {
                    TokenType.NOT -> !isTruthy(right)
                    else -> throw RuntimeError(expr.operator, "Unsupported unary operator '${expr.operator.lexeme}'")
                }
            }
            is Expr.Binary -> {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)

                when (expr.operator.type) {
                    TokenType.PLUS -> {
                        if (left is Double && right is Double) {
                            left + right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '+'.")
                    }
                    TokenType.MINUS -> {
                        if (left is Double && right is Double) {
                            left - right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '-'.")
                    }
                    TokenType.STAR -> {
                        if (left is Double && right is Double) {
                            left * right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '*'.")
                    }
                    TokenType.SLASH -> {
                        if (left is Double && right is Double) {
                            if (right == 0.0) throw RuntimeError(expr.operator, "Division by zero.")
                            else left / right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '/'.")
                    }
                    TokenType.GREATER -> {
                        if (left is Double && right is Double) {
                            left > right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '>'.")
                    }
                    TokenType.GREATER_EQUAL -> {
                        if (left is Double && right is Double) {
                            left >= right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '>='.")
                    }
                    TokenType.LESS -> {
                        if (left is Double && right is Double) {
                            left < right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '<'.")
                    }
                    TokenType.LESS_EQUAL -> {
                        if (left is Double && right is Double) {
                            left <= right
                        } else throw RuntimeError(expr.operator, "Operands must be numbers for '<='.")
                    }
                    TokenType.EQUAL_EQUAL -> isEqual(left, right)
                    TokenType.AND -> isTruthy(left) && isTruthy(right)
                    TokenType.OR -> isTruthy(left) || isTruthy(right)
                    else -> throw RuntimeError(expr.operator, "Unsupported binary operator '${expr.operator.lexeme}'.")
                }
            }
            is Expr.Variable -> {
                // Variables and game-specific runtime queries would be handled here.
                // For now, return variable name or simulate game environment queries as needed.
                throw RuntimeError(expr.name, "Variable access not implemented yet.")
            }
            is Expr.Call -> {
                // Handle function calls (cast, attack, useItem, etc.)
                throw RuntimeError(expr.callee, "Function calls not implemented yet.")
            }
            is Expr.EventHandler -> {
                // Event handlers execute the body statements.
                throw RuntimeError(expr.eventType, "Event handler evaluation at top level not implemented.")
            }
            is Expr.Champion -> {
                // Champion declaration - may register events or similar.
                throw RuntimeError(expr.name, "Champion evaluation not implemented.")
            }
        }
    }

    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null) return false
        if (obj is Boolean) return obj
        return true
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null) return false
        return a == b
    }

    fun getChampion(name: String): Stmt.Champion? = champions[name]

    fun execute(stmt: Stmt) {
        when (stmt) {
            is Stmt.Expression -> evaluate(stmt.expression)
            is Stmt.Block -> {
                for (statement in stmt.statements) {
                    execute(statement)
                }
            }
            is Stmt.If -> {
                val condition = evaluate(stmt.condition)
                if (isTruthy(condition)) {
                    for (s in stmt.thenBranch) execute(s)
                } else {
                    stmt.elseBranch?.forEach { execute(it) }
                }
            }
            is Stmt.While -> {
                while (isTruthy(evaluate(stmt.condition))) {
                    for (s in stmt.body) execute(s)
                }
            }
            is Stmt.Combo -> {
                // Execute combo block - sequence of actions
                for (action in stmt.actions) execute(action)
            }
            is Stmt.Champion -> {
                val championName = stmt.name.lexeme
                champions[championName] = stmt
                println("Champion registered: $championName")
                // Could also initialize any state here as needed
            }
        }
    }

    fun runChampionEvent(championName: String, eventType: String) {
        val champion = champions[championName]
        if (champion != null) {
            for (event in champion.events) {
                if (event.eventType.lexeme == eventType) {
                    println("Running ${eventType} for ${championName}")
                    for (stmt in event.body) {
                        execute(stmt)
                    }
                }
            }
        }
    }

    fun triggerEvent(championName: String, eventType: String) {
        runChampionEvent(championName, eventType)
    }
}
