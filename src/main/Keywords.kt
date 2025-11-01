package  main

object Keywords {
    // Champion/Ability Keywords
    val CHAMPION_KEYWORDS = mapOf(
        "champion" to TokenType.CHAMPION,
        "ability" to TokenType.ABILITY,
        "item" to TokenType.ITEM,
        "buff" to TokenType.BUFF
    )

    // Event Keywords
    val EVENT_KEYWORDS = mapOf(
        "onAbilityCast" to TokenType.ON_ABILITY_CAST,
        "onAttack" to TokenType.ON_ATTACK,
        "onDeath" to TokenType.ON_DEATH,
        "onHealthBelow" to TokenType.ON_HEALTH_BELOW,
        "onManaBelow" to TokenType.ON_MANA_BELOW,
        "onEnemyApproach" to TokenType.ON_ENEMY_APPROACH,
        "onAllyNearby" to TokenType.ON_ALLY_NEARBY,
        "onCooldownReady" to TokenType.ON_COOLDOWN_READY
    )

    // Action Keywords
    val ACTION_KEYWORDS = mapOf(
        "cast" to TokenType.CAST,
        "useItem" to TokenType.USE_ITEM,
        "attack" to TokenType.ATTACK,
        "moveTo" to TokenType.MOVE_TO,
        "placeWard" to TokenType.PLACE_WARD,
        "ping" to TokenType.PING,
        "recall" to TokenType.RECALL,
        "teleport" to TokenType.TELEPORT
    )

    // Query Keywords
    val QUERY_KEYWORDS = mapOf(
        "enemyInRange" to TokenType.ENEMY_IN_RANGE,
        "nearestEnemy" to TokenType.NEAREST_ENEMY,
        "allyInRange" to TokenType.ALLY_IN_RANGE,
        "hasBuff" to TokenType.HAS_BUFF,
        "itemReady" to TokenType.ITEM_READY,
        "cooldownReady" to TokenType.COOLDOWN_READY,
        "getHealth" to TokenType.GET_HEALTH,
        "getMana" to TokenType.GET_MANA
    )

    // Control Flow Keywords
    val CONTROL_KEYWORDS = mapOf(
        "if" to TokenType.IF,
        "else" to TokenType.ELSE,
        "while" to TokenType.WHILE,
        "for" to TokenType.FOR,
        "do" to TokenType.DO,
        "end" to TokenType.END,
        "combo" to TokenType.COMBO,
        "then" to TokenType.THEN
    )

    // Logic Keywords
    val LOGIC_KEYWORDS = mapOf(
        "and" to TokenType.AND,
        "or" to TokenType.OR,
        "not" to TokenType.NOT,
        "true" to TokenType.TRUE,
        "false" to TokenType.FALSE
    )

    // Map Object Keywords
    val MAP_KEYWORDS = mapOf(
        "dragonPit" to TokenType.DRAGON_PIT,
        "baronPit" to TokenType.BARON_PIT,
        "topLane" to TokenType.TOP_LANE,
        "midLane" to TokenType.MID_LANE,
        "botLane" to TokenType.BOT_LANE,
        "triBush" to TokenType.TRI_BUSH,
        "river" to TokenType.RIVER,
        "base" to TokenType.BASE
    )

    // Combined keywords map
    val ALL_KEYWORDS = CHAMPION_KEYWORDS + EVENT_KEYWORDS +
            ACTION_KEYWORDS + QUERY_KEYWORDS + CONTROL_KEYWORDS +
            LOGIC_KEYWORDS + MAP_KEYWORDS
}