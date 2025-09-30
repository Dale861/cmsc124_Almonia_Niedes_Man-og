class Scanner(private val source: String) {

    private val tokens = mutableListOf<Token>()
    private var start = 0
    private var current = 0
    private var line = 1

    private val keywords = mapOf(
        "var" to TokenType.VAR,
        "tuod" to TokenType.TRUE,
        "hindituod" to TokenType.FALSE,
        "kag" to TokenType.AND,
        "ukon" to TokenType.OR,
        "kung" to TokenType.IF,
        "iban" to TokenType.ELSE,
        "samtang" to TokenType.WHILE,
        "sakada" to TokenType.FOR,
        "fun" to TokenType.FUN,
        "balik" to TokenType.RETURN,
        "klase" to TokenType.CLASS,
        "print" to TokenType.PRINT,
        "null" to TokenType.NULL
    )

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        val c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.MULTIPLY)
            '%' -> addToken(TokenType.MODULO)

            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)

            '/' -> {
                when {
                    match('/') -> { // line comment
                        while (peek() != '\n' && !isAtEnd()) advance()
                    }
                    match('*') -> { // block comment
                        blockComment()
                    }
                    else -> addToken(TokenType.DIVIDE)
                }
            }

            ' ', '\r', '\t' -> { /* ignore whitespace */ }
            '\n' -> {
                    line++
            }

            '"' -> string()

            else -> {
                when {
                    isDigit(c) -> number()
                    isAlpha(c) -> identifier()
                    else -> errorAtChar("Unexpected character '$c'.")
                }
            }
        }
    }

    private fun blockComment() {
        while (!isAtEnd()) {
            if (peek() == '\n') line++
            if (peek() == '*' && peekNext() == '/') {
                advance() // *
                advance() // /
                return
            }
            advance()
        }
       errorAtChar("Unterminated block comment")
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()
        val text = source.substring(start, current)
        val type = keywords[text] ?: TokenType.IDENTIFIER
        addToken(type)
    }

    private fun number() {
        while (isDigit(peek())) advance()
        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) advance()
        }
        val lex = source.substring(start, current)
        val value = try { lex.toDouble() } catch (_: Throwable) { null }
        addToken(TokenType.NUMBER, value)
    }

    private fun string() {
        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') line++
            advance()
        }
        if (isAtEnd()) {
            errorAtChar("Unterminated string.")
            return
        }
        advance() // closing quote
        val content = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, content)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false
        current++
        return true
    }

    private fun peek(): Char = if (isAtEnd()) '\u0000' else source[current]
    private fun peekNext(): Char = if (current + 1 >= source.length) '\u0000' else source[current + 1]
    private fun advance(): Char = source[current++]
    private fun isAtEnd(): Boolean = current >= source.length

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun isDigit(c: Char): Boolean = c in '0'..'9'
    private fun isAlpha(c: Char): Boolean =
        (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'
    private fun isAlphaNumeric(c: Char): Boolean = isAlpha(c) || isDigit(c)

    private fun errorAtChar(message: String) {
        println("[line $line] Error: $message")
    }
}
