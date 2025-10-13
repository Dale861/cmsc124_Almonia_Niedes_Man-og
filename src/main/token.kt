enum class TokenType {
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, DIVIDE, MULTIPLY, MODULO,
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,
    IDENTIFIER, STRING, NUMBER, VAR, NULL,
    TRUE, FALSE, AND, OR,
    IF, ELSE, WHILE, FOR,
    FUN, RETURN, CLASS, PRINT, EOF
}

data class Token (
    val type: TokenType,
    val lexeme: String,
    val literal: Any?,
    val line: Int
) {
    override fun toString(): String {
        return "Token(type=$type, lexeme=$lexeme, literal:$literal, line:$line)"
    }
}