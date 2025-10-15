package main

class AstPrinter {
    fun print(expr: Expr): String {
        return when (expr) {
            is Expr.Binary -> "(${expr.operator.lexeme} ${print(expr.left)} ${print(expr.right)})"
            is Expr.Unary -> "(${expr.operator.lexeme} ${print(expr.right)})"
            is Expr.Literal -> expr.value.toString()
            is Expr.Grouping -> "(group ${print(expr.expression)})"
        }
    }
}