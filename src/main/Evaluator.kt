package main

class RuntimeError(message: String, val line: Int = 0) : RuntimeException(message)

class Evaluator {

    fun interpret(expr: Expr): Any? {
        try {
            return evaluate(expr)
        } catch (e: RuntimeError) {
            println("[Line ${e.line}] Runtime error: ${e.message}")
            return null
        }
    }

    private fun evaluate(expr: Expr): Any? {
        return when (expr) {
            is Expr.Champion -> evaluateChampion(expr)
            is Expr.EventHandler -> evaluateEventHandler(expr)
            is Expr.Call -> evaluateCall(expr)
            is Expr.Binary -> evaluateBinary(expr)
            is Expr.Unary -> evaluateUnary(expr)
            is Expr.Literal -> expr.value
            is Expr.Variable -> expr.name.lexeme
            is Expr.Grouping -> evaluate(expr.expression)
        }
    }

    private fun evaluateChampion(champion: Expr.Champion): Any? {
        println("Champion: ${champion.name.lexeme}")
        champion.events.forEach { evaluate(it) }
        return champion.name.lexeme
    }

    private fun evaluateEventHandler(handler: Expr.EventHandler): Any? {
        val eventName = handler.eventType.lexeme
        val params = handler.params.joinToString(", ") { it.lexeme }
        println("  Event: $eventName($params)")

        handler.body.forEach { stmt ->
            evaluateStatement(stmt)
        }
        return null
    }

    private fun evaluateStatement(stmt: Stmt): Any? {
        return when (stmt) {
            is Stmt.If -> evaluateIf(stmt)
            is Stmt.While -> evaluateWhile(stmt)
            is Stmt.Combo -> evaluateCombo(stmt)
            is Stmt.Expression -> evaluate(stmt.expression)
            is Stmt.Block -> {
                stmt.statements.forEach { evaluateStatement(it) }
                null
            }
        }
    }

    private fun evaluateIf(ifStmt: Stmt.If): Any? {
        val condition = evaluate(ifStmt.condition)
        return if (isTruthy(condition)) {
            ifStmt.thenBranch.forEach { evaluateStatement(it) }
            null
        } else if (ifStmt.elseBranch != null) {
            ifStmt.elseBranch.forEach { evaluateStatement(it) }
            null
        } else {
            null
        }
    }

    private fun evaluateWhile(whileStmt: Stmt.While): Any? {
        while (isTruthy(evaluate(whileStmt.condition))) {
            whileStmt.body.forEach { evaluateStatement(it) }
        }
        return null
    }

    private fun evaluateCombo(combo: Stmt.Combo): Any? {
        println("    Combo executing...")
        combo.actions.forEach { evaluateStatement(it) }
        return null
    }

    private fun evaluateCall(call: Expr.Call): Any? {
        val function = call.callee.lexeme
        val args = call.args.map { evaluate(it) }

        println("    Calling: $function(${args.joinToString(", ")})")
        return null
    }

    private fun evaluateBinary(expr: Expr.Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)
        val operator = expr.operator.lexeme

        return when (operator) {
            "+" -> {
                when {
                    left is Double && right is Double -> left + right
                    left is String && right is String -> left + right
                    left is Double && right is String -> left.toString() + right
                    left is String && right is Double -> left + right.toString()
                    else -> throw RuntimeError("Operands must be two numbers or two strings.", expr.operator.line)
                }
            }
            "-" -> {
                requireNumbers(left, right, expr.operator)
                (left as Double) - (right as Double)
            }
            "*" -> {
                requireNumbers(left, right, expr.operator)
                (left as Double) * (right as Double)
            }
            "/" -> {
                requireNumbers(left, right, expr.operator)
                if (right == 0.0) {
                    throw RuntimeError("Division by zero.", expr.operator.line)
                }
                (left as Double) / (right as Double)
            }
            ">" -> {
                requireNumbers(left, right, expr.operator)
                (left as Double) > (right as Double)
            }
            ">=" -> {
                requireNumbers(left, right, expr.operator)
                (left as Double) >= (right as Double)
            }
            "<" -> {
                requireNumbers(left, right, expr.operator)
                (left as Double) < (right as Double)
            }
            "<=" -> {
                requireNumbers(left, right, expr.operator)
                (left as Double) <= (right as Double)
            }
            "==" -> isEqual(left, right)
            "!=" -> !isEqual(left, right)
            "and" -> isTruthy(left) && isTruthy(right)
            "or" -> isTruthy(left) || isTruthy(right)
            else -> throw RuntimeError("Unknown operator: $operator", expr.operator.line)
        }
    }

    private fun evaluateUnary(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)
        val operator = expr.operator.lexeme

        return when (operator) {
            "-" -> {
                requireNumber(right, expr.operator)
                -(right as Double)
            }
            "not" -> !isTruthy(right)
            else -> throw RuntimeError("Unknown unary operator: $operator", expr.operator.line)
        }
    }

    private fun isTruthy(value: Any?): Boolean {
        if (value == null) return false
        if (value is Boolean) return value
        return true
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null || b == null) return false
        return a == b
    }

    private fun requireNumber(value: Any?, token: Token) {
        if (value !is Double) {
            throw RuntimeError("Operand must be a number.", token.line)
        }
    }

    private fun requireNumbers(left: Any?, right: Any?, token: Token) {
        if (left !is Double || right !is Double) {
            throw RuntimeError("Operands must be numbers.", token.line)
        }
    }
}
