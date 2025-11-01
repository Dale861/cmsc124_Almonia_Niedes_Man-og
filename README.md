# LSL - League of Legends Script Language

**Creators:** Dale Almonster & Stefan Niedesmonster

---

## Language Overview

LSL is an experimental domain-specific programming language designed to script League of Legends champion behavior and game mechanics. Its main characteristics are:

- Game-centric syntax with LoL-specific keywords (e.g., `cast`, `ping`, `onAbilityCast`).
- Emphasis on clarity for competitive gaming strategy with readable operators and event-driven architecture.
- Supports champion declarations, event handlers, combos, and map awareness queries.
- Case-sensitive and whitespace-insensitive (except as token delimiters).

---

## Keywords

The following are reserved words in LSL and cannot be used as identifiers:

| Keyword | Purpose |
|---------|---------|
| `champion` | Declares a champion script |
| `onAbilityCast` | Event triggered when ability is cast |
| `onAttack` | Event triggered on basic attack |
| `onDeath` | Event triggered on champion death |
| `onHealthBelow` | Event triggered when health below threshold |
| `onManaBelow` | Event triggered when mana below threshold |
| `onEnemyApproach` | Event triggered when enemy approaches |
| `onAllyNearby` | Event triggered when ally is nearby |
| `onCooldownReady` | Event triggered when cooldown resets |
| `cast` | Cast an ability |
| `useItem` | Use an item from inventory |
| `attack` | Perform basic attack |
| `moveTo` | Move to location |
| `placeWard` | Place a ward at location |
| `ping` | Send ping to teammates |
| `recall` | Return to base |
| `teleport` | Teleport to location |
| `if` | Conditional statement |
| `else` | Else / otherwise branch |
| `while` | Looping construct |
| `combo` | Group multiple actions |
| `and` | Logical AND |
| `or` | Logical OR |
| `not` | Logical NOT |
| `true` | Boolean literal for true |
| `false` | Boolean literal for false |

---

## Operators

**Arithmetic:**
- `+` (addition)
- `-` (subtraction)
- `*` (multiplication)
- `/` (division)

**Comparison:**
- `==` (equal to)
- `!=` (not equal to)
- `<` (less than)
- `>` (greater than)
- `<=` (less than or equal to)
- `>=` (greater than or equal to)

**Logical:**
- `and` (logical AND)
- `or` (logical OR)
- `not` (logical NOT)

**Assignment:**
- `=` (assign value)

---

## Literals

- **Numbers**: Integers (`600`), floating point numbers (`30.5`). Invalid formats like `12.34.56` cause an error.
- **Strings**: Enclosed in double quotes (`"Heal"`, `"Follow"`). Unterminated strings cause an error.
- **Booleans**: `true`, `false`.
- **Map Locations**: `dragonPit`, `baronPit`, `topLane`, `midLane`, `botLane`, `triBush`, `river`, `base`.

---

## Identifiers

- Must begin with a letter (a–z, A–Z).
- May contain letters and digits (a–z, A–Z, 0–9).
- Cannot match reserved keywords.
- Case-sensitive (`Champion` and `champion` are different).

---

## Comments

- **Line comments**: Start with `//` and continue until the end of the line.
- **Block comments**: Enclosed by `/* ... */`.
- Nested block comments are **not** supported. Unterminated block comments cause an error.

---

## Syntax Style

- Whitespace is ignored except as a separator between tokens.
- Statements can span multiple lines without terminators.
- Blocks are delimited by `{ ... }`.
- Grouping for expressions uses parentheses `( ... )`.

---

## Sample Code

## Design Rationale

**Game-Centric Keywords**: LSL uses League of Legends terminology (`cast`, `ping`, `combo`, `ward`) to make code intuitive for competitive players.

**Event-Driven Architecture**: Instead of imperative programming, LSL uses event handlers (`onAttack`, `onHealthBelow`) that naturally mirror how champions behave in-game.

**Familiar Syntax**: C-like braces, operators, and control flow make LSL accessible while keeping it unique to gaming.

**Domain-Specific Functions**: Built-in functions like `enemyInRange()` and `placeWard()` eliminate verbose game state queries.

**Error Handling**: Scanner explicitly checks for unterminated strings and block comments to prevent runtime crashes.

**Simplicity First**: Only basic constructs (champion events, conditions, combos) keep the language minimal and easy to extend.

**Case Sensitivity**: Allows flexibility and avoids ambiguity with keywords.

---
