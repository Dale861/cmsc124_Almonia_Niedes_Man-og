package main

fun main() {
    println("Welcome to Man og Language")
    println("Enter your code (type 'gwa' to quit, 'dalagan' to execute current code):")

    val buffer = mutableListOf<String>()

    while (true) {
        print("> ")
        val line = readLine() ?: break

        when {
            line.trim().lowercase() == "gwa" -> break
            line.trim().lowercase() == "dalagan" -> {
                if (buffer.isNotEmpty()) {
                    // Parse each line separately
                    for (inputLine in buffer) {
                        if (inputLine.trim().isEmpty()) continue

                        // Scanner phase
                        val scanner = Scanner(inputLine)
                        val tokens = scanner.scanTokens()

                        // Parser phase
                        val parser = Parser(tokens)
                        val expression = parser.parse()

                        // Print result
                        if (expression != null) {
                            val printer = AstPrinter()
                            println(printer.print(expression))
                        } else {
                            println("Parse error occurred.")
                        }
                    }

                    println()
                    buffer.clear()
                } else {
                    println("Buffer is empty!")
                }
            }
            else -> {
                buffer.add(line)
            }
        }
    }
    println("Goodbye!")
}
