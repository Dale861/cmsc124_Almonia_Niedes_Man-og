package main

class AstPrinter(expr: Expr) {

    init {
        // Automatically print when AstPrinter is created
        println(print(expr))
    }

    // Main print function for expressions
    private fun print(expr: Expr): String {
        return when (expr) {
            is Expr.EventHandler -> {
                val params = expr.params.joinToString(" ") { it.lexeme }
                val body = expr.body.joinToString(" ") { printStmt(it) }
                "(${expr.eventType.lexeme} ${params.takeIf { it.isNotEmpty() } ?: ""} $body)"
            }
            is Expr.Call -> {
                val args = expr.args.joinToString(" ") { print(it) }
                "(${expr.callee.lexeme} $args)".trim()
            }
            is Expr.Binary -> "(${expr.operator.lexeme} ${print(expr.left)} ${print(expr.right)})"
            is Expr.Unary -> "(${expr.operator.lexeme} ${print(expr.right)})"
            is Expr.Literal -> {
                when (expr.value) {
                    is String -> "\"${expr.value}\""  // Keep quotes for strings
                    else -> expr.value?.toString() ?: "nil"
                }
            }
            is Expr.Variable -> expr.name.lexeme
            is Expr.Grouping -> print(expr.expression)
        }
    }

    // Print function for statements
    private fun printStmt(stmt: Stmt): String {
        return when (stmt) {
            is Stmt.If -> {
                val elsePart = stmt.elseBranch?.let {
                    " " + it.joinToString(" ") { printStmt(it) }
                } ?: ""
                "(if ${print(stmt.condition)} ${stmt.thenBranch.joinToString(" ") { printStmt(it) }}$elsePart)"
            }
            is Stmt.While -> "(while ${print(stmt.condition)} ${stmt.body.joinToString(" ") { printStmt(it) }})"
            is Stmt.Combo -> "(combo ${stmt.actions.joinToString(" ") { printStmt(it) }})"
            is Stmt.Expression -> print(stmt.expression)
            is Stmt.Block -> "(block ${stmt.statements.joinToString(" ") { printStmt(it) }})"
            is Stmt.Champion -> ""
        }
    }
}
