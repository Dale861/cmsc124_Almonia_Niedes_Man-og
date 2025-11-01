package main

enum class TokenType {
    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, PERCENT,

    // One or two character tokens
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    PLUS, MINUS, STAR, SLASH,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords - Champion/Ability related
    CHAMPION, ABILITY, ITEM, BUFF,

    // Keywords - Events
    ON_ABILITY_CAST, ON_ATTACK, ON_DEATH,
    ON_HEALTH_BELOW, ON_MANA_BELOW, ON_ENEMY_APPROACH,
    ON_ALLY_NEARBY, ON_COOLDOWN_READY,

    // Keywords - Actions
    CAST, USE_ITEM, ATTACK, MOVE_TO, PLACE_WARD,
    PING, RECALL, TELEPORT,

    // Keywords - Queries
    ENEMY_IN_RANGE, NEAREST_ENEMY, ALLY_IN_RANGE,
    HAS_BUFF, ITEM_READY, COOLDOWN_READY,
    GET_HEALTH, GET_MANA,

    // Keywords - Control Flow
    IF, ELSE, WHILE, FOR, DO, END,
    COMBO, THEN,

    // Keywords - Logic
    AND, OR, NOT, TRUE, FALSE,

    // Keywords - Map Objects
    DRAGON_PIT, BARON_PIT, TOP_LANE, MID_LANE, BOT_LANE,
    TRI_BUSH, RIVER, BASE,

    EOF
}