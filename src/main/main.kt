fun main() {
    println("Welcome to Man og Language")
    while (true){
        print("> ")
        val line = readLine() ?: break

        val scanner = Scanner(line)
        val tokens = scanner.scanTokens()

        for(i in tokens)
            println(i)
    }
}
