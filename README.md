# Man-og Language
**Creator:** Dale Louize Almonster, Stefan Niedesmonster

---

## Language Overview
TuodLang is a simple, experimental programming language designed to practice compiler construction concepts, specifically lexical scanning and tokenization. Its main characteristics are:
- C-like syntax with custom keywords (some in Filipino, e.g., `tuod`).
- Emphasis on clarity for beginners, with readable operators and familiar control structures.
- Supports identifiers, numbers, strings, comments, and common operators.
- Case-sensitive and whitespace-insensitive (except as token delimiters).

---

## Keywords
The following are reserved words in TuodLang and cannot be used as identifiers:

| Keyword     | Purpose                          |
|-------------|----------------------------------|
| `var`       | Declares a variable              |
| `tuod`      | Boolean literal for `true`       |
| `hindituod` | Boolean literal for `false`      |
| `kung`      | Conditional statement (`if`)     |
| `iban`      | Else / otherwise branch          |
| `samtang`   | Looping construct (`while`)      |
| `para`      | Looping construct (`for`)        |
| `balik`     | Return statement                 |
| `print`     | Output text to console           |

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
- `&&` (logical AND)
- `||` (logical OR)
- `!` (logical NOT)

**Assignment:**
- `=` (assign value)

---

## Literals

- **Numbers**: Integers (`123`), floating point numbers (`12.34`). Invalid formats like `12.34.56` cause an error.
- **Strings**: Enclosed in double quotes (`"Hello World"`). Unterminated strings cause an error.
- **Booleans**: `tuod` (true), `hindituod` (false).

---

## Identifiers
- Must begin with a letter (a–z, A–Z).
- May contain letters and digits (a–z, A–Z, 0–9).
- Cannot match reserved keywords.
- Case-sensitive (`Var` and `var` are different).

---

## Comments
- **Line comments**: Start with `//` and continue until the end of the line.
- **Block comments**: Enclosed by `/* ... */`.
- Nested block comments are **not** supported. Unterminated block comments cause an error.

---

## Syntax Style
- Whitespace is ignored except as a separator between tokens.
- Statements are terminated by a newline or semicolon.
- Blocks are delimited by `{ ... }`.
- Grouping for expressions uses parentheses `( ... )`.

---

## Sample Code

```man-og language
var x = 10
var y = 20

kung (x < y) {
    print("x is less than y")
} iban {
    print("x is greater or equal to y")
}

// Loop example
para (var i = 0; i < 5; i = i + 1) {
    print("Count: " + i)
}

/* Block comment
   This will be ignored
*/
balik tuod
```
## Design Rationale

Design Rationale

Custom Keywords: Blending English (var, print) and Filipino (tuod, kung, iban) makes the language approachable yet unique.

C-like Syntax: Easier to understand since many programmers are already familiar with braces, operators, and semicolons.

Error Handling: Scanner explicitly checks for unterminated strings and block comments to prevent runtime crashes.

Simplicity First: Only basic constructs (variables, conditionals, loops, functions) to keep the language minimal and easy to extend.

Case Sensitivity: Allows flexibility and avoids ambiguity with keywords.
