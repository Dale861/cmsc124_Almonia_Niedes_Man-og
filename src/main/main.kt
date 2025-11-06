package main

fun main() {
    println("===========================================")
    println("   Welcome to LoL Script Language (LSL)")
    println("===========================================")
    println()
    println("Commands:")
    println("  'exit' or 'quit' - Exit the REPL")
    println("  'run' - Execute the current script buffer")
    println("  'clear' - Clear the script buffer")
    println("  'show' - Display the current script buffer")
    println()

    val buffer = mutableListOf<String>()
    val evaluator = Evaluator()

    while (true) {
        print("LSL> ")
        val line = readlnOrNull() ?: break
        val trimmed = line.trim().lowercase()

        when {
            trimmed == "exit" || trimmed == "quit" -> {
                println("Thanks for using LSL! Good luck on the Rift!")
                break
            }

            trimmed == "clear" -> {
                buffer.clear()
                println("Script buffer cleared.")
            }

            trimmed == "show" -> {
                if (buffer.isEmpty()) {
                    println("Buffer is empty!")
                } else {
                    println("Current script buffer:")
                    println("---")
                    buffer.forEach { println(it) }
                    println("---")
                }
            }

            trimmed == "run" -> {
                if (buffer.isEmpty()) {
                    println("Buffer is empty! Add some code first.")
                } else {
                    val source = buffer.joinToString("\n")
                    println("\nExecuting script...")
                    println("---")

                    try {
                        // Scanner phase
                        val scanner = Scanner(source)
                        val tokens = scanner.scanTokens()

                        // Parser phase
                        val parser = Parser(tokens)
                        val expression = parser.parse()

                            // Evaluate instead of printing AST
                            if (expression != null) {
                                val result = evaluator.evaluate(expression)
                                println(evaluator.stringify(result))
                        } else {
                            println("Parse error occurred.")
                        }
                    } catch (e: Evaluator.RuntimeError) {
                        println(e.message)
                    } catch (e: Exception) {
                        println("Error: ${e.message}")
                        e.printStackTrace()
                    }

                    println()
                }
            }

            trimmed.isEmpty() -> {
                // Ignore empty lines
            }

            else -> {
                buffer.add(line)
                println("  [Added to buffer]")
            }
        }
    }
}
