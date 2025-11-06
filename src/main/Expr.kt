package main

sealed class Expr {
    // Champion declaration
    data class Champion(val name: Token, val events: List<EventHandler>) : Expr()

    // Event handler (onAbilityCast, onHealthBelow, etc.)
    data class EventHandler(val eventType: Token, val params: List<Token>, val body: List<Stmt>) : Expr()

    // Function call (cast, ping, useItem, etc.)
    data class Call(val callee: Token, val args: List<Expr>) : Expr()

    // Binary expression (and, or, ==, <, >, etc.)
    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr()

    // Unary expression (not)
    data class Unary(val operator: Token, val right: Expr) : Expr()

    // Literal value (numbers, strings, true, false)
    data class Literal(val value: Any?) : Expr()

    // Variable/identifier reference
    data class Variable(val name: Token) : Expr()

    // Grouping (parenthesized expressions)
    data class Grouping(val expression: Expr) : Expr()
}

// Statement types
sealed class Stmt {
    // If statement
    data class If(val condition: Expr, val thenBranch: List<Stmt>, val elseBranch: List<Stmt>?) : Stmt()

    // Champion statement
    data class Champion(val name: Token, val events: List<Expr.EventHandler>) : Stmt()

    // While loop
    data class While(val condition: Expr, val body: List<Stmt>) : Stmt()

    // Combo block
    data class Combo(val actions: List<Stmt>) : Stmt()

    // Expression statement
    data class Expression(val expression: Expr) : Stmt()

    // Block statement
    data class Block(val statements: List<Stmt>) : Stmt()
}
